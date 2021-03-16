package com.yunqipei.utilslibrary.utils.control;

/**
 * edie create on 2018/9/19
 */
public interface ViewObserver {
//    void onUpdate(String key, String... s);

    void onUpdate(String key, long bytesRead, long contentLength, boolean done);
}
