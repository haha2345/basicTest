package com.example.basictest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class CameraActivity extends BaseCameraActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        tipDialog = new QMUITipDialog.Builder(CameraActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord("录制失败，请重新录制")
                .create();
        onCreateActivity();
        videoWidth = 720;
        videoHeight = 1280;
        cameraWidth = 1280;
        cameraHeight = 720;
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                showProgressDialog(CameraActivity.this,"请稍后");
                uploadVideo();
//                Intent intent=new Intent(getBaseContext(), Apply3Activity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("imagepath",imagePath);
//                intent.putExtra("base64str",src);
//                startActivity(intent);
                Log.d("测试","aaa");

            }
        });





    }


    public void uploadVideo(){
        token= SpUtils.getInstance(this).getString("token",null);
        caseId=SpUtils.getInstance(this).getString("caseId",null);
        filepath=SpUtils.getInstance(CameraActivity.this).getString("videopath",null);
        videoFile=new File(filepath);
        if (videoFile!=null){
            HttpRequest.build(CameraActivity.this, netConstant.getUploadNotarizeVideo())
                    .setMediaType(baseokhttp3.MediaType.parse("video/mpeg4"))
                    .addHeaders("Authorization","Bearer "+token)
                    .addHeaders("Content-Type","multipart/form-data")
                    .addParameter("notarizeVideoFile",videoFile)
                    .addParameter("caseId",caseId)
                    .setJsonResponseListener(new JsonResponseListener() {
                        @Override
                        public void onResponse(JsonMap main, Exception error) {
                            dismissProgressDialog();
                            if (error!=null){
                                Log.e("上传","连接失败",error);
                                Toast.makeText(CameraActivity.this,"连接失败,请重试",Toast.LENGTH_SHORT).show();
                            }else {
                                if (main.getString("code").equals("200")){
                                    //上传成功
                                    Intent intent=new Intent(getBaseContext(), Apply3Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("imagepath",imagePath);
                                    intent.putExtra("base64str",src);
                                    startActivity(intent);
                                    Log.d("上传","成功");

                                }else {
                                    Log.e("上传",main.getString("msg"));
                                    Log.e("上传",main.getString("code"));
                                    Toast.makeText(CameraActivity.this,main.getString("msg"),Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    })
                    .doPost();
        }else {
            tipDialog.show();

            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
                    tipDialog.dismiss();
                }
            }, 2000);
            tv_camera_timer.setText("00:00");
            uploadBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setImageResource(R.drawable.error);
            recordBtn.setEnabled(true);
        }


    }

}