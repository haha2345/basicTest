package com.example.basictest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.basictest.Activity.JiluActivity;
import com.example.basictest.Activity.WenshuActivity;
import com.example.basictest.Adapter.MyzixunRecyclerViewAdapter;
import com.example.basictest.R;
import com.example.basictest.Class.DummyContent;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {



    @BindView(R.id.btn_main_fqgz)
    Button btn_main_fqgz;
    @BindView(R.id.btn_main_gzjl)
    Button btn_main_gzjl;
    @BindView(R.id.btn_main_wsgl)
    Button btn_main_wsgl;
    @BindView(R.id.btn_main_syzn)
    Button btn_main_syzn;


    Intent intent;
    public MainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        //设置行业资讯列表
        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.reView_main);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyzixunRecyclerViewAdapter(DummyContent.ITEMS));
        setButtons();
        return view;
    }

    private void setButtons(){
        btn_main_fqgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"赋强公正",Toast.LENGTH_SHORT);
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
                Toast.makeText(getActivity(),"使用指南",Toast.LENGTH_SHORT);
            }
        });
    }
}