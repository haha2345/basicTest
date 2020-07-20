package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Class.User;
import com.example.basictest.utils.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_RegisterPhone;
    private EditText et_Password;
    private EditText et_RePassword;
    private EditText et_RegisterVCode;
    private CheckBox checkBox_register;
    private Button btn_Register;
    private TextView btn_getVCode;
    private TextView tv_JumpToLogin;
    private User user=null;
    private Utils utils=null;
    Intent intent;
    //定义json串
    private String jsonStr=null;

    private String TAG="RegisterActivity";

    private String username;
    private String pwd;
    private String rePwd;
    private String uuid;
    private String vcode;

    private QMUITipDialog qmuiTipDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        tv_JumpToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //失去焦点报错 此处为重复密码检测
        et_RePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                pwd=et_Password.getText().toString().trim();
                rePwd=et_RePassword.getText().toString().trim();
                if (isPwdCorrect(pwd,rePwd))
                    et_RePassword.setError(null);
                else
                    et_RePassword.setError("两次密码输入不同");
            }
        });
        setOnFocusChangeErrMsg(et_RegisterPhone,"phone","手机号输入不正确");
        setOnFocusChangeErrMsg(et_Password,"password","密码不少于6位");

    }

    private void initViews(){
        et_RegisterPhone=(EditText)findViewById(R.id.et_RegisterPhone);
        et_Password=(EditText)findViewById(R.id.et_Password);
        et_RePassword=(EditText)findViewById(R.id.et_RePassword);
        et_RegisterVCode=(EditText)findViewById(R.id.et_RegisterVCode);
        btn_Register=(Button)findViewById(R.id.btn_register);
        tv_JumpToLogin=(TextView) findViewById(R.id.tv_jumpToLogin);
        btn_getVCode=(TextView)findViewById(R.id.btn_getVcode_from_reg);
        checkBox_register=(CheckBox)findViewById(R.id.checkBox_register);

        btn_Register.setOnClickListener(this);
        btn_getVCode.setOnClickListener(this);

        utils=new Utils();
        intent = getIntent();
        et_RegisterPhone.setText(intent.getStringExtra("username"));

        qmuiTipDialog=new QMUITipDialog.Builder(RegisterActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("注册成功，请返回登录")
                .create();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                username=et_RegisterPhone.getText().toString();
                pwd=et_Password.getText().toString();
                vcode=et_RegisterVCode.getText().toString();
                // 让密码输入框失去焦点,触发setOnFocusChangeErrMsg方法
                et_RePassword.clearFocus();
                // 发送URL请求之前,先进行校验
                if (!(isTelphoneValid(username) && isPasswordValid(pwd))&&isPwdCorrect(pwd,rePwd)) {
                    Toast.makeText(this, "请检查输入是否有误！", Toast.LENGTH_SHORT).show();
                }
                user=new User(uuid,username,pwd,vcode);
                Gson gson=new Gson();
                jsonStr=gson.toJson(user);
                //Toast.makeText(RegisterActivity.this, jsonStr, Toast.LENGTH_SHORT).show();

                Log.d(TAG,jsonStr);
                //注册
                registerRequest();


                break;

            case R.id.btn_getVcode_from_reg:
                username=et_RegisterPhone.getText().toString();
                //Toast.makeText(RegisterActivity.this, username, Toast.LENGTH_SHORT).show();

                Log.d(TAG,username);
                getuuid();
                break;

        }
    }


    //get uuid
    private void getuuid(){
        HttpRequest.GET(RegisterActivity.this,netConstant.getGetVcodeURL()+"?mobile="+username,null,new ResponseListener(){
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
                        utils.showToastInThread(RegisterActivity.this,"已发送验证码，注意查收"+uuid);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerRequest(){
        HttpRequest.JSONPOST(this, netConstant.getRegisterURL(), jsonStr, new ResponseListener() {
            @Override
            public void onResponse(String main, Exception error) {

                if (error==null){
                    if (main.contains("200")){
                        String msg=utils.getJson(main).get("msg").getAsString();
                        Toast.makeText(RegisterActivity.this,msg,Toast.LENGTH_LONG).show();
                        Log.d(TAG,"注册成功");
                        //成功跳转
                        intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        qmuiTipDialog.show();
                    }
                }else {

                }
            }
        });
    }


    private void setOnFocusChangeErrMsg(final EditText editText, final String inputType, final String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
                        if (!hasFocus) {
                            if (inputType == "phone") {
                                if (isTelphoneValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }
                            if (inputType == "password") {
                                if (isPasswordValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }
                        }
                    }
                }
        );
    }

    // 校验账号不能为空且必须是中国大陆手机号（宽松模式匹配）
    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }

    // 校验密码不少于6位
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    private  boolean isPwdCorrect(String pwd,String rePwd){
        if (pwd.equals(rePwd))
            return true;
        else
            return false;
    }
}
