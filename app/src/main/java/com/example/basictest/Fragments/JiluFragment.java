package com.example.basictest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.basictest.Activity.LoginActivity;
import com.example.basictest.Adapter.MyJiluRecyclerViewAdapter;
import com.example.basictest.Class.JiluEntity;
import com.example.basictest.Class.JiluListResponse;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.google.gson.Gson;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.ResponseListener;

import java.util.List;

import static com.example.basictest.utils.DataCleanManager.clear;

/**
 * A fragment representing a list of Items.
 */
public class JiluFragment extends Fragment {

    private Context mContext = getActivity();
    private String token;
    private MyJiluRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String type;

    public JiluFragment(String type) {
        this.type = type;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jilu_list, container, false);
        token = SpUtils.getInstance(mContext).getString("token", null);
        // Set the adapter
        recyclerView = view.findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        getJiluList();

        return view;
    }


    private void getJiluList() {
        HttpRequest.build(mContext, netConstant.getPersonalListURL())
                .addHeaders("Authorization", "Bearer " + token)
                .addParameter("fStatus", type)
                .setResponseListener(new ResponseListener() {
                    @Override
                    public void onResponse(String response, Exception error) {
                        if (error == null) {
                            JiluListResponse list = new Gson().fromJson(response, JiluListResponse.class);
                            if (list.getCode() == 200) {
                                List<JiluEntity> data = list.getRows();
                                adapter = new MyJiluRecyclerViewAdapter(data, getActivity());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            } else if (list.getCode() == 401){
                                clear(mContext);
                                Intent intent=new Intent(mContext, LoginActivity.class);
                                //调到页面，关闭之前所有页面
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,list.getMsg(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .doGet();
    }
}