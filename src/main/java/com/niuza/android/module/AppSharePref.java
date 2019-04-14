package com.niuza.android.module;

import android.content.SharedPreferences;
import com.niuza.android.NZApplication;

public class AppSharePref {
    private static final AppSharePref instance = new AppSharePref();
    private SharedPreferences appPref = NZApplication.getApp().getSharedPreferences("appInfo", 0);

    public static AppSharePref getInstance() {
        return instance;
    }

    private AppSharePref() {
    }

    public SharedPreferences getAppPref() {
        return this.appPref;
    }

    public void saveString(String name, String value) {
        this.appPref.edit().putString(name, value).commit();
    }

    public String getString(String name) {
        return this.appPref.getString(name, "");
    }
}
