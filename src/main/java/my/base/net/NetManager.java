package my.base.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import my.base.App;
import my.base.util.LogUtils;

public class NetManager {
    public static int networkType = 1;

    public static void checkNetwork(Context context) {
        if (context != null) {
            if (App.connectivityManager == null) {
                App.connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            }
            NetworkInfo networkInfo = App.connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getTypeName() != null && networkInfo.getTypeName().equalsIgnoreCase("mobile") && (networkInfo.getExtraInfo().equalsIgnoreCase("cmnet") || networkInfo.getExtraInfo().equalsIgnoreCase("3gnet"))) {
                networkType = 2;
            } else if (networkInfo != null && networkInfo.getTypeName() != null && networkInfo.getTypeName().equalsIgnoreCase("mobile") && (networkInfo.getExtraInfo().equalsIgnoreCase("cmwap") || networkInfo.getExtraInfo().equalsIgnoreCase("3gwap"))) {
                networkType = 3;
            }
            LogUtils.d(App.tag, "network is " + networkType);
        }
    }
}
