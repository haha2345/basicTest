package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.basictest.R;
import com.example.basictest.base.BaseActivity;
import com.example.basictest.utils.DataCleanManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.basictest.utils.DataCleanManager.cleanFiles;
import static com.example.basictest.utils.DataCleanManager.cleanSharedPreference;
import static com.example.basictest.utils.DataCleanManager.clear;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.groupListView_setting)
    QMUIGroupListView groupListView;
    QMUICommonListItemView item_1,item_2,item_3,item_4;
    Intent intent;
    @BindView(R.id.topbar_setting)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.btn_setting)
    Button btn;

    private Context mContext=SettingActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initList();
        initTopBar();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //datacleanManager
                clear(mContext);
//                cleanFiles(mContext);
//                cleanSharedPreference(mContext);
                intent=new Intent(mContext,LoginActivity.class);
                //调到页面，关闭之前所有页面
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    private void initList(){


        item_2 = groupListView.createItemView("修改登录密码");
        item_2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_3 = groupListView.createItemView("清除缓存");
        item_3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_4 = groupListView.createItemView("版本号");
        // 去除 icon 的 tintColor 换肤设置
        QMUICommonListItemView.SkinConfig skinConfig = new QMUICommonListItemView.SkinConfig();
        skinConfig.iconTintColorRes = 0;
        item_4.setSkinConfig(skinConfig);
        item_4.setDetailText("V 1.0");


        QMUIGroupListView.newSection(this)
                .setTitle(null)
                .addItemView(item_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent=new Intent(mContext,ForgetPwdActivity.class);
                        startActivity(intent);

                    }
                })
                .addItemView(item_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//清除缓存
                        DataCleanManager.clearAllCache(mContext);
                        getTipDialog(mContext, QMUITipDialog.Builder.ICON_TYPE_SUCCESS,"缓存已清除").show();
                        delayCloseTip();

                    }
                })
                .addItemView(item_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(SettingActivity.this,"版本号" , Toast.LENGTH_SHORT).show();

                    }
                })
                .addTo(groupListView);
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
        mTopBar.setTitle("系统设置");
    }


}