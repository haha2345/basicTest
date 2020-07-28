package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;



import com.example.basictest.Fragments.MainFragment;
import com.example.basictest.Fragments.MineFragment;
import com.example.basictest.R;

import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    private MainFragment mainFragment;
    private MineFragment mineFragment;


    private EasyNavigationBar navigationBar;
    private String[] tabText={"首页","我的"};
    private int[] normalIcon={R.drawable.main_1,R.drawable.mine_2};
    private int[] selectIcon={R.drawable.main_2,R.drawable.mine_1};
    private List<Fragment> fragments=new ArrayList<>();


    private String username;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment=new MainFragment();
        mineFragment=new MineFragment();

        //initViews();
        //底部导航栏
        navigationBar=findViewById(R.id.nvBar);
        fragments.add(mainFragment);
        fragments.add(mineFragment);
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .build();



    }

    //传参 先传activity，再通过activity传给fragment
    private void initViews(){
        intent=getIntent();
        username=intent.getStringExtra("username");
        Bundle bundle=new Bundle();
        bundle.putString("username",username);
        mineFragment.setArguments(bundle);



    }

    public EasyNavigationBar getNavigationBar() {
        return navigationBar;
    }
}