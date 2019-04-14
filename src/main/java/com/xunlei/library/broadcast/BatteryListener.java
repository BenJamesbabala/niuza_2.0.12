package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class BatteryListener extends BroadcastListener {
    public abstract void onBatteryChange(Intent intent);

    public final void onEvent(String action) {
        if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
            onBatteryChange(this.mIntent);
        }
    }

    protected final IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        return filter;
    }
}
