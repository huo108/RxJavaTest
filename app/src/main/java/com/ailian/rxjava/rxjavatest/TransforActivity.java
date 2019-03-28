package com.ailian.rxjava.rxjavatest;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ailian.rxjava.rxjavatest.databinding.ActivityTransforBinding;
import com.ailian.rxjava.rxjavatest.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TransforActivity extends BaseActivity {
    private static final String TAG = "TransforActivity";
    private ActivityTransforBinding binding;
    private Disposable disposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfor);
        binding.setClk(new TransforActivity.Click());
        recyclerView = binding.include.logcatRecycler;
        setAdapter();
    }

    public class Click {
        public void clickMap() {
            map();
        }

        public void clickFlatMap() {
            flatMap();
        }

        public void clickConcatMap() {
            concatMap();
        }

        public void clickBuffer() {
            buffer();
        }
    }

    /**
     * 1、定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     * 2、应用场景:缓存被观察者发送的事件
     */
    private void buffer() {
        Observable.just(1, 2, 3, 4, 5).buffer(3, 1).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                addLogcat(LogUtil.dl(TAG, "数组长度:" + integers.size()));
                for (int i = 0; i < integers.size(); i++) {
                    addLogcat(LogUtil.dl(TAG, "buffer:" + integers.get(i)));
                }
            }
        });
    }

    /**
     * 1、与FlatMap（）的 区别在于：拆分 & 重新合并生成的事件序列 的顺序 = 被观察者旧序列生产的顺序
     * 2、应用场景:有序的将被观察者发送的整个事件序列进行变换
     */
    private void concatMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("flatMap1");
                emitter.onNext("flatMap2");
                emitter.onNext("flatMap3");
            }
        }).concatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String string) throws Exception {
                List<String> strs = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    strs.add(string + i);
                }
                return Observable.fromIterable(strs);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addLogcat(LogUtil.dl(TAG, "concatMap:" + s));
            }
        });
    }

    /**
     * 1、作用：将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
     * 2、无序的将被观察者发送的整个事件序列进行变换
     */
    private void flatMap() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("flatMap1");
                emitter.onNext("flatMap2");
                emitter.onNext("flatMap3");
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String string) throws Exception {
                List<String> strs = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    strs.add(string + i);
                }
                return Observable.fromIterable(strs);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addLogcat(LogUtil.dl(TAG, "flatMap:" + s));
            }
        });
    }

    /**
     * 1、将被观察者发送的事件转换为任意的类型事件
     * 2、应用场景：数据类型转换
     */
    private void map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
            // 2. 使用Map变换操作符中的Function函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer i) throws Exception {
                return "操作符，将Observable传过来的int类型数据" + i + "转换成字符串";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addLogcat(LogUtil.dl(TAG, "map:" + s));
            }
        });
    }
}
