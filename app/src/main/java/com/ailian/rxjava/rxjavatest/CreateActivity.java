package com.ailian.rxjava.rxjavatest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ailian.rxjava.rxjavatest.databinding.ActivityCreateBinding;
import com.ailian.rxjava.rxjavatest.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * RxJava创建型操作符
 */
public class CreateActivity extends BaseActivity {
    private static final String TAG = "CreateActivity";
    private ActivityCreateBinding binding;
    private Disposable disposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setTitle("创建型操作符");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create);
        binding.setClk(new Click());
        recyclerView = binding.include.logcatRecycler;
        setAdapter();
    }

    public class Click {
        public void clickCreate() {
            create();
        }

        public void clickJust() {
            just();
        }

        public void clickFromArray() {
            formArray();
        }

        public void clickFromIterable() {
            fromIterable();
        }

        public void clickNever() {
            addLogcat(LogUtil.dl(TAG, "该方法创建的被观察者对象发送事件的特点：不发送任何事件"));
        }

        public void clickEmpty() {
            addLogcat(LogUtil.dl(TAG, "该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成"));
        }

        public void clickError() {
            addLogcat(LogUtil.dl(TAG, "该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常"));
        }

        public void clickFromDefer() {
            defer();
        }

        public void clickTimer() {
            timer();
        }

        public void clickInterval() {
            interval();
        }

        public void clickIntervalRange() {
            intervalRange();
        }

        public void clickRange() {
            range();
        }
        public void clickRangeLong() {
            rangeLong();
        }

        public void showLog() {
            // logcat();
        }
    }

    /**
     * 类似于range（），区别在于该方法支持数据类型 = Long
     */
    private void rangeLong() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        Observable.rangeLong(10, 10).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long l) throws Exception {
                addLogcat(LogUtil.dl(TAG, "onNext" + l));
            }
        });
    }

    /**
     * 1、连续发送 1个事件序列，可指定范围
     * 2、从0开始、无限递增1的的整数序列
     * 3、作用类似于intervalRange（），但区别在于：无延迟发送事件
     */
    private void range() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        Observable.range(1, 10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "onNext" + integer));
            }
        });
    }

    /**
     * 1、每隔指定时间 就发送 事件，可指定发送的数据的数量
     * 2、从0开始、无限递增1的的整数序列
     * 3、可指定发送的数据的数量
     */
    private void intervalRange() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 参数3 = 第1次事件延迟发送时间；
        // 参数4 = 间隔时间数字；
        // 参数5 = 时间单位
        Observable.intervalRange(3, 5, 1, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addLogcat(LogUtil.dl(TAG, "onSubscribe"));
                    }

                    @Override
                    public void onNext(Long aLong) {
                        addLogcat(LogUtil.dl(TAG, "onNext" + aLong));
                    }

                    @Override
                    public void onError(Throwable e) {
                        addLogcat(LogUtil.dl(TAG, "onError"));
                    }

                    @Override
                    public void onComplete() {
                        addLogcat(LogUtil.dl(TAG, "onComplete"));
                    }
                });
    }

    /**
     * 从0开始、无限递增1的的整数序列
     */
    private void interval() {
        // 参数说明：
        // 1、第1次延迟时间
        // 2、间隔时间数字；
        // 3、时间单位
        Observable.interval(3, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // 该例子发送的事件序列特点：延迟3s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        addLogcat(LogUtil.dl(TAG, "onSubscribe"));
                    }
                    // 默认最先调用复写的 onSubscribe（）

                    @Override
                    public void onNext(Long value) {
                        addLogcat(LogUtil.dl(TAG, "onNext" + value));
                        if (value == 10) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        addLogcat(LogUtil.dl(TAG, "onError"));
                    }

                    @Override
                    public void onComplete() {
                        addLogcat(LogUtil.dl(TAG, "onComplete"));
                    }
                });
    }

    /**
     * 延迟指定时间后，发送1个数值0（Long类型）
     */
    private void timer() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG, "onSubscribe"));
            }

            @Override
            public void onNext(Long aLong) {
                addLogcat(LogUtil.dl(TAG, "onNext" + aLong));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "onComplete"));
            }
        });
    }

    /**
     * 1、直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     * 2、每次订阅后，都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
     */
    private void defer() {
        final String str = "defer1";
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just(str);
            }
        });

        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addLogcat(LogUtil.dl(TAG, "onNext:" + s));
            }
        });

    }

    /**
     * 1、传入的集合List数据
     */
    private void fromIterable() {
        List<String> list = new ArrayList<String>();
        list.add("iterable1");
        list.add("iterable2");
        list.add("iterable3");
        Observable.fromIterable(list).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG, "onSubscribe"));
            }

            @Override
            public void onNext(String s) {
                addLogcat(LogUtil.dl(TAG, "onNext:" + s));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "onComplete"));
            }
        });
    }

    /**
     * 1、会将数组中的数据转换为Observable对象
     * 2、可发送10个以上事件（数组形式）
     * 3、Consumer<T> 自定义需要处理的回调方法，如该列中只处理onNext方法
     */
    private void formArray() {
        String[] strs = {"array1", "array2", "array3"};
        Observable.fromArray(strs).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                addLogcat(LogUtil.dl(TAG, "onNext:" + s));
            }
        });

    }

    /**
     * RxJava 中创建被观察者对象最基本的操作符
     */
    private void create() {
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
                addLogcat(LogUtil.dl(TAG, "onSubscribe"));
            }

            @Override
            public void onNext(String s) {
                addLogcat(LogUtil.dl(TAG, "onNext:" + s));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "onComplete"));
            }
        });
    }

    /**
     * 最多只能发送10个参数
     */
    private void just() {
        Observable.just("just1", "just2", "just3").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG, "onSubscribe"));
            }

            @Override
            public void onNext(String s) {
                addLogcat(LogUtil.dl(TAG, "onNext:" + s));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "onComplete"));
            }
        });
    }


}
