package com.example.basictest.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.basictest.R;
import com.example.basictest.utils.SpUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Apply3Activity extends AppCompatActivity {

    @BindView(R.id.tv_apply3_name)
    TextView tv_apply3_name;
    @BindView(R.id.tv_apply3_name1)
    TextView tv_apply3_name1;
    @BindView(R.id.tv_apply3_name2)
    TextView tv_apply3_name2;
    @BindView(R.id.tv_apply3_bank)
    TextView tv_apply3_bank;

    private String name,bank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply3);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        name= SpUtils.getInstance(this).getString("name",null);
        bank=SpUtils.getInstance(this).getString("bank",null);
        tv_apply3_name.setText(name);
        tv_apply3_name1.setText(name);
        tv_apply3_name2.setText(name);
        tv_apply3_bank.setText(bank);
    }

}