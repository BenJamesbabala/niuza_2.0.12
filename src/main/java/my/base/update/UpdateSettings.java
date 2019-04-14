package my.base.update;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import my.base.util.ApplicationUtils;

public class UpdateSettings {
    public static final String DOWNLOAD_URL = "dlurl";
    public static final String LASTUPDATETIME = "lastUpdateTime";
    public static final String NOMOREPLAY = "noMorePlay";
    public static final String UPDATECYCLE = "updateCycle";
    public static final String UPDATE_SETTING = "updateSetting";
    public static final String USE_TIMES = "use_times";
    public static final String VERSIONCODE = "versionCode";
    public static String updateUrl = "http://yiplayer.com/update/100";
    public static int wap3_cid = 0;

    public static void setUpdateUrl(String updateUrl) {
        updateUrl = updateUrl;
    }

    public static void setWap3Cid(int wap3CId) {
        wap3_cid = wap3CId;
    }

    public static void init(Context context, String updateUrl, int wap3CId) {
        if (updateUrl != null) {
            updateUrl = updateUrl;
        }
        wap3_cid = wap3CId;
        context.getSharedPreferences(UPDATE_SETTING, 0).edit().putLong(USE_TIMES, getUsedTimes(context) + 1);
    }

    public static long getUsedTimes(Context context) {
        return context.getSharedPreferences(UPDATE_SETTING, 0).getLong(USE_TIMES, 0);
    }

    public static void checkUpdate(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(UPDATE_SETTING, 0);
        if (prefs.getBoolean(NOMOREPLAY, false)) {
            if (ApplicationUtils.getVerCode(activity, activity.getPackageName()) < prefs.getInt(VERSIONCODE, 0)) {
                String dlurl = prefs.getString(DOWNLOAD_URL, updateUrl);
                GetUpdateInfoThread updateThread = new GetUpdateInfoThread(activity, true);
                updateThread.upHandler.sendMessage(updateThread.upHandler.obtainMessage(202, dlurl));
                return;
            }
            Editor editor = prefs.edit();
            editor.putBoolean(NOMOREPLAY, false);
            editor.commit();
            return;
        }
        if (((long) (((prefs.getInt(UPDATECYCLE, 48) * 60) * 60) * 1000)) + prefs.getLong(LASTUPDATETIME, 0) < System.currentTimeMillis()) {
            new GetUpdateInfoThread(activity, true).start();
        }
    }

    public static boolean isFirstTime(Activity activity) {
        SharedPreferences prefs = activity.getPreferences(0);
        int versionCode = prefs.getInt(VERSIONCODE, 0);
        int curVersionCode = ApplicationUtils.getVerCode(activity, activity.getPackageName());
        Editor editor = prefs.edit();
        editor.putInt(VERSIONCODE, curVersionCode);
        editor.commit();
        if (versionCode < curVersionCode) {
            return true;
        }
        return false;
    }
}
