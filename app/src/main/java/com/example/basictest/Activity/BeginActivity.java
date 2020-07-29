package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.basictest.R;

public class BeginActivity extends AppCompatActivity {
    private static int SPLASH_DISPLAY_LENGHT= 2000;    //延迟2秒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(BeginActivity.this, LoginActivity.class);	//第二个参数即为执行完跳转后的Activity
                startActivity(intent);
                finish();   //关闭splashActivity，将其回收，否则按返回键会返回此界面
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}