package com.example.basictest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Activity.JiluActivity;
import com.example.basictest.Activity.SettingActivity;
import com.example.basictest.Activity.WenshuActivity;
import com.example.basictest.R;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;


public class MineFragment extends Fragment {


    private String username;

    private TextView tv_mine_phone;
    private TextView tv_mine_uid;
    private QMUIGroupListView groupListView;

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
                        Toast.makeText(getActivity(),"公正记录" , Toast.LENGTH_SHORT).show();
                        intent=new Intent(getActivity(), JiluActivity.class);
                        startActivity(intent);
                    }
                })
                .addItemView(item_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"文书管理" , Toast.LENGTH_SHORT).show();
                        intent=new Intent(getActivity(), WenshuActivity.class);
                        startActivity(intent);

                    }
                })
                .addItemView(item_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"使用帮助" , Toast.LENGTH_SHORT).show();

                    }
                })
                .addItemView(item_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"公证处简介" , Toast.LENGTH_SHORT).show();

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


        return view;

    }
}