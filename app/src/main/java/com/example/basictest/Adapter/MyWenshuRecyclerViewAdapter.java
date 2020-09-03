package com.example.basictest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.basictest.Activity.PdfViewerActivity;
import com.example.basictest.Class.DummyContent.DummyItem;
import com.example.basictest.Class.JiluEntity;
import com.example.basictest.R;
import com.example.basictest.constant.netConstant;
import com.example.basictest.utils.SpUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.JsonResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;

import java.util.List;


public class MyWenshuRecyclerViewAdapter extends RecyclerView.Adapter<MyWenshuRecyclerViewAdapter.ViewHolder> {

    private List<JiluEntity> datas;
    private Context context;
    private int num;
    private String userid,caseid,token,url;
    private Intent intent;

    public MyWenshuRecyclerViewAdapter(Context context, List<JiluEntity> datas, int count) {
        this.context = context;
        this.datas = datas;
        this.num = count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_wenshuguanli, parent, false);
        MyWenshuRecyclerViewAdapter.ViewHolder viewHolder = new MyWenshuRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyWenshuRecyclerViewAdapter.ViewHolder vh = (MyWenshuRecyclerViewAdapter.ViewHolder) holder;


        final JiluEntity jiluEntity = datas.get(position);
        vh.tv_guanli_bank.setText(jiluEntity.getCoName());
        vh.tv_guanli_title.setText(jiluEntity.getLoanName());
        vh.tv_guanli_code.setText(jiluEntity.getCaseCode());
        vh.tv_guanli_date.setText(jiluEntity.getApplyTime());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userid=jiluEntity.getUserId();
                caseid=jiluEntity.getId();
                getPath("400010");
            }
        });

    }

    @Override
    public int getItemCount() {
        return num;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_guanli_title;
        TextView tv_guanli_code;
        TextView tv_guanli_bank;
        TextView tv_guanli_date;
        TextView tv_guanli_jump;


        public ViewHolder(View view) {
            super(view);
            tv_guanli_title = view.findViewById(R.id.tv_guanli_title);
            tv_guanli_code = view.findViewById(R.id.tv_guanli_code);
            tv_guanli_bank = view.findViewById(R.id.tv_guanli_bank);
            tv_guanli_date = view.findViewById(R.id.tv_guanli_date);
            tv_guanli_jump = view.findViewById(R.id.tv_guanli_jump);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

    private void getPath(String fileType){
        token = SpUtils.getInstance(context).getString("token", null);

        HttpRequest.build(context, netConstant.getGetCaseFilePathURL()+"?userId="+userid+"&caseId="+caseid+"&fileType="+fileType+"&arriveFlag=1")
                .addHeaders("Authorization", "Bearer " + token)
                .setJsonResponseListener(new JsonResponseListener() {
                    @Override
                    public void onResponse(JsonMap main, Exception error) {
                        if (error != null) {
                            Log.d("获取路径", "连接失败", error);
                        } else {
                            if (main.getString("code").equals("200")) {
                                url=main.getString("filePath");
                                //直接跳转
                                intent=new Intent(context, PdfViewerActivity.class);
                                intent.putExtra("url",netConstant.getURL()+url);
                                context.startActivity(intent);
                            } else {
                                Log.e("获取路径", main.getString("msg"));
                                Log.e("获取路径", main.getString("code"));
                            }
                        }
                    }
                })
                .doGet();

    }
}