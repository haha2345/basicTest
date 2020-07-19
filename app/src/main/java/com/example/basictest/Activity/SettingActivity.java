package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.basictest.R;
import com.example.basictest.utils.DataCleanManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {


    @BindView(R.id.groupListView_setting)
    QMUIGroupListView groupListView;
    QMUICommonListItemView item_1,item_2,item_3,item_4;
    Intent intent;
    @BindView(R.id.topbar_setting)
    QMUITopBarLayout mTopBar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initList();
        initTopBar();
    }

    private void initList(){


        item_1 = groupListView.createItemView("登录方式设置");
        item_1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

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
                .addItemView(item_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SettingActivity.this,"公正记录" , Toast.LENGTH_SHORT).show();

                    }
                })
                .addItemView(item_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SettingActivity.this,"文书管理" , Toast.LENGTH_SHORT).show();

                    }
                })
                .addItemView(item_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SettingActivity.this,"使用帮助" , Toast.LENGTH_SHORT).show();
                        DataCleanManager.clearAllCache(mContext);

                    }
                })
                .addItemView(item_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SettingActivity.this,"版本号" , Toast.LENGTH_SHORT).show();

                    }
                })
                .addTo(groupListView);
    }

    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置标题名
        mTopBar.setTitle("系统设置");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

}