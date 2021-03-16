package com.yunqipei.utilslibrary.utils;

import java.util.HashSet;

/**
 * edie create on 2018/10/16
 */
public class DownloadManager {
    public static DownloadManager sInstance;
    private HashSet<String> mDownloadUrl;

    public DownloadManager() {
        mDownloadUrl = new HashSet<>();
    }

    public static DownloadManager getInstance() {
        if (sInstance == null) {
            synchronized (DownloadManager.class) {
                sInstance = new DownloadManager();
            }
        }
        return sInstance;
    }

    public boolean addUrl(String url) {
        return mDownloadUrl.add(url);
    }

    public boolean removeUrl(String url) {
        return mDownloadUrl.remove(url);
    }

    public boolean containsUrl(String url) {
        return mDownloadUrl.contains(url);
    }
}
