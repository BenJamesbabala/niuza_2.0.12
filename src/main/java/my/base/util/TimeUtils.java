package my.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static long parseToSecond(String time) throws IllegalArgumentException {
        if (StringUtils.isBlank(time)) {
            return 0;
        }
        time = time.trim().toLowerCase();
        if (time.length() == 1) {
            try {
                return Long.parseLong(time);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }
        try {
            long numPrefix = Long.parseLong(time.substring(0, time.length() - 1));
            if (time.endsWith("s")) {
                return numPrefix;
            }
            if (time.endsWith("m")) {
                return numPrefix * 60;
            }
            if (time.endsWith("h")) {
                return numPrefix * 3600;
            }
            throw new IllegalArgumentException();
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException();
        }
    }

    public static long getIntervalInSecond(Date prevDate, Date nextDate) {
        if (prevDate != null && nextDate != null) {
            return (nextDate.getTime() - prevDate.getTime()) / 1000;
        }
        throw new IllegalArgumentException();
    }

    public static String formatDate(Date date, String formatString) {
        if (StringUtils.isBlank(formatString)) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(formatString).format(date);
    }
}
