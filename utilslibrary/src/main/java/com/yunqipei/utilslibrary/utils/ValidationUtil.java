package com.yunqipei.utilslibrary.utils;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean checkEmail(String email) {
        String regex = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPhone(String phone) {
//        //暂时去掉手机号匹配
//        String regex = "[0-9]{11}$";
////        String regex = "^1[34578][0-9]{9}$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(phone);
//        return m.matches();
        return true;
    }

    public static boolean checkCode(String code) {
        return !TextUtils.isEmpty(code) && code.length() == 4;
    }

    public static boolean checkPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6 && password.length() <= 20;
    }

    public static boolean checkUsername(String username) {
        return !TextUtils.isEmpty(username) && username.length() >= 2 && username.length() <= 20;
    }

}
