package com.ailian.rxjava.rxjavatest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ailian.rxjava.rxjavatest.adapter.LogcatAdapter;
import com.ailian.rxjava.rxjavatest.bean.LogcatBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    protected static List<LogcatBean> logcats;
    protected RecyclerView recyclerView;
    protected LogcatAdapter logcatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logcats == null){
            logcats = new ArrayList<>();
        }
        if (logcatAdapter == null){
            logcatAdapter = new LogcatAdapter(logcats);
        }
    }

    protected void setAdapter(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(logcatAdapter);
    }

    protected void addLogcat(final LogcatBean logcatBean){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logcatAdapter.addDatas(logcatBean);
                recyclerView.scrollToPosition(logcatAdapter.getItemCount()-1);
            }
        });
    }

}
