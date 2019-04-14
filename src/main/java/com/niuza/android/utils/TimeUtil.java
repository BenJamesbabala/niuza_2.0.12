package com.niuza.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
    public static String getTimeString(long timeInSecond) {
        long now = Calendar.getInstance().getTimeInMillis() / 1000;
        if (now - timeInSecond < 86400) {
            long t = now - timeInSecond;
            if (t < 0) {
                t = 0;
            }
            long min = (t / 60) % 60;
            if (t / 3600 != 0) {
                return String.format("%d小时%d分钟前", new Object[]{Long.valueOf(t / 3600), Long.valueOf(min)});
            } else if (min == 0) {
                return "刚刚";
            } else {
                return String.format("%d分钟前", new Object[]{Long.valueOf(min)});
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1000 * timeInSecond);
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        df.setLenient(false);
        return df.format(cal.getTime());
    }
}
