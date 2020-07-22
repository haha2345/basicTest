package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class PdfViewerActivity extends AppCompatActivity {
    QMUITopBarLayout mTopBar;
    WebView pdf;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdf=findViewById(R.id.pdf_view);
        mTopBar=findViewById(R.id.topbar_pdf);
        initTopBar();
        url=getIntent().getStringExtra("url");
        pdf.getSettings().setJavaScriptEnabled(true);
        pdf.getSettings().setSupportZoom(true);
        pdf.getSettings().setAllowUniversalAccessFromFileURLs(true);
        pdf.getSettings().setBuiltInZoomControls(true);
        pdf.getSettings().setDisplayZoomControls(true);

        pdf.getSettings().setAllowUniversalAccessFromFileURLs(true); //设置可以访问URL

        //file:///android_asset/pdf.html?+网络路径
        pdf.loadUrl("file:///android_asset/pdf.html?"+url);
    }
    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftImageButton(R.drawable.back, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        //设置标题名
        mTopBar.setTitle("查看PDF");
    }
}