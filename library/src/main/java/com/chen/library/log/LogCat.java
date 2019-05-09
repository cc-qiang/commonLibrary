package com.chen.library.log;

import android.util.Log;

import com.chen.library.BuildConfig;


public class LogCat {
    private static final String TAG = "cc-qiang";

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "*****>> " + msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "*****>> " + msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "*****>> " + msg);
        }
    }
}
