package com.niuza.android.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.xunlei.library.utils.AndroidConfig;
//import com.xunlei.library.utils.XLLog;
import java.io.File;

public abstract class DeviceHelper {
    private static String macAddr = null;

    public static String getRomName() {
        StringBuilder romStrBuilder = new StringBuilder();
        romStrBuilder.append(AndroidConfig.getPhoneBrand()).append("_");
        romStrBuilder.append(AndroidConfig.getPhoneModel()).append("_");
        romStrBuilder.append(AndroidConfig.getAndroidVersion());
        return romStrBuilder.toString();
    }

    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    public static int getScreenWidth(Context c) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) c.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context c) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) c.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static long getAvailableExternalMemorySize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        Environment.getExternalStorageState();
        StatFs stat = new StatFs(path.getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static String getSDCardDir() {
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
        if (sdcardPath == null || sdcardPath.endsWith("/")) {
            return sdcardPath;
        }
        return sdcardPath + "/";
    }

    public static boolean isSDCardExist() {
        return "mounted".equalsIgnoreCase(Environment.getExternalStorageState());
    }

    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
//        XLLog.i("TimeAlbum", "isInMainThread myLooper=" + myLooper + ";mainLooper=" + mainLooper);
        return myLooper == mainLooper;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static String generateDeviceNameById(String deivceId) {
        String deivceName = "下载宝_";
        if (deivceId.length() > 18) {
            return deivceName + deivceId.substring(14, 18);
        }
        return "下载宝_未知";
    }
}
