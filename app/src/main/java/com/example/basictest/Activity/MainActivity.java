package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.basictest.Fragments.JiluFragment;
import com.example.basictest.Fragments.MainFragment;
import com.example.basictest.Fragments.MineFragment;
import com.example.basictest.R;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    private MainFragment mainFragment;
    private MineFragment mineFragment;
    private JiluFragment jiluFragment;

    private EasyNavigationBar navigationBar;
    private String[] tabText={"首页","我的"};
    private int[] normalIcon={R.mipmap.index,R.mipmap.me};
    private int[] selectIcon={R.mipmap.index1,R.mipmap.me1};
    private List<Fragment> fragments=new ArrayList<>();


    private String username;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment=new MainFragment();
        mineFragment=new MineFragment();
        jiluFragment=new JiluFragment();
        //initViews();
        //底部导航栏
        navigationBar=findViewById(R.id.nvBar);
        fragments.add(mainFragment);
        //fragments.add(mineFragment);
        fragments.add(jiluFragment);
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