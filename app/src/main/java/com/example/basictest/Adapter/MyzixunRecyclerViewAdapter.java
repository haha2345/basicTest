package com.example.basictest.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basictest.Class.NoticeEntity;
import com.example.basictest.R;

import java.util.List;


public class MyzixunRecyclerViewAdapter extends RecyclerView.Adapter<MyzixunRecyclerViewAdapter.ViewHolder> {

    private List<NoticeEntity> datas;
    private Context context;

    public MyzixunRecyclerViewAdapter(Context context) {
        this.context=context;
    }
    public MyzixunRecyclerViewAdapter(Context context,List<NoticeEntity> datas){
        this.context=context;
        this.datas=datas;
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
        NoticeEntity noticeEntity=datas.get(position);
        vh.tv_zixun_title.setText(noticeEntity.getNoticeTitle());
        vh.tv_zixun_date.setText(noticeEntity.getUpdateTime());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "我被点击了"+position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_zixun_title;
        private TextView tv_zixun_date;


        public ViewHolder(View view) {
            super(view);
            tv_zixun_date=view.findViewById(R.id.tv_zixun_date);
            tv_zixun_title=view.findViewById(R.id.tv_zixun_title);

        }

        @Override
        public String toString() {
            return super.toString() + " '"  + "'";
        }
    }
}