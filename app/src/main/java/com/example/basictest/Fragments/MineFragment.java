package com.example.basictest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Activity.HtmlActivity;
import com.example.basictest.Activity.JiluActivity;
import com.example.basictest.Activity.LoginActivity;
import com.example.basictest.Activity.SettingActivity;
import com.example.basictest.Activity.WenshuActivity;
import com.example.basictest.Class.NoticeEntity;
import com.example.basictest.Class.NoticeListResponse;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.basictest.utils.DataCleanManager.clear;


public class MineFragment extends Fragment {


    private String username;

    private TextView tv_mine_phone;
    private TextView tv_mine_uid;
    private QMUIGroupListView groupListView;
    private String token;

    QMUICommonListItemView item_1,item_2,item_3,item_4,item_5;
    Intent intent;
    private int flag=0;



    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        //获取username
        //tv_mine_test=getView().findViewById(R.id.tv_mine_test);
        //Bundle bundle=getArguments();
        //username=bundle.getString("username");
        //tv_mine_test.setText(username);
        if (flag==0){
            initList();
        }

    }


    private void initList(){
        flag=1;

        groupListView=getActivity().findViewById(R.id.groupListView);

        item_1 = groupListView.createItemView(ContextCompat.getDrawable(getContext(), R.drawable.gognzhengjilu),
                "公正记录",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        item_1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_2 = groupListView.createItemView(ContextCompat.getDrawable(getContext(), R.drawable.wenshuguanli),
                "文书管理",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        item_2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_3 = groupListView.createItemView(ContextCompat.getDrawable(getContext(), R.drawable.shiyongzhinan),
                "使用帮助",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        item_3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_4 = groupListView.createItemView(ContextCompat.getDrawable(getContext(), R.drawable.jianjie),
                "公证处简介",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        item_4.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        item_5 = groupListView.createItemView(ContextCompat.getDrawable(getContext(), R.drawable.shezhi),
                "系统设置",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        item_5.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(getContext())
                .setTitle(null)
                .addItemView(item_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(),"公正记录" , Toast.LENGTH_SHORT).show();
                        intent=new Intent(getActivity(), JiluActivity.class);
                        startActivity(intent);
                    }
                })
                .addItemView(item_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(),"文书管理" , Toast.LENGTH_SHORT).show();
                        intent=new Intent(getActivity(), WenshuActivity.class);
                        startActivity(intent);

                    }
                })
                .addItemView(item_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(),"使用帮助" , Toast.LENGTH_SHORT).show();
                        jumpTo("5");

                    }
                })
                .addItemView(item_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(),"公证处简介" , Toast.LENGTH_SHORT).show();
                        jumpTo("7");
                    }
                })
                .addItemView(item_5, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"系统设置" , Toast.LENGTH_SHORT).show();
                        intent=new Intent(getActivity(), SettingActivity.class);
                        startActivity(intent);

                    }
                })
                .addTo(groupListView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mine, container, false);
        tv_mine_phone=view.findViewById(R.id.tv_mine_phone);
        String phone= SpUtils.getInstance(getActivity()).getString("username",null);

        tv_mine_phone.setText(phone);
        return view;

    }
    private void jumpTo(String type){
        token=SpUtils.getInstance(getActivity()).getString("token",null);
        String header="Bearer "+token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .addHeader("Authorization",header)
                .url(netConstant.getNoticeURL()+"?noticeType="+type)
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
                        clear(getActivity());
                        intent=new Intent(getActivity(), LoginActivity.class);
                        //调到页面，关闭之前所有页面
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),list.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                catch (IOException e){

                }
            }
        }).start();
    }
}