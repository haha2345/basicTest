package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.basictest.R;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.IntentUtil;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.androidman.SuperButton;

public class Apply4Activity extends BaseActivity {

    @BindView(R.id.tv_apply4_no)
    TextView tv_apply4_no;
    @BindView(R.id.tv_apply4_name)
    TextView tv_apply4_name;
    @BindView(R.id.tv_apply4_date)
    TextView tv_apply4_date;
    @BindView(R.id.tv_apply4_bank)
    TextView tv_apply4_bank;
    @BindView(R.id.a45)
    TextView tv_filename1;
    @BindView(R.id.a46)
    TextView tv_filename2;
    @BindView(R.id.tv_apply4_detial1)
    TextView tv_apply4_detial1;
    @BindView(R.id.tv_apply4_detial2)
    TextView tv_apply4_detial2;
    @BindView(R.id.sbtn_apply4)
    SuperButton sbtn_apply4;
    @BindView(R.id.topbar_apply4)
    QMUITopBarLayout mTopBar;
    private String caseCode,name,date,bank,uploadfilename,signedFileName,url,hetongurl,gaozhishuurl,token,userid,caseid;
    private Intent intent;
    private Context mContext=Apply4Activity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply4);
        ButterKnife.bind(this);
        initTopBar();
        initView();
        initBtn();
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
        mTopBar.setTitle("赋强公证申请");
    }

    private void initView(){
        date=getIntent().getStringExtra("date");
        signedFileName=getIntent().getStringExtra("filename");
        caseCode= SpUtils.getInstance(this).getString("caseCode",null);
        name= SpUtils.getInstance(this).getString("name",null);
        bank= SpUtils.getInstance(this).getString("bank",null);
        uploadfilename= SpUtils.getInstance(this).getString("uploadfilename",null);
        tv_apply4_no.setText(caseCode);
        tv_apply4_name.setText(name);
        tv_apply4_bank.setText(bank);
        tv_apply4_date.setText(date);
        tv_filename1.setText(uploadfilename);
        tv_filename2.setText(signedFileName);
        String videoPath=SpUtils.getInstance(mContext).getString("videopath",null);
        String imagePath=SpUtils.getInstance(mContext).getString("imagepath",null);
        String pdfPath=SpUtils.getInstance(mContext).getString("pdfpath",null);
        File video=new File(videoPath);
        File image=new File(imagePath);
        File pdf=new File(pdfPath);
        deleteFile(video);
        deleteFile(image);
        deleteFile(pdf);
    }

    private void initBtn(){
        sbtn_apply4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(mContext,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        tv_apply4_detial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取电子合同路径
                getPath("100010");
            }
        });

        tv_apply4_detial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPath("300010");
            }
        });
    }

    private void getPath(String fileType){
        token = SpUtils.getInstance(this).getString("token", null);
        caseid = SpUtils.getInstance(this).getString("caseId", null);
        userid = SpUtils.getInstance(this).getString("userId", null);

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
                            }  else if (main.getString("code").equals("401")){
                                breaker(mContext);
                            }else {
                                Log.e("获取路径", main.getString("msg"));
                                Log.e("获取路径", main.getString("code"));
                            }
                        }
                    }
                })
                .doGet();

    }

}