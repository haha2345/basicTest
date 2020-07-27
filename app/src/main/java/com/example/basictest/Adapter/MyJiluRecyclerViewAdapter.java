package com.example.basictest.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.basictest.Class.JiluEntity;
import com.example.basictest.R;
import com.example.basictest.Class.DummyContent.DummyItem;

import java.util.List;


public class MyJiluRecyclerViewAdapter extends RecyclerView.Adapter<MyJiluRecyclerViewAdapter.ViewHolder> {

    private int count=2;
    private List<JiluEntity> datas;
    private Context context;

    public MyJiluRecyclerViewAdapter(List<JiluEntity> items,int num) {
        count=num;
        datas = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gongzhengjilu, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ViewHolder vh=holder;
        JiluEntity jiluEntity=datas.get(position);
        vh.tv_jilu_bank.setText(jiluEntity.getCoName());
        vh.tv_jilu_title.setText(jiluEntity.getLoanName());
        vh.tv_jilu_number.setText(jiluEntity.getCaseCode());
        vh.tv_jilu_date.setText(jiluEntity.getApplyTime());
        if (jiluEntity.getFStatus().equals("1")){
        }else if (jiluEntity.getFStatus().equals("21")){
            vh.tv_jilu_state.setText("审核通过");
            vh.tv_jilu_state.setBackgroundColor(R.color.tv_success);
            vh.tv_jilu_state.setTextColor(R.color.tv_text_success);
        }else if (jiluEntity.getFStatus().equals("22")){
            vh.tv_jilu_state.setText("审核失败");
            vh.tv_jilu_state.setBackgroundColor(R.color.tv_fail);
            vh.tv_jilu_state.setTextColor(R.color.tv_text_fail);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_jilu_state;
        TextView tv_jilu_date;
        TextView tv_jilu_title;
        TextView tv_jilu_number;
        TextView tv_jilu_bank;



        public ViewHolder(View view) {
            super(view);
            tv_jilu_state=view.findViewById(R.id.tv_jilu_state);
            tv_jilu_date=view.findViewById(R.id.tv_jilu_date);
            tv_jilu_title=view.findViewById(R.id.tv_jilu_title);
            tv_jilu_number=view.findViewById(R.id.tv_jilu_number);
            tv_jilu_bank=view.findViewById(R.id.tv_jilu_bank);
        }

        @Override
        public String toString() {
            return super.toString() + " '"  + "'";
        }
    }
}