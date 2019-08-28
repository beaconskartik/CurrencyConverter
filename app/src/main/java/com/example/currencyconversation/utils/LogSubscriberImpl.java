package com.example.currencyconversation.utils;


import android.util.Log;

import io.reactivex.CompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class LogSubscriberImpl<T> extends DisposableObserver<T> implements CompletableObserver {

    private final String prefix;
    private final boolean printOnNextLog;
    private final String tag;

    public LogSubscriberImpl(String tag, String prefix, boolean printOnNextLog) {
        this.tag = tag;
        this.prefix = prefix;
        this.printOnNextLog = printOnNextLog;
    }

    @Override
    public void onError(Throwable e) {
        Log.w(tag, prefix + " onError: " + e.getMessage(), e);
    }

    @Override
    public void onNext(T obj) {
        if (printOnNextLog) {
            Log.i(tag, prefix + " onNext: " + objectToString(obj));
        }
    }

    private String objectToString(T obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "null";
        }
    }

    @Override
    public void onComplete() {
        Log.i(tag, prefix + " onComplete");
    }
}
