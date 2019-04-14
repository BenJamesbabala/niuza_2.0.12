package com.xunlei.library.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Process;
import java.util.List;

public class ProcessUtils {
    static List<RunningAppProcessInfo> apps = null;
    static int mgprocessId = 0;

    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static boolean isExistProcessName(Context context, String name) {
        int pid = Process.myPid();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.processName.equals(name) && appProcess.pid != pid) {
                return true;
            }
        }
        return false;
    }

    public static String getMyProcessName(Context context) {
        return context.getPackageName();
    }

    public static String getTopProcessNameByBaseActivity(Context context) {
        List<RunningTaskInfo> taskList = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (taskList == null || taskList.size() <= 0) {
            return null;
        }
        return ((RunningTaskInfo) taskList.get(0)).baseActivity.getPackageName();
    }

    public static boolean isMyProcessOnTopByBaseActivity(Context context) {
        String myProcessName = getMyProcessName(context);
        String topProcessName = getTopProcessNameByBaseActivity(context);
        if (topProcessName != null) {
            return myProcessName.equals(topProcessName);
        }
        return false;
    }

    public static String getTopProcessNameByTopActivity(Context context) {
        List<RunningTaskInfo> taskList = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (taskList == null || taskList.size() <= 0) {
            return null;
        }
        return ((RunningTaskInfo) taskList.get(0)).topActivity.getPackageName();
    }

    public static boolean isMyProcessOnTopByTopActivity(Context context) {
        return getMyProcessName(context).equals(getTopProcessNameByTopActivity(context));
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

    public static int getProcessId(Context con) {
        if (mgprocessId == 0) {
            apps = ((ActivityManager) con.getSystemService("activity")).getRunningAppProcesses();
            for (RunningAppProcessInfo p : apps) {
                if (p.processName.equals("com.xunlei.kankan")) {
                    mgprocessId = p.pid;
                    break;
                }
            }
        }
        return mgprocessId;
    }
}
