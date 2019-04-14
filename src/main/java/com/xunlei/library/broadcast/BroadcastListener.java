package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class BroadcastListener {
    protected Intent mIntent;

    protected abstract IntentFilter getIntentFilter();

    protected abstract void onEvent(String str);

    protected void setIntent(Intent it) {
        this.mIntent = it;
    }
}
