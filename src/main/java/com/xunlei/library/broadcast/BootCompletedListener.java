package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class BootCompletedListener extends BroadcastListener {
    public abstract void onBootCompleted(Intent intent);

    protected final void onEvent(String action) {
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            onBootCompleted(this.mIntent);
        }
    }

    protected final IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        return filter;
    }
}
