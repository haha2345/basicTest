package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Class.User;
import com.example.basictest.Class.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.JsonObject;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_forgetPhone;
    private EditText et_forgetVcode;
    private Button btn_forget_getvcode;
    private Button btn_forget_next;
    private TextView tv_forget_test;

    private String username;
    private String vcode;
    private String uuid;

    private User user=null;
    private Utils utils=null;

    private QMUITipDialog qmuiTipDialog;

    private String jsonStr;

    private String TAG="ForgetPwdActivity";

    Intent intent=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initViews();

    }

    private void initViews(){
        et_forgetPhone=findViewById(R.id.et_forgetPhone);
        et_forgetVcode=findViewById(R.id.et_forgetVcode);
        btn_forget_getvcode=findViewById(R.id.btn_forget_getvcode);
        btn_forget_next=findViewById(R.id.btn_forget_next);
        tv_forget_test=findViewById(R.id.tv_forget_test);
        utils=new Utils();
        btn_forget_next.setOnClickListener(this);
        btn_forget_getvcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forget_getvcode:
                username=et_forgetPhone.getText().toString();
                getuuid();
                tv_forget_test.setText(uuid);
                break;
            case R.id.btn_forget_next:
                vcode=et_forgetVcode.getText().toString();
                intent=new Intent(ForgetPwdActivity.this,ChangePwdActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("uuid",uuid);
                intent.putExtra("vcode",vcode);
                startActivity(intent);
                break;
        }

    }

    //get uuid
    private void getuuid(){
        HttpRequest.GET(ForgetPwdActivity.this,netConstant.getGetVcodeURL()+"?mobile="+username,null,new ResponseListener(){
            @Override
            public void onResponse(String response, Exception error) {

                if (error == null) {
                    //先判断是否正常
                    if(response.contains("200")){
                        //解析json
                        JsonObject resultJson=utils.getJson(response);
                        //提取uuid
                        uuid=resultJson.get("uuid").getAsString();
                        //线程中无法直接使用toast
                        //测试用
                        utils.showToastInThread(ForgetPwdActivity.this,"已发送验证码，注意查收"+uuid);
                    }
                } else {
                    Toast.makeText(ForgetPwdActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}