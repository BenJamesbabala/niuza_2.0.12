package com.xunlei.library.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.android.volley.DefaultRetryPolicy;
import com.xunlei.library.BaseApplication;

public class ScreenUtils {
    private static String SCREEN_BRIGHTNESS = "screen_brightness";
    private static String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    private static int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;
    private static int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;

    public static int dip2px(float dpValue) {
        return (int) ((dpValue * BaseApplication.getInstance().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) ((pxValue / BaseApplication.getInstance().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void hideIme(Activity context) {
        if (context != null) {
            View v = context.getWindow().peekDecorView();
            if (v != null && v.getWindowToken() != null) {
                ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

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

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            statusBarHeight = context.getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        XLLog.d("statusBarHeight", "statusBarHeight=" + statusBarHeight);
        return statusBarHeight;
    }
}
