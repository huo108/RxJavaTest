package com.ailian.rxjava.rxjavatest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ailian.rxjava.rxjavatest.databinding.ActivityCreateBinding;
import com.ailian.rxjava.rxjavatest.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * RxJava创建型操作符
 */
public class CreateActivity extends BaseActivity {
    private static final String TAG = "CreateActivity";
    ActivityCreateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setTitle("创建型操作符");
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create);
        binding.setClk(new Click());
        recyclerView = binding.include.logcatRecycler;
        setAdapter();
    }

    public class Click{
        public void clickCreate(){
            create();
        }

        public void showLog(){
           // logcat();
        }
    }

    private void create(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hello");
                emitter.onNext("world");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG,"onSubscribe"));
            }

            @Override
            public void onNext(String s) {
                addLogcat(LogUtil.dl(TAG,"onNext:" + s));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG,"onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG,"onComplete"));
            }
        });
    }
}
