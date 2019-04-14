package com.xunlei.library.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.xunlei.library.BaseApplication;
import java.util.HashSet;
import java.util.Set;

public class GlobalBroadcast {
    private static final String TAG = GlobalBroadcast.class.getSimpleName();
    private static Set<String> sFilterFlag = new HashSet();
    private static Set<BroadcastListener> sListenerList = new HashSet();
    private static BasicReceiver sRecerver = new BasicReceiver();

    public static class BasicReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            synchronized (GlobalBroadcast.sListenerList) {
            }
            for (BroadcastListener lis : (BroadcastListener[]) GlobalBroadcast.sListenerList.toArray()) {
                lis.setIntent(intent);
                lis.onEvent(action);
            }
        }
    }

    public static void registerBroadcastListener(BroadcastListener lis) {
        synchronized (sListenerList) {
            sListenerList.add(lis);
        }
        if (!sFilterFlag.contains(lis.getClass().getName())) {
            sFilterFlag.add(lis.getClass().getName());
            BaseApplication.getInstance().registerReceiver(sRecerver, lis.getIntentFilter());
        }
    }

    public static void unregisterBroadcastListener(BroadcastListener lis) {
        if (sListenerList.isEmpty()) {
            synchronized (sListenerList) {
                sFilterFlag.clear();
            }
            try {
                BaseApplication.getInstance().unregisterReceiver(sRecerver);
                return;
            } catch (Exception e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                    return;
                }
                return;
            }
        }
        synchronized (sListenerList) {
            sListenerList.remove(lis);
        }
    }
}
