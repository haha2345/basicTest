package com.example.basictest.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basictest.Activity.RegisterActivity;
import com.example.basictest.constant.netConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;

public class Utils extends AppCompatActivity {

    //解析json
    public JsonObject getJson(String jsonString){
        JsonObject responseBodyJSONObject =
                (JsonObject) new JsonParser()
                .parse(jsonString);
        return responseBodyJSONObject;
    }
    // 实现在子线程中显示Toast
    public void showToastInThread(final Context context, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    //get uuid
    public String getuuid(final Context context, String username){
        final String[] uuid = new String[1];
        HttpRequest.GET(context, netConstant.getGetVcodeURL()+"?mobile="+username,null,new ResponseListener(){
            @Override
            public void onResponse(String response, Exception error) {

                if (error == null) {
                    //先判断是否正常
                    if(response.contains("200")){
                        //解析json
                        JsonObject resultJson=getJson(response);
                        //提取uuid
                        uuid[0] =resultJson.get("uuid").getAsString();
                        //线程中无法直接使用toast
                        //测试用
                        showToastInThread(context,"已发送验证码，注意查收"+ uuid[0]);
                    }
                } else {
                    Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return uuid[0];
    }





}
