package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.listener.OnDownloadListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.tv_detail_bank)
    TextView tv_detail_bank;
    @BindView(R.id.tv_detail_state)
    TextView tv_detail_state;
    @BindView(R.id.tv_detail_title)
    TextView tv_detail_title;
    @BindView(R.id.tv_detail_no)
    TextView tv_detail_no;
    @BindView(R.id.tv_detail_name)
    TextView tv_detail_name;
    @BindView(R.id.tv_detail_date)
    TextView tv_detail_date;
    @BindView(R.id.tv_detail_detial1)
    TextView tv_detail_detial1;
    @BindView(R.id.tv_detail_detial2)
    TextView tv_detail_detial2;
    @BindView(R.id.tv_detail_title_success)
    TextView tv_detail_title_success;
    @BindView(R.id.tv_detial_success)
    TextView tv_detial_success;
    @BindView(R.id.btn_detial)
    Button btn_detial;
    @BindView(R.id.btn_detail_re)
    Button btn_detial_re;
    @BindView(R.id.topbar_detail)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.rv_detail)
    RelativeLayout rv_detail;
    private Context mContext=DetailActivity.this;
    private Intent intent;
    private ProgressDialog progressDialog;
    private String name,bank,caseid,casecode,date,fstate,userid,caseRegCode,loanName,token,url,auditReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initTopBar();
        initView();
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
        mTopBar.setTitle("查看详情");
    }

    @SuppressLint("ResourceAsColor")
    private void initView(){
        fstate=getIntent().getStringExtra("fstate");
        casecode=getIntent().getStringExtra("casecode");
        caseid=getIntent().getStringExtra("caseid");
        bank=getIntent().getStringExtra("bank");
        date=getIntent().getStringExtra("date");
        userid=getIntent().getStringExtra("userid");
        name=getIntent().getStringExtra("name");
        caseRegCode=getIntent().getStringExtra("caseRegcode");
        loanName=getIntent().getStringExtra("loanname");
        auditReason=getIntent().getStringExtra("auditReason");
        tv_detail_bank.setText(bank);
        tv_detail_no.setText(casecode);
        tv_detail_date.setText(date);
        tv_detail_name.setText(name);
        if (fstate.equals("1")){

        }else if (fstate.equals("21")){//审核通过
            iv_detail.setImageResource(R.drawable.yes3);
            tv_detail_state.setText("审核通过");
            tv_detail_title.setText("您的审核已通过！");
            rv_detail.setVisibility(View.VISIBLE);
            tv_detail_no.setText(caseRegCode);
//            tv_detail_title_success.setText(loanName);
        }else if (fstate.equals("22")){
            iv_detail.setImageResource(R.drawable.error);
            tv_detail_state.setText("审核失败");
            tv_detail_title.setText(auditReason);
            tv_detail_title.setTextColor(getResources().getColor(R.color.tv_fail_detail));
            btn_detial_re.setVisibility(View.VISIBLE);

        }

        tv_detail_detial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPath("100010");
            }
        });
        tv_detail_detial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPath("300010");
            }
        });
        tv_detial_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPath("400010");
            }
        });
        btn_detial_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(mContext,Apply1stActivity.class);
                startActivity(intent);
            }
        });
        btn_detial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnlyPath("400010");
                url=netConstant.getURL()+url;
                Toast.makeText(mContext, url, Toast.LENGTH_LONG).show();
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
                                showProgressDialog(mContext, "下载中，请稍后" + progress + "%");
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT);
                            }
                        }
                );
            }
        });

    }
    private void getPath(String fileType){
        token = SpUtils.getInstance(this).getString("token", null);

        HttpRequest.build(mContext, netConstant.getGetCaseFilePathURL()+"?userId="+userid+"&caseId="+caseid+"&fileType="+fileType)
                .addHeaders("Authorization", "Bearer " + token)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error != null) {
                            Log.d("获取路径", "连接失败", error);
                        } else {
                            if (main.getString("code").equals("200")) {
                                url=main.getString("filePath");
                                //直接跳转
                                intent=new Intent(mContext,PdfViewerActivity.class);
                                intent.putExtra("url",netConstant.getURL()+url);
                                startActivity(intent);
                            } else {
                                Log.e("获取路径", main.getString("msg"));
                                Log.e("获取路径", main.getString("code"));
                            }
                        }
                    }
                })
                .doGet();

    }
    private void getOnlyPath(String fileType){
        token = SpUtils.getInstance(this).getString("token", null);

        HttpRequest.build(mContext, netConstant.getGetCaseFilePathURL()+"?userId="+userid+"&caseId="+caseid+"&fileType="+fileType)
                .addHeaders("Authorization", "Bearer " + token)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error != null) {
                            Log.d("获取路径", "连接失败", error);
                        } else {
                            if (main.getString("code").equals("200")) {
                                url=main.getString("filePath");

                            } else {
                                Log.e("获取路径", main.getString("msg"));
                                Log.e("获取路径", main.getString("code"));
                            }
                        }
                    }
                })
                .doGet();

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