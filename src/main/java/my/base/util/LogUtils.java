package my.base.util;

import android.util.Log;

public class LogUtils {
    private static boolean isShowLog = false;
    private static int level = 2;

    public static void enableLogging(boolean enable) {
        isShowLog = enable;
    }

    public static void i(String tag, String msg) {
        if (isShowLog) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isShowLog) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isShowLog) {
            Log.w(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isShowLog) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isShowLog) {
            Log.v(tag, msg);
        }
    }

    public static void setLevel(int level) {
        level = level;
    }
}
