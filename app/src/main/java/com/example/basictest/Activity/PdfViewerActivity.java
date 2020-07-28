package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.DownloadUtil;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.OnDownloadListener;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfViewerActivity extends AppCompatActivity {
    QMUITopBarLayout mTopBar;
    WebView pdf;
    String url;
    Button btn;
    QMUITipDialog qmuiTipDialog;
    Context mContext = PdfViewerActivity.this;
    //加载框
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdf = findViewById(R.id.pdf_view);
        mTopBar = findViewById(R.id.topbar_pdf);
        btn = findViewById(R.id.btn_pdf);
        initTopBar();
        qmuiTipDialog = new QMUITipDialog.Builder(PdfViewerActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("成功保存在"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/fuqianggognzheng")
                .create();
        url = getIntent().getStringExtra("url");
        pdf.getSettings().setJavaScriptEnabled(true);
        pdf.getSettings().setSupportZoom(true);
        pdf.getSettings().setAllowUniversalAccessFromFileURLs(true);
        pdf.getSettings().setBuiltInZoomControls(true);
        pdf.getSettings().setDisplayZoomControls(true);

        pdf.getSettings().setAllowUniversalAccessFromFileURLs(true); //设置可以访问URL

        //file:///android_asset/pdf.html?+网络路径
        pdf.loadUrl("file:///android_asset/pdf.html?" + url);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PdfViewerActivity.this, url, Toast.LENGTH_SHORT).show();
                Download();
            }
        });
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

    private void Download() {
//        DownloadUtil.get().download(url,
//                Environment.getExternalStorageDirectory().getAbsolutePath(),
//                new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+"ss.pdf",
//                new DownloadUtil.OnDownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(File file) {
//
//                        dismissProgressDialog();
//                        qmuiTipDialog.show();
//
//                    }
//
//                    @Override
//                    public void onDownloading(int progress) {
//                        showProgressDialog(PdfViewerActivity.this, "下载中，请稍后" + progress + "%");
//                    }
//
//                    @Override
//                    public void onDownloadFailed(Exception e) {
//
//                    }
//                });

        HttpRequest.DOWNLOAD(
                mContext,
                url,
                new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "fuqianggongzheng"), new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date())+"ss.pdf"),
                new OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        dismissProgressDialog();
//                        Toast.makeText(mContext, "文件已下载完成：" + file.getAbsolutePath(), Toast.LENGTH_LONG);
                        showDialog("文件下载完成，保存在："+file.getAbsolutePath());
                    }

                    @Override
                    public void onDownloading(int progress) {
                        showProgressDialog(PdfViewerActivity.this, "下载中，请稍后" + progress + "%");
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT);
                    }
                }
        );
    }

    //显示加载框
    public void showProgressDialog(Context mContext, String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);    //设置内容
        progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
        progressDialog.show();

        //设置超时自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dismissProgressDialog()) {

                }
            }
        }, 60000);//超时时间60秒
    }

    //取消加载框
    public Boolean dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                return true;//取消成功
            }
        }
        return false;//已经取消过了，不需要取消
    }

    private void showDialog(String str) {
        new AlertDialog.Builder(mContext)
                .setTitle("下载成功")
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}