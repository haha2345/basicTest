package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.example.basictest.utils.Utils;
import com.google.gson.JsonObject;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.org.bjca.identifycore.callback.IdentifyCallBack;
import cn.org.bjca.identifycore.enums.CtidActionType;
import cn.org.bjca.identifycore.enums.CtidModelEnum;
import cn.org.bjca.identifycore.impl.BJCAIdentifyAPI;
import cn.org.bjca.identifycore.params.BJCAAuthModel;
import cn.org.bjca.identifycore.params.CtidReturnParams;
import top.androidman.SuperButton;

public class Apply2edActivity extends AppCompatActivity {

    @BindView(R.id.et_apply2_phone)
    EditText et_apply2_phone;
    @BindView(R.id.et_apply2_vcode)
    EditText et_apply2_vcode;
    @BindView(R.id.tv_apply2_vcode)
    TextView tv_apply2_vcode;
    @BindView(R.id.sbtn_apply2_verify)
    Button sbtn_apply2_verify;
    @BindView(R.id.topbar_apply2)
    QMUITopBarLayout mTopBar;

    String caseId,userId,username,vcode,uuid,token,name;
    String TAG="Apply2";
    //TEST
    private CtidReturnParams ctidReturnParams;
    private String authinfo;

    Utils utils=new Utils();
    Intent intent;
    Context mContext=Apply2edActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply2ed);
        ButterKnife.bind(this);
        initData();
        initTopBar();
        initBtns();
    }

    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftImageButton(R.drawable.back, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        //设置标题名
        mTopBar.setTitle("赋强公证申请");
    }

    private void initData(){
        caseId= SpUtils.getInstance(this).getString("caseId",null);
        token=SpUtils.getInstance(this).getString("token",null);
    }

    private void initBtns(){
        username=et_apply2_phone.getText().toString();
        vcode=et_apply2_vcode.getText().toString();
        sbtn_apply2_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=et_apply2_phone.getText().toString();
                vcode=et_apply2_vcode.getText().toString();
                checkVcode();
            }
        });


        tv_apply2_vcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=et_apply2_phone.getText().toString();
                if (username!=null){
                    getUuid();
                }
            }
        });
    }

    private void getUuid(){
        HttpRequest.GET(mContext, netConstant.getGetVcodeURL() + "?mobile=" + username, null, new JsonResponseListener() {
            @Override
            public void onResponse(JsonMap main, Exception error) {
                if (error == null) {
                    //先判断是否正常
                    if(main.getString("code").equals("200")){
                        uuid=main.getString("uuid");
                        utils.showToastInThread(mContext,"已发送验证码，注意查收"+uuid);
                    }
                } else {
                    Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkVcode(){
        HttpRequest.build(mContext,netConstant.getCheckSMSCodeURL())
                .addParameter("username",username)
                .addParameter("code",vcode)
                .addParameter("uuid",uuid)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error==null){
                            Log.d(TAG,main.toString());
                            if (main.getString("code").equals("200")){
                                Log.d(TAG,"核验正确");
                                test2nd();
                                postAuthinfo(authinfo);

                            }else{
                                Log.e(TAG,main.getString("msg"));
                                Toast.makeText(mContext,main.getString("msg"),Toast.LENGTH_SHORT);
                            }
                        }
                    }
                })
                .doGet();
    }

    //初始化 获取authinfo
    private void test2nd(){
        ctidReturnParams= BJCAIdentifyAPI.initialCtidIdentify(mContext, CtidModelEnum.MODEL_0X12, CtidActionType.AUTH_ACTION);
        authinfo=ctidReturnParams.getValue();
        Log.d("status",ctidReturnParams.getStatus());
        Log.d("msg",ctidReturnParams.getMessage());
        Log.d("auth",authinfo);
    }
    //提交authorinfo
    private void postAuthinfo(final String authinfo){
        HttpRequest.build(mContext,netConstant.getApplyURL())
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("Content-Type","application/json")
                .setJsonParameter("{\"authInfo\":\""+authinfo+"\"}")
                .setJsonResponseListener(new JsonResponseListener() {

                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error == null) {
                            JsonMap result=main.getJsonMap("data");
                            Log.d("没错",result.getString("authResultInfo"));
                            testIdentify(result.getString("authResultInfo"));
                        } else {
                            Log.e("有错",error.toString());
                        }
                    }
                })
                .doPost();

    }

    //测试识人接口
    private void testIdentify(final String value){
        BJCAIdentifyAPI.actionCtidIdentify(mContext, value, CtidModelEnum.MODEL_0X12, CtidActionType.AUTH_ACTION
                , new BJCAAuthModel(), true, new IdentifyCallBack(mContext) {
                    @Override
                    public void onIdentifyCallBack(CtidReturnParams ctidReturnParams) {
                        String val=ctidReturnParams.getValue();
                        Log.d("status",ctidReturnParams.getStatus());
                        Log.d("msg",ctidReturnParams.getMessage());
                        Log.d("value",val);
                        //传值
                        model0x12(val);
                    }

                    @Override
                    public void onPreExecute() {

                    }
                });
    }

    private void model0x12(String value){
//        String jsonStr="{\n" +
//                "    \"caseId\":\""+caseId+"\",\n" +
//                "    \"authInfo\":\""+value+"\",\n" +
//                "    \"personMobile\":\""+username+"\"\n" +
//                "}";
        //test
        String jsonStr="{\n" +
                "    \"caseId\":\""+caseId+"\",\n" +
                "    \"authInfo\":\""+value+"\",\n" +
                "    \"personMobile\":\""+username+"\"\n" +
                "}";
        HttpRequest.build(mContext,netConstant.getModel0x12URL())
                .addHeaders("Authorization","Bearer "+token)
                .addHeaders("Content-Type","application/json")
                .setJsonParameter(jsonStr)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error == null) {
                            Log.d("没错",main.toString());
                            if (main.getString("code").equals("200")){
                                JsonMap result=main.getJsonMap("data");
                                Log.d(TAG,result.getString("name"));
                                //姓名
                                name=result.getString("name");
                                intent=new Intent(mContext,Apply3Activity.class);
                                startActivity(intent);
                            }else {
                                final String msg=main.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        } else {
                            Log.e("有错",error.toString());
                        }
                    }

                })
                .doPost();
        SpUtils.getInstance(this).setString("name",name);
    }
}