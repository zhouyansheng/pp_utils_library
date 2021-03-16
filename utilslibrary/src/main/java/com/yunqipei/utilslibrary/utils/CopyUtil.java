package com.yunqipei.utilslibrary.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

/**
 * edie create on 2019/7/9
 */
public class CopyUtil {

    public static void copyText(CharSequence text,Context context) {
        copyText("llq", text,context);
    }

    public static void copyText(String label, CharSequence text,Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        assert cm != null;
        cm.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    public static boolean isAppCopy(ClipboardManager cm) {
        ClipData cdText = cm.getPrimaryClip();
        assert cdText != null;
        ClipDescription description = cdText.getDescription();
        CharSequence label = description.getLabel();
        boolean flag = TextUtils.equals(label, "llq");
        return flag;
    }


    /**
     * Paste clipboard content between min and max positions.
     */
    public static void paste(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            for (int i = 0; i < clip.getItemCount(); i++) {

                final CharSequence text = clip.getItemAt(i).coerceToText(context);
                final CharSequence paste = (text instanceof Spanned) ? text.toString() : text;

            }
        }
    }


    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
