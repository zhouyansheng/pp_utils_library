package com.yunqipei.utilslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class PreferencesUtil {

    private static final String TAG = "PreferencesUtil";
    // 用于解决 token 过期时，拦截器可能多次启动 StartupActivity
    private static final String KEY_FLAG_STARTUP_ACTIVITY_STARTED = "flag_startup_activity_started";
    private static final String KEY_USER_DATA = "user_data";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_REGISTRATION_ID = "registration_id";
    private static final String KEY_IMAGE_PULL = "image_pull";
    private static final String KEY_SWITCH_QUERY = "switch_query";

    private static SharedPreferences preferences;
    private static Gson gson = new Gson();

    private PreferencesUtil() {
    }

    // Application中初始化
    public static void init(Context context) {
        preferences = context.getSharedPreferences("AutoParts", Context.MODE_PRIVATE);
    }

    public static boolean isStartupActivityStarted() {
        return preferences.getBoolean(KEY_FLAG_STARTUP_ACTIVITY_STARTED, false);
    }

    public static void setStartupActivityStarted(boolean started) {
        preferences.edit()
                .putBoolean(KEY_FLAG_STARTUP_ACTIVITY_STARTED, started)
                .apply();
    }

    public static String getPhone() {
        return getString(KEY_PHONE);
    }

    public static String getString(String key) {
        return preferences.getString(key, "");
    }

    public static void savePhone(String phone) {
        saveString(KEY_PHONE, phone);
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }


    public static void saveRegistrationId(String registrationId) {
        saveString(KEY_REGISTRATION_ID, registrationId);
    }

    public static String getRegistrationId() {
        return getString(KEY_REGISTRATION_ID);
    }

    public static boolean getIsDevelop() {
        return preferences.getBoolean("is_develop", false);
    }

    public static void setDevelop(boolean isDevelop) {
        preferences.edit()
                .putBoolean("is_develop", isDevelop)
                .commit();
    }

    /**
     * @return true 过滤 false 不过滤
     */
    public static boolean getIsFilter() {
        return preferences.getBoolean("is_filter", false);
    }

    /**
     * @param isFilter true 过滤 false 不过滤
     */
    public static void setFilter(boolean isFilter) {
        preferences.edit()
                .putBoolean("is_filter", isFilter)
                .commit();
    }

    public static void setMaintainQuery(int isMaintainQuery) {
        preferences.edit()
                .putInt(KEY_SWITCH_QUERY, isMaintainQuery)
                .commit();
    }


    public static int getMaintainQuery() {
        return preferences.getInt(KEY_SWITCH_QUERY, 0);
    }
}