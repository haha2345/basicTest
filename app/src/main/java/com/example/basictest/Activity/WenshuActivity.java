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
import android.widget.TextView;
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
    @BindView(R.id.tv_wenshu)
    TextView tv_wenshu;

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
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        mTopBar.setTitle("文书管理");
    }

    private void getWenshuList(){
        HttpRequest.build(mContext, netConstant.getPersonalListURL())
                .addHeaders("Authorization","Bearer "+token)
                .addParameter("fStatus", "21")
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String response, Exception error) {
                        if (error == null) {
                            JiluListResponse list=new Gson().fromJson(response,JiluListResponse.class);
                            if (list.getCode()==200){
                                if (list.getTotal()<1){
                                    tv_wenshu.setVisibility(View.VISIBLE);
                                }else if (list.getCode()==401){
                                    breaker(mContext);
                                }
                                else {
                                    List<JiluEntity> data=list.getRows();
                                    initAdapter();
                                    adapter=new MyWenshuRecyclerViewAdapter(mContext,data,list.getTotal());
                                    recyclerView.setAdapter(adapter);
                                }

                            }else {
                                tv_wenshu.setVisibility(View.VISIBLE);
                            }

                        } else {


                        }
                    }
                })
                .doGet();
    }

}