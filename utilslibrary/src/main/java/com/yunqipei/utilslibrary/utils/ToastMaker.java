package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {

    private static final String TAG = "ToastMaker";
    private static Toast toast;
    private static Context mContext;

    private ToastMaker() {
    }

    public static void init(Context context) {
        mContext = context;
    }

    public static void show(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    public static void show(int resId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        toast.setText(resId);
        toast.setDuration(duration);
        toast.show();
    }

}