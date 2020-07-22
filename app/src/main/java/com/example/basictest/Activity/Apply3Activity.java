package com.example.basictest.Activity;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.basictest.R;
import com.example.basictest.base.BaseApply3Activity;
import com.example.basictest.utils.SpUtils;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.org.bjca.signet.component.core.activity.SignetCoreApi;

import cn.org.bjca.signet.component.core.bean.results.SignImageResult;

import cn.org.bjca.signet.component.core.callback.SetSignImageCallBack;

import cn.org.bjca.signet.component.core.enums.SetSignImgType;

public class Apply3Activity extends BaseApply3Activity {

    @BindView(R.id.tv_apply3_name)
    TextView tv_apply3_name;
    @BindView(R.id.tv_apply3_name1)
    TextView tv_apply3_name1;
    @BindView(R.id.tv_apply3_name2)
    TextView tv_apply3_name2;
    @BindView(R.id.tv_apply3_bank)
    TextView tv_apply3_bank;
    @BindView(R.id.lv_apply3)
    LinearLayout lv_apply3;
    //初始化下面两个界面
    @BindView(R.id.lv_apply3_auto)
    LinearLayout lv_apply3_auto;
    @BindView(R.id.rv_apply3_auto)
    RelativeLayout rv_apply3_auto;
    @BindView(R.id.iv_apply3_yes)
    ImageView iv_apply3_yes;
    @BindView(R.id.im_apply_auto)
    ImageView im_apply_auto;
    @BindView(R.id.tv_apply3_auto_date)
    TextView tv_apply3_auto_date;
    @BindView(R.id.tv_apply3_reauto)
    TextView tv_apply3_reauto;
    @BindView(R.id.iv_apply3_yes1)
    ImageView iv_apply3_yes1;
    @BindView(R.id.lv_apply3_record)
    LinearLayout lv_apply3_record;
    //这是录像成功后的界面
    @BindView(R.id.rv_apply3_record)
    RelativeLayout rv_apply3_record;
    @BindView(R.id.im_apply_record)
    ImageView im_apply_record;
    @BindView(R.id.tv_apply3_re_record)
    TextView tv_apply3_re_record;
    @BindView(R.id.tv_apply3_record_date)
    TextView tv_apply3_record_date;
    //初始化按钮
    @BindView(R.id.sbtn_apply3_next)
    Button sbtn_apply3_next;

    @BindView(R.id.topbar_apply3)
    QMUITopBarLayout mTopBar;


    private Intent intent;
    private String name="王文哲",
            bank,
            videoPath,
            imagePath,
            phone="13205401086",
            idcard="370284199803310014";
    private Context mContext=Apply3Activity.this;
    private Bitmap handWritingBitmap;
    private String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply3);
        ButterKnife.bind(this);
        initTopBar();
        initBtn();
        //第一步，检测是否有证
        showProgressDialog(mContext,"请稍后。。。");
        getNativeUserList(mContext,name,idcard,phone);
        //第二步，添加证书
        initView();

        //从别的页面跳转回来不会调用onCreate，只会调用onRestart、onStart、onResume
    }

    private void initView(){
        name= SpUtils.getInstance(this).getString("name",null);
        bank=SpUtils.getInstance(this).getString("bank",null);
        tv_apply3_name.setText(name);
        tv_apply3_name1.setText(name);
        tv_apply3_name2.setText(name);
        tv_apply3_bank.setText(bank);
        //取录像信息
        src=getIntent().getStringExtra("base64str");
        imagePath=getIntent().getStringExtra("imagepath");
        if (imagePath!=null){
            getRecord();
        }
        if (src!=null){
            handWritingBitmap=base64ToBitmap(src);
            getAuto();
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        //若二者都有即可进行下一步
//        if (imagePath!=null&&src!=null){
//            sbtn_apply3_next.setEnabled(true);
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initBtn(){

        sbtn_apply3_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog(mContext,"加载中");
                setupPdf(lv_apply3,rv_apply3_auto);
                uploadPdf(mContext);

            }
        });
        lv_apply3_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handWriting(mContext);
            }
        });
        //点击录像
        lv_apply3_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(mContext,ShipingongzhenActivity.class);
                intent.putExtra("basestr",src);
                startActivity(intent);
            }
        });

        tv_apply3_reauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAuto();
            }
        });

        tv_apply3_re_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reRecord();
            }
        });

        im_apply_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(mContext,VideoActivity.class);
                startActivity(intent);
            }
        });
    }

    //如果正常录像调用此方法
    private void getRecord(){
        iv_apply3_yes1.setVisibility(View.VISIBLE);
        rv_apply3_record.setVisibility(View.VISIBLE);
        lv_apply3_record.setVisibility(View.INVISIBLE);
        im_apply_record.setImageURI(getImageContentUri(mContext,new File(imagePath)));
    }
    //重新录像
    private void reRecord(){
        iv_apply3_yes1.setVisibility(View.INVISIBLE);
        rv_apply3_record.setVisibility(View.INVISIBLE);
        lv_apply3_record.setVisibility(View.VISIBLE);
        intent=new Intent(mContext,ShipingongzhenActivity.class);
        startActivity(intent);
    }

    //手写签名
    public void handWriting(Context con) {
        SignetCoreApi.useCoreFunc(
                new SetSignImageCallBack(con, msspId, SetSignImgType.SET_HANDWRITING) {

                    @Override
                    public void onSetSignImageResult(SignImageResult setSignImageResult) {
                        src = setSignImageResult.getSignImageSrc();
                        Log.d("shouxie", src);
                        Log.d("手写", setSignImageResult.getErrMsg());
                        Log.d("手写", setSignImageResult.getErrCode());
                        handWritingBitmap=base64ToBitmap(src);
                        if(setSignImageResult.getErrCode()!="0x11000001"){
                            getAuto();
                        }
                    }

                });
    }

    private void getAuto(){
        iv_apply3_yes.setVisibility(View.VISIBLE);
        rv_apply3_auto.setVisibility(View.VISIBLE);
        lv_apply3_auto.setVisibility(View.INVISIBLE);
        im_apply_auto.setImageBitmap(handWritingBitmap);
    }

    private void reAuto(){
        iv_apply3_yes.setVisibility(View.INVISIBLE);
        rv_apply3_auto.setVisibility(View.INVISIBLE);
        lv_apply3_auto.setVisibility(View.VISIBLE);
        handWriting(mContext);
    }
}