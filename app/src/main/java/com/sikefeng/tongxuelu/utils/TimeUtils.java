package com.sikefeng.tongxuelu.utils;

import java.text.SimpleDateFormat;

/**
 * Created by sikefeng on 2016/8/14.
 */
public class TimeUtils {


    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        String time = sdf.format(System.currentTimeMillis());
        return time;
    }
    public static String getCurrentTimeCN() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日 hh:mm");
        String time = sdf.format(System.currentTimeMillis());
        return time;
    }
    public static String getCurrentTimeToFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
        String time = sdf.format(System.currentTimeMillis());
        return time;
    }
}
