package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.utils.SpUtils;
import com.example.basictest.Class.User;
import com.example.basictest.utils.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements View.OnClickListener{


    //定义控件
    private TextView btn_getVcode;
    private Button btn_login;
    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_vcode;
    private TextView tv_jumpToRegister;
    private TextView tv_forgetPwd;
    private User user=null;
    private Utils utils=null;

    private String username;
    private String pwd;
    private String vcode;
    private String uuid;

    private SharedPreferences preferences;

    //定义弹出窗口
    QMUITipDialog qmuiTipDialog;

    //定义json串
    private String jsonStr;

    private int flag =0;

    public String TAG="LoginActivity";

    Intent registerIntent;
    Intent forgetPwdIntent;
    Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (SpUtils.getInstance(this).getString("token",null)!=null){
            jupmToMain();
        }
        initViews();
        setOnFocusChangeErrMsg(et_phone,"phone","手机号格式不正确");
        setOnFocusChangeErrMsg(et_pwd,"password","密码不少于6位");
        username=SpUtils.getInstance(this).getString("username",null);
        pwd=SpUtils.getInstance(this).getString("pwd",null);



    }

    private void initViews(){
        btn_getVcode=(TextView) findViewById(R.id.btn_getVcode);
        btn_login=(Button)findViewById(R.id.btn_login);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        et_vcode=(EditText)findViewById(R.id.et_vcode);
        tv_forgetPwd=(TextView)findViewById(R.id.tv_forgetPwd);
        tv_jumpToRegister=(TextView)findViewById(R.id.tv_jumpToRegister);

        //工具类
        utils=new Utils();

        btn_login.setOnClickListener(this);
        btn_getVcode.setOnClickListener(this);
        tv_jumpToRegister.setOnClickListener(this);
        tv_forgetPwd.setOnClickListener(this);


        //提示框
        qmuiTipDialog=new QMUITipDialog.Builder(LoginActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("请稍后")
                .create();

    }

    @Override
    public void onClick(View view) {

        username=et_phone.getText().toString();
        pwd=et_pwd.getText().toString();
        vcode=et_vcode.getText().toString();

        switch (view.getId()){
            //获取验证码
            case R.id.btn_getVcode:
                username=et_phone.getText().toString();
                //测试qmui的提示框
                qmuiTipDialog.show();
                //获取uuid和用户名
                getuuid();
                qmuiTipDialog.dismiss();
                //test
                //Toast toast = Toast.makeText(LoginActivity.this, "验证码已发送"+uuid, Toast.LENGTH_SHORT);
                //toast.show();
                break;

                //登录
            case R.id.btn_login:
                //获取文本框内对象
                username=et_phone.getText().toString();
                pwd=et_pwd.getText().toString();
                vcode=et_vcode.getText().toString();
                // 让密码输入框失去焦点,触发setOnFocusChangeErrMsg方法
                et_pwd.clearFocus();
                // 发送URL请求之前,先进行校验
                if (!(isTelphoneValid(username) && isPasswordValid(pwd))) {
                    Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {//初始化user对象，生成json
                    user = new User(uuid, username, pwd, vcode);
                    Gson gson = new Gson();
                    jsonStr = gson.toJson(user);
                    Log.d(TAG, jsonStr);
                    loginValidate();
                }
                break;
            case R.id.tv_jumpToRegister:
                registerIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                registerIntent.putExtra("username",username);
                startActivity(registerIntent);
                break;

            case R.id.tv_forgetPwd:
                forgetPwdIntent=new Intent(LoginActivity.this, ForgetPwdActivity.class);
                forgetPwdIntent.putExtra("username",username);
                startActivity(forgetPwdIntent);

                break;
            default:
                break;
        }
    }


    //post json
    private void loginValidate(){
        HttpRequest.JSONPOST(LoginActivity.this,netConstant.getLoginURL(), jsonStr, new ResponseListener() {
            @Override
            public void onResponse(String response, Exception error) {
                if (error == null) {
                    if (response.contains("200")){
                        String msg=utils.getJson(response).get("msg").getAsString();
                        utils.showToastInThread(LoginActivity.this,msg);
                        Log.d("LoginActivity",msg);
                        String token=utils.getJson(response).get("token").getAsString();
                        Log.d("a",token);
                        //储存
                        saveValue(token);
                        jupmToMain();
                        finish();
                    }else{
                        utils.showToastInThread(LoginActivity.this,"登陆失败");
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //get uuid
    private void getuuid(){
        HttpRequest.GET(LoginActivity.this,netConstant.getGetVcodeURL()+"?mobile="+username,null,new ResponseListener(){
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
                        utils.showToastInThread(LoginActivity.this,"已发送验证码，注意查收"+uuid);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
    //跳转页面
    private void jupmToMain(){
        //转到下个页面
        loginIntent=new Intent(LoginActivity.this,MainActivity.class);
        loginIntent.putExtra("username",username);
        startActivity(loginIntent);
    }

    private void saveValue(String token){
//        preferences=getSharedPreferences("info",MODE_PRIVATE);
//        SharedPreferences.Editor editor=preferences.edit();
//        editor.putString("username",username);
//        editor.putString("pwd",pwd);
//        editor.putString("token",token);
//        editor.commit();
        SpUtils.getInstance(this).setString("username",username,1800);
        SpUtils.getInstance(this).setString("pwd",pwd,1800);
        SpUtils.getInstance(this).setString("token",token,1800);
    }
}
