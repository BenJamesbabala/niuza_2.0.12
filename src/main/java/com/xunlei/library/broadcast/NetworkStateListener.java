package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class NetworkStateListener extends BroadcastListener {
    public abstract void onNetworkStateChange(Intent intent);

    public final void onEvent(String action) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            onNetworkStateChange(this.mIntent);
        }
    }

    protected final IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        return filter;
    }
}
