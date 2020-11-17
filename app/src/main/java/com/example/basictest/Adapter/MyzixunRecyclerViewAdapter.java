package com.example.basictest.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Activity.HtmlActivity;
import com.example.basictest.Class.NoticeEntity;
import com.example.basictest.R;

import java.util.List;


public class MyzixunRecyclerViewAdapter extends RecyclerView.Adapter<MyzixunRecyclerViewAdapter.ViewHolder> {

    private List<NoticeEntity> datas;
    private Context context;
    private int count;

    public MyzixunRecyclerViewAdapter(Context context) {
        this.context=context;
    }
    public MyzixunRecyclerViewAdapter(Context context,List<NoticeEntity> datas,int count){
        this.context=context;
        this.datas=datas;
        this.count=count;
    }

    public void setDatas(List<NoticeEntity> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_zixun, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewHolder vh=(ViewHolder)holder;
        final NoticeEntity noticeEntity=datas.get(position);
        vh.tv_zixun_title.setText(noticeEntity.getNoticeTitle());

        if (noticeEntity.getUpdateTime()==null){
            vh.tv_zixun_date.setText(noticeEntity.getCreateTime());
        }else {
            vh.tv_zixun_date.setText(noticeEntity.getUpdateTime());
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "我被点击了"+position, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, HtmlActivity.class);
                intent.putExtra("html",noticeEntity.getNoticeContent());
                intent.putExtra("title",noticeEntity.getNoticeTitle());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_zixun_title;
        private TextView tv_zixun_date;
        private TextView tv_zixun_subtitle;

        public ViewHolder(View view) {
            super(view);
            tv_zixun_date=view.findViewById(R.id.tv_zixun_date);
            tv_zixun_title=view.findViewById(R.id.tv_zixun_title);
            tv_zixun_subtitle=view.findViewById(R.id.tv_zixun_subtitle);
        }

        @Override
        public String toString() {
            return super.toString() + " '"  + "'";
        }
    }
}