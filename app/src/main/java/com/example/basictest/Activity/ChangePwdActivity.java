package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basictest.Class.User;
import com.example.basictest.Class.Utils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;

public class ChangePwdActivity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        initViews();
    }


    private void initViews(){
        et_change_verifyPwd=findViewById(R.id.et_change_verfy_pwd);
        et_changePwd=findViewById(R.id.et_change_pwd);
        btn_change_pwd=findViewById(R.id.btn_change_pwd);
        utils=new Utils();

        username=getIntent().getStringExtra("username");
        uuid=getIntent().getStringExtra("uuid");
        vcode=getIntent().getStringExtra("vcode");

        btn_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwd=et_changePwd.getText().toString();
                user=new User(uuid,username,pwd,vcode);
                Gson gson=new Gson();
                jsonStr=gson.toJson(user);
                Log.d(TAG,jsonStr);
                putToChangePwd(jsonStr);
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
                                Toast.makeText(ChangePwdActivity.this,"操作成功",Toast.LENGTH_LONG);
                                Log.d("LoginActivity",main);
                            }else{
                                Toast.makeText(ChangePwdActivity.this,"修改失败",Toast.LENGTH_LONG);
                            }

                        } else {
                            Toast.makeText(ChangePwdActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }}
                }).doPut();
    }

}