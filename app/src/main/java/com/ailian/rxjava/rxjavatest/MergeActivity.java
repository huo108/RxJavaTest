package com.ailian.rxjava.rxjavatest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ailian.rxjava.rxjavatest.databinding.ActivityMergeBinding;
import com.ailian.rxjava.rxjavatest.util.LogUtil;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MergeActivity extends BaseActivity {
    private static final String TAG = "MergeActivity";
    private ActivityMergeBinding binding;
    private Disposable disposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_merge);
        binding.setClk(new MergeActivity.Click());
        recyclerView = binding.include.logcatRecycler;
        setAdapter();
    }

    public class Click {
        public void clickConcat() {
            concat();
        }

        public void clickConcatArray() {
            concatArray();
        }

        public void clickMerge() {
            merge();
        }

        public void clickMergeArray() {
            mergeArray();
        }

        public void clickConcatDelayError() {
            concatDelayError();
        }

        public void clickMergeDelayError() {
            mergeDelayError();
        }

        public void clickZip() {
            zip();
        }

        public void clickCombineLatest() {
            combineLatest();
        }

        public void clickCombineLatestDelayError() {
            combineLatestDelayError();
        }

        public void clickReduce() {
            reduce();
        }

        public void clickCollect() {
            collect();
        }

        public void clickStartWith() {
            startWith();
        }

        public void clickStartWithArray() {
            startWithArray();
        }

        public void clickCount() {
            count();
        }
    }

    /**
     * 作用:统计被观察者发送事件的数量
     */
    private void count() {
        Observable.just(1,2,3).count().subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                addLogcat(LogUtil.dl(TAG, "count-总的发送事件数：" + aLong));
            }
        });
    }

    /**
     * 1、作用：在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
     * 2、后加的数据先发送
     */
    private void startWithArray() {
        startWith();
    }

    /**
     * 1、作用：在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
     * 2、后加的数据先发送
     */
    private void startWith() {
        Observable.just(1,2,3).startWith(4).startWithArray(5,6,7,8).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "startWith-后加的数据先发送：" + integer));
            }
        });
    }

    /**
     * 作用:将被观察者Observable发送的数据事件收集到一个数据结构里
     */
    private void collect() {
        Observable.just(1,2,3,4,5,6).collect(new Callable<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() throws Exception {
                return new ArrayList<>();
            }
        }, new BiConsumer<ArrayList<Integer>, Integer>() {
            @Override
            public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                integers.add(integer);
            }
        }).subscribe(new Consumer<ArrayList<Integer>>() {
            @Override
            public void accept(ArrayList<Integer> integers) throws Exception {
                addLogcat(LogUtil.dl(TAG, "collect-accept组合后的数据是" + integers));
            }
        });
    }

    /**
     * 1、把被观察者需要发送的事件聚合成1个事件 & 发送
     * 2、聚合的逻辑根据需求撰写，但本质都是前2个数据聚合，然后与后1个数据继续进行聚合，依次类推
     */
    private void reduce() {
        Observable.just(1, 3, 5, 7).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                addLogcat(LogUtil.dl(TAG, "reduce:合并的数据是" + integer + "*" + integer2));
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "reduce-accept最终的合并的数据是" + integer));
            }
        });
    }

    /**
     * 作用类似于concatDelayError（） / mergeDelayError（） ，即错误处理
     * omError事件推迟到其他被观察者发送完所有事件后执行
     */
    private void combineLatestDelayError() {
        Observable.combineLatest(
                Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                        emitter.onNext(1L);
                        emitter.onNext(2L);
                        emitter.onError(new NullPointerException());
                        emitter.onNext(3L);
                    }
                }),
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 第2个发送数据事件的Observable：从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long o1, Long o2) throws Exception {
                        // o1 = 第1个Observable发送的最新（最后）1个数据
                        // o2 = 第2个Observable发送的每1个数据
                        addLogcat(LogUtil.dl(TAG, "combineLatestDelayError:合并的数据是" + o1 + "和" + o2));
                        return o1 + o2;
                        // 合并的逻辑 = 相加
                        // 即第1个Observable发送的最后1个数据 与 第2个Observable发送的每1个数据进行相加
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG, "combineLatestDelayError:onSubscribe"));
            }

            @Override
            public void onNext(Long aLong) {
                addLogcat(LogUtil.dl(TAG, "combineLatestDelayError:onNext" + aLong));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "combineLatestDelayError:onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "combineLatestDelayError:onComplete"));
            }
        });
    }

    /**
     * 作用:当两个Observables中的任何一个发送了数据后，将先发送了数据的Observables 的最新（最后）一个数据 与 另外一个Observable发送的每个数据结合，最终基于该函数的结果发送数据
     */
    private void combineLatest() {
        Observable.combineLatest(
                Observable.just(1L, 2L, 3L), // 第1个发送数据事件的Observable
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 第2个发送数据事件的Observable：从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long o1, Long o2) throws Exception {
                        // o1 = 第1个Observable发送的最新（最后）1个数据
                        // o2 = 第2个Observable发送的每1个数据
                        LogUtil.d(TAG, "combineLatest:合并的数据是" + o1 + " " + o2);
                        addLogcat(LogUtil.dl(TAG, "combineLatest:合并的数据是" + o1 + "和" + o2));
                        return o1 + o2;
                        // 合并的逻辑 = 相加
                        // 即第1个Observable发送的最后1个数据 与 第2个Observable发送的每1个数据进行相加
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long s) throws Exception {
                LogUtil.d(TAG, "combineLatest:accept" + s);
                addLogcat(LogUtil.dl(TAG, "combineLatest:accept" + s));
            }
        });
    }

    /**
     * 1、合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
     * 2、事件组合方式 = 严格按照原先事件序列 进行对位合并
     * 3、最终合并的事件数量 = 多个被观察者（Observable）中数量最少的数量
     */
    private void zip() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("事件A");
                addLogcat(LogUtil.dl(TAG, "发送了事件A"));
                Thread.sleep(1000);
                emitter.onNext("事件B");
                addLogcat(LogUtil.dl(TAG, "发送了事件B"));
                Thread.sleep(1000);
                emitter.onNext("事件C");
                addLogcat(LogUtil.dl(TAG, "发送了事件C"));
                Thread.sleep(1000);
                emitter.onComplete();
            }
        }).observeOn(Schedulers.io());

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                addLogcat(LogUtil.dl(TAG, "发送了事件1"));
                Thread.sleep(1000);
                emitter.onNext(2);
                addLogcat(LogUtil.dl(TAG, "发送了事件2"));
                Thread.sleep(1000);
                emitter.onNext(3);
                addLogcat(LogUtil.dl(TAG, "发送了事件3"));
                Thread.sleep(1000);
                emitter.onNext(4);
                addLogcat(LogUtil.dl(TAG, "发送了事件3"));
                Thread.sleep(1000);
                emitter.onComplete();
            }
        }).observeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer integer) throws Exception {
                return s + integer;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                addLogcat(LogUtil.dl(TAG, "zip:onSubscribe"));
            }

            @Override
            public void onNext(String s) {
                addLogcat(LogUtil.dl(TAG, "zip:onNext:" + s));
            }

            @Override
            public void onError(Throwable e) {
                addLogcat(LogUtil.dl(TAG, "zip:onError"));
            }

            @Override
            public void onComplete() {
                addLogcat(LogUtil.dl(TAG, "zip:onComplete"));
            }
        });

    }

    /**
     * 不使用mergeDelayError带来的问题：如果一个被观察者发出onError事件，则其他被观察者立即终止发送事件；
     * 解决办法：若希望omError事件推迟到其他被观察者发送完所有事件后执行，则需要使用concatDelayError/mergeDelayError
     */
    private void mergeDelayError() {
        Observable.mergeDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException()); // 发送Error事件，因为无使用concatDelayError，所以第2个Observable将不会发送事件
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addLogcat(LogUtil.dl(TAG, "mergeDelayError:onSubscribe"));
                    }

                    @Override
                    public void onNext(Integer value) {
                        addLogcat(LogUtil.dl(TAG, "mergeDelayError:onNext：" + value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        addLogcat(LogUtil.dl(TAG, "mergeDelayError:onError"));
                    }

                    @Override
                    public void onComplete() {
                        addLogcat(LogUtil.dl(TAG, "mergeDelayError:onComplete"));
                    }
                });
    }

    /**
     * 不使用concatDelayError带来的问题：如果一个被观察者发出onError事件，则其他被观察者立即终止发送事件；
     * 解决办法：若希望omError事件推迟到其他被观察者发送完所有事件后执行，则需要使用concatDelayError/mergeDelayError
     */
    private void concatDelayError() {
        Observable.concatArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException()); // 发送Error事件，因为无使用concatDelayError，所以第2个Observable将不会发送事件
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addLogcat(LogUtil.dl(TAG, "concatDelayError:onSubscribe"));
                    }

                    @Override
                    public void onNext(Integer value) {
                        addLogcat(LogUtil.dl(TAG, "concatDelayError:onNext：" + value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        addLogcat(LogUtil.dl(TAG, "concatDelayError:onError"));
                    }

                    @Override
                    public void onComplete() {
                        addLogcat(LogUtil.dl(TAG, "concatDelayError:onComplete"));
                    }
                });
    }

    /**
     * 1、作用：组合多个被观察者一起发送数据，合并后 按时间线并行执行
     * 2、数量不限制
     */
    private void mergeArray() {
        Observable.mergeArray(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12),
                Observable.just(13, 14, 15),
                Observable.just(16, 17, 18)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "merge:" + integer));
            }
        });
    }

    /**
     * 1、作用：组合多个被观察者一起发送数据，合并后 按时间线并行执行
     * 2、数量≤4个
     */
    private void merge() {
        Observable.merge(Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS))// 从2开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Exception {
                        addLogcat(LogUtil.dl(TAG, "merge:" + integer));
                    }
                });
    }

    /**
     * 1、组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     * 2、数量不限制
     */
    private void concatArray() {
        Observable.concatArray(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12),
                Observable.just(13, 14, 15),
                Observable.just(16, 17, 18)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "concatArray:" + integer));
            }
        });
    }

    /**
     * 1、组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     * 2、数量≤4个
     */
    private void concat() {
        Observable.concat(Observable.just(1, 2, 3),
                Observable.just(4, 5, 6),
                Observable.just(7, 8, 9),
                Observable.just(10, 11, 12)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                addLogcat(LogUtil.dl(TAG, "concat:" + integer));
            }
        });
    }

}
