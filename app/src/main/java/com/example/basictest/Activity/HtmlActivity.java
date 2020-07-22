package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.example.basictest.R;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class HtmlActivity extends AppCompatActivity {
    QMUITopBarLayout mTopBar;
    WebView html;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        html=findViewById(R.id.html_view);
        mTopBar=findViewById(R.id.topbar_html);

        html.getSettings().setJavaScriptEnabled(true);
        html.getSettings().setBlockNetworkImage(false);
//webSettings.setBuiltInZoomControls(true);
//webSettings.setSupportZoom(true);
        html.getSettings().setUseWideViewPort(true);
        html.getSettings().setLoadWithOverviewMode(true);
        html.getSettings().setAllowFileAccess(true);
        html.getSettings().setSupportMultipleWindows(true);
        html.getSettings().setDomStorageEnabled(true);
        html.getSettings().setDefaultFontSize(30); //设置显示字体的大小
        html.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }
}