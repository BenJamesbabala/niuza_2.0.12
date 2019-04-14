package my.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import my.base.model.AppInfo;

public class ApplicationUtils {
    public static int getVerCode(Context context, String packageName) {
        int verCode = -1;
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return verCode;
        }
    }

    public static String getVerName(Context context, String packageName) {
        String verName = "";
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return verName;
        }
    }

    public static String getAppName(Context context, ApplicationInfo info) {
        return context.getPackageManager().getApplicationLabel(info).toString();
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        return context.getPackageManager().getApplicationIcon(context.getApplicationInfo());
    }

    public static boolean isPackageAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (packageName.equals(((PackageInfo) pinfo.get(i)).packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isIntentAvailable(Context context, String action) {
        return context.getPackageManager().queryIntentActivities(new Intent(action), 65536).size() > 0;
    }

    public static AppInfo getApkFileInfo(Context ctx, String apkPath) {
        File apkFile = new File(apkPath);
        if (!apkFile.exists() || !apkPath.toLowerCase().endsWith(".apk")) {
            return null;
        }
        String PATH_AssetManager = "android.content.res.AssetManager";
        try {
            Class<?> pkgParserCls = Class.forName("android.content.pm.PackageParser");
            Object pkgParser = pkgParserCls.getConstructor(new Class[]{String.class}).newInstance(new Object[]{apkPath});
            new DisplayMetrics().setToDefaults();
//            Object pkgParserPkg = pkgParserCls.getDeclaredMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(pkgParser, new Object[]{new File(apkPath), apkPath, metrics, Integer.valueOf(0)});
            Object pkgParserPkg = pkgParserCls.getDeclaredMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(pkgParser, new Object[]{new File(apkPath), apkPath, null, Integer.valueOf(0)});
            if (pkgParserPkg == null) {
                return null;
            }
            Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
            if (appInfoFld.get(pkgParserPkg) == null) {
                return null;
            }
            ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);
            Class<?> assetMagCls = Class.forName(PATH_AssetManager);
            Object assetMag = assetMagCls.newInstance();
            assetMagCls.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(assetMag, new Object[]{apkPath});
            Resources res = ctx.getResources();
            res = (Resources) Resources.class.getConstructor(new Class[]{assetMag.getClass(), res.getDisplayMetrics().getClass(), res.getConfiguration().getClass()}).newInstance(new Object[]{assetMag, res.getDisplayMetrics(), res.getConfiguration()});
            AppInfo appInfo = new AppInfo();
            if (info == null) {
                return null;
            }
            if (info.icon != 0) {
                appInfo.setIcon(res.getDrawable(info.icon));
            }
            if (info.labelRes != 0) {
                appInfo.setAppName((String) res.getText(info.labelRes));
            } else {
                appInfo.setAppName(getAppName(ctx, info));
            }
            appInfo.setPackageName(info.packageName);
            appInfo.setAppFile(apkFile);
            appInfo.setFileName(apkFile.getName());
            appInfo.setSize(apkFile.length());
            appInfo.setInstallPath(apkPath);
            PackageInfo packageInfo = ctx.getPackageManager().getPackageArchiveInfo(apkPath, 1);
            if (packageInfo == null) {
                return appInfo;
            }
            appInfo.setVersionName(packageInfo.versionName);
            appInfo.setVersionCode(packageInfo.versionCode);
            return appInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
