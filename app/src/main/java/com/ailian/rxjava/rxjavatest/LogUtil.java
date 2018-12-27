package com.ailian.rxjava.rxjavatest;

import android.os.SystemClock;
import android.util.Log;

public class LogUtil {

    private final static String DEFAULT_TAG = "RxJavaTest_";
    private static boolean isDebug = true;
    private static long mLogtime = 0;

    public void LogOn(boolean val) {
        isDebug = val;
    }

    public static void i(String log) {
        if (isDebug)
            Log.i(DEFAULT_TAG, log);
    }

    public static void d(String log) {
        if (isDebug)
            Log.d(DEFAULT_TAG, log);
    }

    public static LogcatBean dl(String tag,String log) {
        if (isDebug)
            Log.d(DEFAULT_TAG, log);
        return new LogcatBean(tag,log);
    }

    public static void e(String log) {
        if (isDebug)
            Log.e(DEFAULT_TAG, log);
    }

    public static void i(String tag, String log) {
        if (isDebug)
            Log.i(DEFAULT_TAG + tag, log);
    }

    public static void d(String tag, String log) {
        if (isDebug)
            Log.d(DEFAULT_TAG + tag, log);
    }

    public static void w(String tag, String log) {
        if (isDebug) {
            Log.w(DEFAULT_TAG + tag, log);
        }
    }

    public static void w(String tag, String log, Throwable tr) {
        if (isDebug)
            Log.w(DEFAULT_TAG + tag, log, tr);
    }

    public static void e(String tag, String log) {
        if (isDebug)
            Log.e(DEFAULT_TAG + tag, log);
    }

    public static void e(String tag, String log, Throwable tr) {
        if (isDebug)
            Log.e(DEFAULT_TAG + tag, log, tr);
    }

    public static void resetTime() {
        mLogtime = SystemClock.elapsedRealtime();
    }

    private static String addTimeMsg(String msg) {
        return msg + " T:" + (SystemClock.elapsedRealtime() - mLogtime);
    }

    public static void time_i(String tag, String msg) {
        if (isDebug)
            Log.i(DEFAULT_TAG + tag, addTimeMsg(msg));
    }

    public static void time_d(String tag, String msg) {
        if (isDebug)
            Log.d(DEFAULT_TAG + tag, addTimeMsg(msg));
    }

    public static void time_w(String tag, String msg) {
        if (isDebug)
            Log.w(DEFAULT_TAG + tag, addTimeMsg(msg));
    }

    public static void time_v(String tag, String msg) {
        if (isDebug)
            Log.v(DEFAULT_TAG + tag, addTimeMsg(msg));
    }

    public static void time_e(String tag, String msg) {
        if (isDebug)
            Log.e(DEFAULT_TAG + tag, addTimeMsg(msg));
    }
}
