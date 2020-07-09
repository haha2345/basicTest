package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.basictest.Adapter.MyWenshuRecyclerViewAdapter;
import com.example.basictest.Adapter.MyzixunRecyclerViewAdapter;
import com.example.basictest.Class.DummyContent;
import com.example.basictest.R;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WenshuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @BindView(R.id.topbar_wenshu)
    QMUITopBarLayout mTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenshu);
        ButterKnife.bind(this);
        setAdapter();
        initTopBar();

    }

    private void setAdapter(){
        recyclerView=findViewById(R.id.reView_wenshu);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyWenshuRecyclerViewAdapter(DummyContent.ITEMS));

    }

    @SuppressLint("ResourceAsColor")
    private void initTopBar() {
        mTopBar.setBackgroundColor(R.color.qmui_config_color_gray_8);
        mTopBar.setBackgroundAlpha(255);
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置标题名
        mTopBar.setTitle("文书管理");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }
}