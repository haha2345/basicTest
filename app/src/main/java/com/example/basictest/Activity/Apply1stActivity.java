package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.basictest.Class.SpUtils;
import com.example.basictest.Class.bankEntity;
import com.example.basictest.Class.bankListResponse;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.androidman.SuperButton;

public class Apply1stActivity extends AppCompatActivity {

    Intent intent;
    @BindView(R.id.topbar_apply1)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.sbtn_apply1)
    SuperButton sbtn;

    @BindView(R.id.spinner_apply)
    Spinner spinner;

    private String token;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply1st);
        ButterKnife.bind(this);
        token=SpUtils.getInstance(this).getString("token",null);
        initTopBar();
        initSpinner();
    }


    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundColor(R.color.qmui_config_color_gray_8);
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置标题名
        mTopBar.setTitle("系统设置");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }


    private void initSpinner(){
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getBankURL())
                .get()//默认就是GET请求，可以不写
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String res=response.body().string();
                    Log.d("a", "onResponse: " + res);
                    bankListResponse list=new Gson().fromJson(res, bankListResponse.class);
                    List<bankEntity> data=list.getRows();
                    List<String> datas=new ArrayList<String>();
                    for (int i=0;i<data.size();i++){
                        datas.add(data.get(i).getCoName());
                        Log.d("a",datas.get(i));
                    }
                    adapter=new ArrayAdapter<String>(Apply1stActivity.this,R.layout.item_spinner,R.id.tv_spinner,datas);
                    //分线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setAdapter(adapter);
                        }
                    });
                }
                catch (IOException e){

                }
            }
        }).start();
    }
}