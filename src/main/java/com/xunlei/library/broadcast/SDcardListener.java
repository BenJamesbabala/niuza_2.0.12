package com.xunlei.library.broadcast;

import android.content.Intent;
import android.content.IntentFilter;

public abstract class SDcardListener extends BroadcastListener {
    public final void onEvent(String action) {
        if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
            onSDcardMounted(this.mIntent);
        } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
            onSDcardUnmounted(this.mIntent);
        } else if ("android.intent.action.MEDIA_REMOVED".equals(action) || "android.intent.action.MEDIA_BAD_REMOVAL".equals(action)) {
            onSDcardRemoved(this.mIntent);
        } else if ("android.intent.action.MEDIA_SCANNER_FINISHED".equals(action)) {
            onSDcardReady(this.mIntent);
        } else if ("android.intent.action.MEDIA_SHARED".equals(action)) {
            onSDcardShared(this.mIntent);
        }
    }

    protected final IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter.addDataScheme("file");
        return intentFilter;
    }

    public void onSDcardMounted(Intent intent) {
    }

    public void onSDcardUnmounted(Intent intent) {
    }

    public void onSDcardRemoved(Intent intent) {
    }

    public void onSDcardReady(Intent intent) {
    }

    public void onSDcardShared(Intent intent) {
    }
}
