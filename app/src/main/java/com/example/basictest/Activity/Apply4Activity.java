package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.basictest.R;
import com.example.basictest.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.androidman.SuperButton;

public class Apply4Activity extends AppCompatActivity {

    @BindView(R.id.tv_apply4_no)
    TextView tv_apply4_no;
    @BindView(R.id.tv_apply4_name)
    TextView tv_apply4_name;
    @BindView(R.id.tv_apply4_date)
    TextView tv_apply4_date;
    @BindView(R.id.tv_apply4_bank)
    TextView tv_apply4_bank;
    @BindView(R.id.a45)
    TextView tv_filename;
    @BindView(R.id.tv_apply4_detial1)
    TextView tv_apply4_detial1;
    @BindView(R.id.tv_apply4_detial2)
    TextView tv_apply4_detial2;
    @BindView(R.id.sbtn_apply4)
    SuperButton sbtn_apply4;
    @BindView(R.id.tv_apply4_jump)
    TextView tv_apply4_jump;
    private String caseCode,name,date,bank,uploadfilename;
    private Intent intent;
    private Context mContext=Apply4Activity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply4);
        ButterKnife.bind(this);
        initView();
        initBtn();
    }

    private void initView(){
        caseCode= SpUtils.getInstance(this).getString("caseCode",null);
        name= SpUtils.getInstance(this).getString("name",null);
        bank= SpUtils.getInstance(this).getString("bank",null);
        uploadfilename= SpUtils.getInstance(this).getString("uploadfilename",null);
        tv_apply4_no.setText(caseCode);
        tv_apply4_name.setText(name);
        tv_apply4_bank.setText(bank);
        tv_filename.setText(uploadfilename);
    }

    private void initBtn(){
        sbtn_apply4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
            }
        });

        tv_apply4_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_apply4_detial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_apply4_detial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}