package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.basictest.Adapter.MyWenshuRecyclerViewAdapter;
import com.example.basictest.Adapter.MyzixunRecyclerViewAdapter;
import com.example.basictest.Class.DummyContent;
import com.example.basictest.Class.JiluEntity;
import com.example.basictest.Class.JiluListResponse;
import com.example.basictest.R;
import com.example.basictest.base.BaseApply3Activity;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WenshuActivity extends BaseApply3Activity {

    private RecyclerView recyclerView;

    @BindView(R.id.topbar_wenshu)
    QMUITopBarLayout mTopBar;

    private Context mContext=WenshuActivity.this;
    private String token;
    private MyWenshuRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenshu);
        ButterKnife.bind(this);
        token= SpUtils.getInstance(this).getString("token",null);
        initTopBar();
        getWenshuList();
    }

    private void initAdapter(){
        recyclerView=findViewById(R.id.reView_wenshu);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


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
        mTopBar.setTitle("文书管理");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    private void getWenshuList(){
        showProgressDialog(mContext,"加载中");
        HttpRequest.build(mContext, netConstant.getPersonalListURL())
                .addHeaders("Authorization","Bearer "+token)
                //.addParameter("fStatus", "21")
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String response, Exception error) {
                        dismissProgressDialog();
                        if (error == null) {
                            JiluListResponse list=new Gson().fromJson(response,JiluListResponse.class);
                            if (list.getCode()==200){
                                List<JiluEntity> data=list.getRows();
                                initAdapter();
                                adapter=new MyWenshuRecyclerViewAdapter(mContext,data);
                                recyclerView.setAdapter(adapter);
                            }else {

                            }

                        } else {


                        }
                    }
                })
                .doGet();
    }

}