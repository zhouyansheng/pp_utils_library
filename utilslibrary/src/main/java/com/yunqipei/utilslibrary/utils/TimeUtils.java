package com.yunqipei.utilslibrary.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jiajie on 2017/5/25.
 */

public class TimeUtils {

    private static final String FORMAT_NORMAL = "yyyy-MM-dd";
    private static Date sDate;

    /**
     * 时间转换：long to String
     *
     * @param time    整形时间,精确到秒
     * @param pattern 格式化后的格式， example("yyyy年MM月dd日","yyyy-MM-dd HH:mm")
     * @return yyyy年MM月dd日 格式
     */
    public static String getFormatTime(long time, String pattern) {
        time *= 1000;
        if (sDate == null)
            sDate = new Date();
        sDate.setTime(time);

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CANADA);
        return format.format(time);
    }

    /**
     * String 00:01:00 转化成long ：毫秒
     *
     * @param time 00:01:00
     * @return 60000
     */
    public static long getLongMills(String time) {
        String[] temp = time.split(":");
        if (temp.length == 0) {
            return 0;
        }
        try {
            int hour = Integer.parseInt(temp[0]);
            int min = Integer.parseInt(temp[1]);
            int second = Integer.parseInt(temp[2]);
            return hour * 60 * 60 * 1000 + min * 60 * 1000 + second * 1000;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getStringTime(long mills) {
        if (mills < 0) {
            return "";
        }
        String result;
        mills /= 1000;//秒
        int hour = (int) (mills / 3600);
        int min = (int) ((mills % 3600) / 60);
        int second = (int) (mills % 60);

        String hourStr = String.valueOf(hour);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }

        String mintStr = String.valueOf(min);
        if (min < 10) {
            mintStr = "0" + mintStr;
        }

        String secondStr = String.valueOf(second);
        if (second < 10) {
            secondStr = "0" + secondStr;
        }

        if (hour == 0) {
            result = mintStr + ":" + secondStr;
        } else {
            result = hourStr + ":" + mintStr + ":" + secondStr;
        }
        return result;
    }

    public static int getLeftDays(String end) {
        if (TextUtils.isEmpty(end)) {
            return 0;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date endDate = df.parse(end);
            Date now = new Date();
            long diffSecond = (endDate.getTime() - now.getTime()) / 1000;
            return (int) (diffSecond / (24 * 60 * 60));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 转换时间格式
     *
     * @param value       时间字符串
     * @param fromPattern value的格式
     * @param toPattern   要转换成的格式
     * @return toPattern 格式的字符串
     */
    public static String convertTimeString(String value, String fromPattern, String toPattern) {
        SimpleDateFormat from = new SimpleDateFormat(fromPattern, Locale.getDefault());
        SimpleDateFormat to = new SimpleDateFormat(toPattern, Locale.getDefault());

        try {
            Date fromDate = from.parse(value);
            return to.format(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String format(Date date) {
        return format(date, FORMAT_NORMAL);
    }

    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(long sdate) {
        Calendar pre = Calendar.getInstance();
        pre.setTimeInMillis(sdate);

        Calendar taday = Calendar.getInstance();

        if (pre.get(Calendar.YEAR) == (taday.get(Calendar.YEAR))) {
            int diffDay = pre.get(Calendar.DAY_OF_YEAR) - taday.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
}
