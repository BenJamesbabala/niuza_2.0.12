package com.xunlei.library;

import org.litepal.LitePalApplication;

public class BaseApplication extends LitePalApplication {
    private static BaseApplication instance = null;

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
