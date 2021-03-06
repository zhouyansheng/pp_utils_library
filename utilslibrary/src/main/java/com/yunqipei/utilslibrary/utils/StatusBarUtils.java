package com.yunqipei.utilslibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * StatusBarUtils
 * Created by jiajie on 2017/6/20.
 */

public class StatusBarUtils {

    /**
     * 使状态栏透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity      需要设置的activity
     * @param backgroundRes 状态栏颜色资源
     */
    public static void setBackground(Activity activity, int backgroundRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, backgroundRes);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 移除状态栏颜色view
     *
     * @param activity 需要设置的activity
     */
    public static void removeBackground(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 移除decorView的statusView
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (decorView.findViewById(0) != null) {
                decorView.removeView(decorView.findViewById(0));
            }
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity      需要设置的activity
     * @param backgroundRes 状态栏颜色资源
     *
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int backgroundRes) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusView.setId(0);
        statusView.setLayoutParams(params);
//        statusView.setBackgroundColor(color);
        statusView.setBackgroundResource(backgroundRes);
        return statusView;
    }

}