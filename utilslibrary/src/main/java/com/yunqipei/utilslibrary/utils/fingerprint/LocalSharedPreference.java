package com.yunqipei.utilslibrary.utils.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.yunqipei.utilslibrary.utils.PreferencesUtil;

public class LocalSharedPreference {
    //登陆
    private static final String KEY_LOGIN_PASSWORD = "login_password";
    //提现
    private static final String KEY_WITHDRAW_PASSWORD = "withdraw_password";
    //iv
    private static final String KEY_IV = "Key_iv";
    private static LocalSharedPreference sInstance;
    private SharedPreferences preferences;

    private LocalSharedPreference(Context context) {
        preferences = context.getSharedPreferences("fingerprint_local", Activity.MODE_PRIVATE);
    }

    public static LocalSharedPreference getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LocalSharedPreference.class) {
                if (sInstance == null) {
                    sInstance = new LocalSharedPreference(context);
                }
            }
        }
        return sInstance;
    }

    public String getKeyIv() {
        return getString(KEY_IV);
    }
//    public String getTemp() {
//        return getString("temp");
//    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */
    private String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean saveKayIv(String iv) {
        return saveString(KEY_IV, iv);
    }
//    public boolean saveTemp(String temp) {
//        return saveString("temp", temp);
//    }

    /**
     * 保存对应key的值
     *
     * @param key
     * @param value
     * @return
     */
    private boolean saveString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 清除所有
     * @return
     */
    public boolean clearAllString() {
        return preferences.edit()
                .clear()
                .commit();
    }

    //获取iv 密码类型+手机号
    public String getKeyIv(String password, String phone) {
        return getString(KEY_IV + password + phone);
    }

    public boolean saveKayIv(String password, String iv) {
        return saveString(KEY_IV + password + PreferencesUtil.getPhone(), iv);
    }

    //登录状态时登录密码
    public String getLoginPassword() {
        return getLoginPassword(PreferencesUtil.getPhone());
    }

    //登陆密码
    public String getLoginPassword(String phone) {
        return getString(KEY_LOGIN_PASSWORD + phone);
    }

    //设置的时候已经登录了
    public boolean saveLoginPassword(String loginPassword) {
        return saveLoginPassword(PreferencesUtil.getPhone(), loginPassword);
    }

    public boolean saveLoginPassword(String phone, String loginPassword) {
        return saveString(KEY_LOGIN_PASSWORD + phone, loginPassword);
    }

    //支付密码
    public String getWithdrawPassword() {
        return getWithdrawPassword(PreferencesUtil.getPhone());
    }

    public String getWithdrawPassword(String phone) {
        return getString(KEY_WITHDRAW_PASSWORD + phone);
    }

    //支付密码肯定已经登录了
    public boolean saveWithdrawPassword(String withdrawPassword) {
        return saveWithdrawPassword(PreferencesUtil.getPhone(), withdrawPassword);
    }

    public boolean saveWithdrawPassword(String phone, String withdrawPassword) {
        return saveString(KEY_WITHDRAW_PASSWORD + phone, withdrawPassword);
    }
}
