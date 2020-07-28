package com.example.basictest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.basictest.Activity.Apply1stActivity;
import com.example.basictest.Activity.Apply3Activity;
import com.example.basictest.Activity.HtmlActivity;
import com.example.basictest.Activity.JiluActivity;
import com.example.basictest.Activity.PdfViewerActivity;
import com.example.basictest.Activity.WenshuActivity;
import com.example.basictest.Adapter.MyzixunRecyclerViewAdapter;
import com.example.basictest.Activity.CameraActivity;
import com.example.basictest.Class.NoticeEntity;
import com.example.basictest.Class.NoticeListResponse;
import com.example.basictest.utils.SpUtils;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.androidman.SuperButton;


public class MainFragment extends Fragment {


    private String token;


    @BindView(R.id.btn_main_fqgz)
    SuperButton btn_main_fqgz;
    @BindView(R.id.btn_main_gzjl)
    SuperButton btn_main_gzjl;
    @BindView(R.id.btn_main_wsgl)
    SuperButton btn_main_wsgl;
    @BindView(R.id.btn_main_syzn)
    SuperButton btn_main_syzn;

    private List<NoticeEntity> datas=new ArrayList<>();
    private MyzixunRecyclerViewAdapter adapter,adapter1;

    private RecyclerView recyclerView,recyclerView1;
    //线程通信
    private static final int COMPLETED = 0;



    Intent intent;
    public MainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static MainFragment newInstance(String title) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        //设置行业资讯列表
        // Set the adapter
        recyclerView = view.findViewById(R.id.reView_main);
        recyclerView1=view.findViewById(R.id.reView_main1);
        token= SpUtils.getInstance(getActivity()).getString("token",null);

        setButtons();
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        getNoticeList();


        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(linearLayoutManager1);
        getNoticeList1();


    }

    private void setButtons(){
        btn_main_fqgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"赋强公正",Toast.LENGTH_SHORT);
                intent=new Intent(getActivity(), Apply1stActivity.class);
                startActivity(intent);
            }
        });
        btn_main_gzjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"公正记录",Toast.LENGTH_SHORT);
                intent=new Intent(getActivity(), JiluActivity.class);
                startActivity(intent);

            }
        });
        btn_main_wsgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"文书管理",Toast.LENGTH_SHORT);
                intent=new Intent(getActivity(), WenshuActivity.class);
                startActivity(intent);
            }
        });
        btn_main_syzn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent=new Intent(getActivity(), CameraActivity.class);
//                startActivity(intent);
//                Toast.makeText(getActivity(),"使用指南",Toast.LENGTH_SHORT);
                jumpToHowToUse();
            }
        });
    }

    private void getNoticeList(){
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getNoticeURL()+"?noticeType=3")
                .get()//默认就是GET请求，可以不写
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String res=response.body().string();
                    Log.d("a", "onResponse: " + res);
                    NoticeListResponse list=new Gson().fromJson(res,NoticeListResponse.class);
                    if (list.getCode()==200){
                        List<NoticeEntity> data=list.getRows();
                        adapter=new MyzixunRecyclerViewAdapter(getActivity(),data,4);
                        //分线程
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(adapter);

                            }
                        });
                    }else if (list.getCode()==401){

                    }

                }
                catch (IOException e){

                }
            }
        }).start();
    }
    private void getNoticeList1(){
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getNoticeURL()+"?noticeType=4")
                .get()//默认就是GET请求，可以不写
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String res=response.body().string();
                    Log.d("a", "onResponse: " + res);
                    NoticeListResponse list=new Gson().fromJson(res,NoticeListResponse.class);
                    if (list.getCode()==200){
                        List<NoticeEntity> data=list.getRows();
                        adapter1=new MyzixunRecyclerViewAdapter(getActivity(),data,1);
                        //分线程
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView1.setAdapter(adapter1);

                            }
                        });
                    }else if (list.getCode()==401){

                    }

                }
                catch (IOException e){

                }
            }
        }).start();
    }
    private void jumpToHowToUse(){
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getNoticeURL()+"?noticeType=6")
                .get()//默认就是GET请求，可以不写
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String res=response.body().string();
                    Log.d("a", "onResponse: " + res);
                    NoticeListResponse list=new Gson().fromJson(res,NoticeListResponse.class);
                    if (list.getCode()==200){
                        List<NoticeEntity> data=list.getRows();
                        Intent intent=new Intent(getContext(), HtmlActivity.class);
                        intent.putExtra("html",data.get(0).getNoticeContent());
                        intent.putExtra("title",data.get(0).getNoticeTitle());
                        getActivity().startActivity(intent);
                    }else if (list.getCode()==401){

                    }

                }
                catch (IOException e){

                }
            }
        }).start();
    }

}