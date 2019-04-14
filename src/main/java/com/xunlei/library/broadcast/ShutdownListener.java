package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class ShutdownListener extends BroadcastListener {
    public abstract void onShutdown(Intent intent);

    public void onEvent(String action) {
        if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
            onShutdown(this.mIntent);
        }
    }

    protected IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        return filter;
    }
}
