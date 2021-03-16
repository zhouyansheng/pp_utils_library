package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;


/**
 * 获取汽配api参数的工具类
 * Created by jiajie on 2017/11/18.
 */

public class ParamUtil {

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
        return imei != null ? imei : "123456789";
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getDeviceId() {
        return DeviceUuidFactory.getDeviceUuid().toString();
    }

    public static String getHash(String... values) {
        String securityCode = "8cUoeQ31Q3C+5DQEDkYD55RA++GP0EwJgE95gaxbZlo=";
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        sb.append(securityCode);
        return MD5.getStringMD5(sb.toString());
    }

    public static String getTime() {
        //精确到秒
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

}
