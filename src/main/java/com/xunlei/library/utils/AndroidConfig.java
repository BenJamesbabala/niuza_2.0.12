package com.xunlei.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.xunlei.library.BaseApplication;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
//import mtopsdk.common.util.SymbolExpUtil;

public class AndroidConfig {
    private static final String DEFAULT_IMEI = "000000000000000";
    private static final String DEFAULT_PEER_ID = "0000000000000000004V";
    private static String mIMEI = null;
    private static String mMac = null;
    private static String mPeerId = null;

    public static String getPeerid(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService("wifi");
        if (!(wm == null || wm.getConnectionInfo() == null)) {
            mPeerId = (wm.getConnectionInfo().getMacAddress() + "004V").replaceAll(":", "");
            mPeerId = mPeerId.replaceAll(";", "");
            mPeerId = mPeerId.replaceAll("[.]", "");
            mPeerId = mPeerId.toUpperCase();
        }
        if (TextUtils.isEmpty(mPeerId)) {
            return DEFAULT_PEER_ID;
        }
        return mPeerId;
    }

    public static String getMAC() {
        WifiManager wm = (WifiManager) BaseApplication.getInstance().getSystemService("wifi");
        if (!(wm == null || wm.getConnectionInfo() == null)) {
            mMac = wm.getConnectionInfo().getMacAddress();
            if (!TextUtils.isEmpty(mMac)) {
                mMac = mMac.toUpperCase();
            }
        }
        return mMac;
    }

    public static String getIMEI(Context context) {
        String imei = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm != null) {
            imei = tm.getDeviceId();
        }
        if (TextUtils.isEmpty(imei)) {
            return DEFAULT_IMEI;
        }
        return imei;
    }

    public static int getAndroidVersion() {
        return VERSION.SDK_INT;
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    public static boolean isInstalledApk(String packageName, int versionCode) {
        try {
            PackageInfo packageInfo = BaseApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo == null) {
                return false;
            }
            if (packageInfo.versionCode == versionCode || versionCode == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSDKSupport(int apiLevel) {
        return VERSION.SDK_INT >= apiLevel;
    }

    public static String getVersion(Context context) {
        String version = null;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (Exception e) {
            XLLog.d("AndroidConfig", "getVersion error " + e.getMessage());
        }
        return version;
    }

    public static String getVersionName(Context context) {
        String version = null;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            XLLog.d("AndroidConfig", "getVersionName error " + e.getMessage());
            return version;
        }
    }

    public static String getVersionName() {
        return getVersionName(BaseApplication.getInstance());
    }

    public static int getVersionCode() {
        int version = 1;
        try {
            return BaseApplication.getInstance().getPackageManager().getPackageInfo(BaseApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            XLLog.d("AndroidConfig", "getVersion error " + e.getMessage());
            return version;
        }
    }

    public static String getDeviceInfo() {
        String device = getPhoneBrand() + "|" + getPhoneModel();
        try {
            device = URLEncoder.encode(device, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return device;
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCountry(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }
}
