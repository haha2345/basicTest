package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.example.basictest.R;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;




public class HtmlActivity extends AppCompatActivity {
    QMUITopBarLayout mTopBar;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        mTopBar = findViewById(R.id.topbar_html);
        title=getIntent().getStringExtra("title");
        initTopBar();
        //TextView htmlTextView = (TextView) findViewById(R.id.html_text);

//        Spanned formattedHtml = HtmlFormatter.formatHtml(new HtmlFormatterBuilder().setHtml(getIntent().getStringExtra("html")).setImageGetter(new HtmlResImageGetter(htmlTextView.getContext())));
//        htmlTextView.setText(formattedHtml);


        WebView webView=findViewById(R.id.webview_html);
        webView.loadDataWithBaseURL(null,setWebVIewImage(getIntent().getStringExtra("html")), "text/html", "UTF-8", null);



    }

    // 适配image和table标签
    public String setWebVIewImage(String star) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "<style>table{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + star + "</body></html>";

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
        mTopBar.setTitle(title);

    }
}