package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.TypedValue;


/**
 * edie create on 2018/8/22
 */
public class UiUtil {



    public static Context getAppContext(Context context) {
        return context;
    }

    public static void startSetting(Context context){
        Uri packageURI = Uri.parse("package:"+ getAppContext(context).getPackageName());
        Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
        context. startActivity(intent);
    }

    public static int dp2px(float value,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }


}
