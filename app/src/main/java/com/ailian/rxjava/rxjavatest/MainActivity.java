package com.ailian.rxjava.rxjavatest;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ailian.rxjava.rxjavatest.databinding.ActivityMainBinding;
import com.ailian.rxjava.rxjavatest.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
   private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setSmt(new ClickHandler());
//        getActionBar().setTitle("功能列表");
    }

    public class ClickHandler{
        public void toCreate(){
            Intent intent = new Intent(MainActivity.this,CreateActivity.class);
            MainActivity.this.startActivity(intent);
//            justOperator();
        }
    }

    private void justOperator(){
        Observable.just("a","b","c","d").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.dl(TAG,"onSubscribe");
            }

            @Override
            public void onNext(String o) {
                LogUtil.d(TAG,"onNext 当前输出值:"+o);
                binding.show.setText("当前输出值:"+o);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d(TAG,"onError");
            }

            @Override
            public void onComplete() {
                LogUtil.d(TAG,"onComplete");
            }
        });
    }
}
