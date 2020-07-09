package com.example.basictest.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.R;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


public class MineFragment extends Fragment {


    private String username;

    private TextView tv_mine_phone;
    private TextView tv_mine_uid;
    private QMUIGroupListView groupListView;



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

        initList();
    }


    private void initList(){
        groupListView=getActivity().findViewById(R.id.groupListView);

        QMUICommonListItemView item_1 = groupListView.createItemView("公正记录");
        item_1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView item_2 = groupListView.createItemView("文书管理");
        item_2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView item_3 = groupListView.createItemView("使用帮助");
        item_3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView item_4 = groupListView.createItemView("公证处简介");
        item_4.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView item_5 = groupListView.createItemView("系统设置");
        item_5.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(getContext())
                .setTitle(null)
                .addItemView(item_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"公正记录" , Toast.LENGTH_SHORT).show();
                    }
                })
                .addItemView(item_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"文书管理" , Toast.LENGTH_SHORT).show();

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

                    }
                })
                .addTo(groupListView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);



    }
}