package com.example.basictest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkManager {
    private OkHttpClient client;
    private volatile static OkManager manager;   //防止多个线程访问时
    private final String TAG = OkManager.class.getSimpleName();  //获得类名

    private OkManager() {
        client = new OkHttpClient();
    }

    //采用单例模式获取对象
    public static OkManager getInstance() {
        OkManager instance = null;
        if (manager == null) {
            synchronized (OkManager.class) {                //同步代码块
                if (instance == null) {
                    instance = new OkManager();
                    manager = instance;
                }
            }
        }
        return instance;
    }




    public static void postFile(Context context,String url, RequestBody requestBody , okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        // 上传文件使用MultipartBody.Builder
        RequestBody body = requestBody;


        Request request = new Request.Builder()
                .header("Authorization","Bearer "+SpUtils.getInstance(context).getString("token",null))
                .addHeader("Content-Type","multipart/form-data")
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
