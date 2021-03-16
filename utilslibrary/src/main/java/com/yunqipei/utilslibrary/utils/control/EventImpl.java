package com.yunqipei.utilslibrary.utils.control;

import android.database.Observable;

/**
 * edie create on 2018/9/19
 */
public class EventImpl extends Observable<ViewObserver> {
    private static EventImpl sInstance;

    public static EventImpl getInstance() {
        if (sInstance == null) {
            synchronized (EventImpl.class) {
                if (sInstance == null) {
                    sInstance = new EventImpl();
                }
            }
        }
        return sInstance;
    }

//    public void notifyItemRangeChanged(String key) {
//        notifyItemRangeChanged(key, null);
//
//    }
//
//    public void notifyItemRangeChanged(String key, String... s) {
//        for (ViewObserver observer : mObservers) {
//            observer.onUpdate(key, s);
//        }
//
//    }

    public void notifyItemRangeChanged(String key, long bytesRead, long contentLength, boolean done) {
        for (ViewObserver observer : mObservers) {
            observer.onUpdate(key, bytesRead,contentLength,done);
        }

    }
}
