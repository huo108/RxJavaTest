package com.ailian.rxjava.rxjavatest.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ailian.rxjava.rxjavatest.R;
import com.ailian.rxjava.rxjavatest.bean.LogcatBean;

import java.util.List;

/**
 * Created by wumh on 2018/12/27.
 */
public class LogcatAdapter extends RecyclerView.Adapter<LogcatAdapter.LogcatViewHolder> {

    private List<LogcatBean> mDatas;

    public LogcatAdapter(List<LogcatBean> datas) {
        this.mDatas = datas;
    }

    public void addDatas(LogcatBean logcatBean){
        mDatas.add(logcatBean);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogcatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_logcat, viewGroup, false);
        return new LogcatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LogcatViewHolder viewHolder, int i) {
        viewHolder.logcatContent.setText(mDatas.get(i).getContent());
        viewHolder.logcatTime.setText(mDatas.get(i).getLogTime());
        viewHolder.logcatTag.setText(mDatas.get(i).getTag());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class LogcatViewHolder extends RecyclerView.ViewHolder{
        public final TextView logcatContent;
        public final TextView logcatTime;
        public final TextView logcatTag;
        public LogcatViewHolder(View v) {
            super(v);
            logcatContent = v.findViewById(R.id.logcat_content);
            logcatTime = v.findViewById(R.id.logcat_time);
            logcatTag = v.findViewById(R.id.logcat_tag);
        }
    }
}
