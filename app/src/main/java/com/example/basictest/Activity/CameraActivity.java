package com.example.basictest.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.basictest.R;
import com.example.basictest.base.BaseCameraActivity;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.basictest.utils.DataCleanManager.clear;

public class CameraActivity extends BaseCameraActivity {


    String shiyan;
    TextView shiYan;

    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        bundle=getIntent().getExtras();
        shiyan=bundle.getString("shiyan");
        shiYan=findViewById(R.id.tv_camera);
/*        name = getIntent().getStringExtra("name");
        idcard = getIntent().getStringExtra("idcard");
        phone = getIntent().getStringExtra("phone");*/
        tipDialog = new QMUITipDialog.Builder(CameraActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord("录制失败，请重新录制")
                .create();
        onCreateActivity();
        videoWidth = 720;
        videoHeight = 1280;
        cameraWidth = 1280;
        cameraHeight = 720;
        shiYan.setText(shiyan);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                showProgressDialog(CameraActivity.this, "请稍后");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //要延时的程序
                        uploadVideo();
                    }
                }, 2000); //8000为毫秒单位
                //dismissProgressDialog();

//                Intent intent=new Intent(getBaseContext(), Apply3Activity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("imagepath",imagePath);
//                intent.putExtra("base64str",src);
//                startActivity(intent);
                Log.d("测试", "aaa");

            }
        });


    }

    //拦截器
    public void breaker(Context mContext) {
        clear(mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        //调到页面，关闭之前所有页面
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public void uploadVideo() {
        token = SpUtils.getInstance(this).getString("token", null);
//        caseId = SpUtils.getInstance(this).getString("caseId", null);
        caseId=bundle.getString("caseid");
        filepath = SpUtils.getInstance(this).getString("videopath", null);
        videoFile = new File(filepath);

        if (videoFile.exists()) {
            if (!IsFileInUse(filepath)) {
                HttpRequest.build(CameraActivity.this, netConstant.getUploadNotarizeVideo())
                        .setMediaType(baseokhttp3.MediaType.parse("video/mpeg4"))
                        .addHeaders("Authorization", "Bearer " + token)
                        .addHeaders("Content-Type", "multipart/form-data")
                        .addParameter("notarizeVideoFile", videoFile)
                        .addParameter("caseId", caseId)
                        .setJsonResponseListener(new JsonResponseListener() {
                            @Override
                            public void onResponse(JsonMap main, Exception error) {
//                                if (error!=null){
//                                    Log.e("上传","连接失败",error);
//                                    dismissProgressDialog();
//                                    Toast.makeText(CameraActivity.this,"连接失败,请重试",Toast.LENGTH_SHORT).show();
//                                }else {
                                if (main.getString("code").equals("200")) {
                                    //上传成功
                                    Intent intent = new Intent(getBaseContext(), Apply3Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.putExtra("name", name);
//                                    intent.putExtra("idcard", idcard);
//                                    intent.putExtra("phone", phone);
//                                    intent.putExtra("videopath", filepath);
//                                    intent.putExtra("imagepath", imagePath);
                                    bundle.putString("videopath", filepath);
                                    bundle.putString("imagepath", imagePath);
//                                    intent.putExtra("base64str", src);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    isJump=1;
                                    dismissProgressDialog();
                                } else if (main.getString("code").equals("401")) {
                                    dismissProgressDialog();
                                    breaker(CameraActivity.this);
                                } else {
                                    Log.e("上传", main.getString("msg"));
                                    Log.e("上传", main.getString("code"));
                                    dismissProgressDialog();
                                    ToastUtils.showShort(main.getString("msg"));
//                                    Toast.makeText(CameraActivity.this, main.getString("msg"), Toast.LENGTH_SHORT).show();

                                }
                            }
//                            }
                        })
                        .doPost();
            } else {
                //文件正在被操作
                Log.d("a", "uploadVideo: 文件正在被操作");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //do something
                        uploadVideo();
                    }
                }, 2000);    //延时1s执行
            }

        } else {

            ToastUtils.showShort("录制失败，请重新录制");
//            Toast.makeText(CameraActivity.this, "录制失败，请重新录制", Toast.LENGTH_SHORT).show();

            tv_camera_timer.setText("00:00");
            uploadBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setImageResource(R.drawable.error);
            recordBtn.setEnabled(true);
        }


    }


}