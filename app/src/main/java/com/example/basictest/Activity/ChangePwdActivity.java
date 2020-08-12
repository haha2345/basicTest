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
import android.widget.Toast;

import com.example.basictest.Class.User;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.utils.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePwdActivity extends BaseActivity {

    private EditText et_changePwd;
    private EditText et_change_verifyPwd;
    private Button btn_change_pwd;

    private String username;
    private String pwd;
    private String rePwd;
    private String uuid;
    private String vcode;
    private String jsonStr;
    private String TAG="ChangePwdActivity";


    private User user=null;
    private Utils utils=null;
    @BindView(R.id.topbar_change)
    QMUITopBarLayout mTopBar;
    private QMUITipDialog tipDialog;
    private Context mContext=ChangePwdActivity.this;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        initViews();
        initTopBar();
    }

    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftImageButton(R.drawable.back,R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        //设置标题名
        mTopBar.setTitle("修改密码");

    }
    private void initViews(){
        et_change_verifyPwd=findViewById(R.id.et_change_verfy_pwd);
        et_changePwd=findViewById(R.id.et_change_pwd);
        btn_change_pwd=findViewById(R.id.btn_change_pwd);
        utils=new Utils();

        username=getIntent().getStringExtra("username");
        uuid=getIntent().getStringExtra("uuid");
        vcode=getIntent().getStringExtra("vcode");
        setOnFocusChangeErrMsg(et_changePwd,"password","密码不少于6位");
        setOnFocusChangeErrMsg(et_change_verifyPwd,"repwd","两次输入密码不同");

        btn_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwd=et_changePwd.getText().toString().trim();
                rePwd=et_change_verifyPwd.getText().toString().trim();
                if (!(isPasswordValid(pwd)&&isPwdCorrect(pwd,rePwd))){
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_FAIL,"请检查输入是否有误").show();
                    delayCloseTip();
                }else {
                    user=new User(uuid,username,pwd,vcode);
                    Gson gson=new Gson();
                    jsonStr=gson.toJson(user);
                    Log.d(TAG,jsonStr);
                    getTipDialog(QMUITipDialog.Builder.ICON_TYPE_LOADING,null).show();
                    putToChangePwd(jsonStr);
                }

            }
        });
    }



    private void putToChangePwd(String jsonStr) {

        HttpRequest.build(ChangePwdActivity.this,netConstant.getResetPwdURL())
                .addHeaders("Content-Type","application/json")
                .setJsonParameter(jsonStr)
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String main, Exception error) {
                        if (error == null) {
                            if (main.contains("200")){
                                tipDialog.dismiss();
                                showMessagePositiveDialog();
                                Log.d("LoginActivity",main);
                            }else{
                                tipDialog.dismiss();
                                String msg=utils.getJson(main).get("msg").getAsString();
                                getTipDialog(QMUITipDialog.Builder.ICON_TYPE_FAIL,msg).show();
                                delayCloseTip();
                            }

                        } else {
                            tipDialog.dismiss();
                            getTipDialog(QMUITipDialog.Builder.ICON_TYPE_FAIL,"连接失败").show();
                            delayCloseTip();

                        }}
                }).doPut();
    }

    // 校验密码不少于6位
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    //判断两次输入是否一致
    private  boolean isPwdCorrect(String pwd,String rePwd){
        return pwd.equals(rePwd);
    }

    public QMUITipDialog getTipDialog(int type, String str) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(type)
                .setTipWord(str)
                .create();
        return tipDialog;
    }
    //1.5s后关闭tipDIalog
    public void delayCloseTip(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //要延时的程序
                tipDialog.dismiss();
            }
        },1500);
    }
    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setMessage("修改成功，去登录")
                .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        breaker(ChangePwdActivity.this);
//成功跳转
                        intent=new Intent(mContext,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }
    private void setOnFocusChangeErrMsg(final EditText editText, final String inputType, final String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        pwd=et_changePwd.getText().toString();
                        rePwd=et_change_verifyPwd.getText().toString();
                        String inputStr = editText.getText().toString();
                        if (!hasFocus) {
                            if (inputType == "password") {
                                if (isPasswordValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }
                            if (inputType=="repwd"){
                                if (isPwdCorrect(rePwd,pwd)){
                                    editText.setError(null);
                                }else {
                                    editText.setError(errMsg);
                                }
                            }
                        }
                    }
                }
        );
    }


}