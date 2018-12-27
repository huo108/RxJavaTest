package com.ailian.rxjava.rxjavatest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

    protected void addLogcat(LogcatBean logcatBean){
        logcatAdapter.addDatas(logcatBean);
        recyclerView.scrollToPosition(logcatAdapter.getItemCount()-1);
    }

}
