package my.base.util;

import android.app.Activity;
import android.net.Uri;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.WindowManager.LayoutParams;
import com.android.volley.DefaultRetryPolicy;

public class ScreenUtils {
    private static String SCREEN_BRIGHTNESS = "screen_brightness";
    private static String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    private static int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;
    private static int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;

    public static void setBrightness(Activity activity, float brightness) {
        LayoutParams lp = activity.getWindow().getAttributes();
        if (isAutoBrightness(activity)) {
            stopAutoBrightness(activity);
        }
        lp.screenBrightness += brightness / 255.0f;
        if (lp.screenBrightness > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
            lp.screenBrightness = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        } else if (((double) lp.screenBrightness) < 0.05d) {
            lp.screenBrightness = 0.05f;
        }
        activity.getWindow().setAttributes(lp);
    }

    public static boolean isAutoBrightness(Activity activity) {
        try {
            return System.getInt(activity.getContentResolver(), SCREEN_BRIGHTNESS_MODE) == SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        try {
            nowBrightnessValue = System.getInt(activity.getContentResolver(), SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    public static void stopAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public static void startAutoBrightness(Activity activity) {
        System.putInt(activity.getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    public static void saveBrightness(Activity activity, int brightness) {
        Uri uri = System.getUriFor(SCREEN_BRIGHTNESS);
        System.putInt(activity.getContentResolver(), SCREEN_BRIGHTNESS, brightness);
        activity.getContentResolver().notifyChange(uri, null);
    }

    public static void setFullScreen(Activity activity) {
        LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= 1024;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(512);
    }

    public static void cancelFullScreen(Activity activity) {
        LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= -1025;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(512);
    }

    public static void disableWindowTouch(Activity activity) {
        LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= 16;
        activity.getWindow().setAttributes(attrs);
    }

    public static void setWindowTouchable(Activity activity) {
        LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= -17;
        activity.getWindow().setAttributes(attrs);
    }
}
