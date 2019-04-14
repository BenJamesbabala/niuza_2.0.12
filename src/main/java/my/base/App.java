package my.base;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;

public class App {
    public static ConnectivityManager connectivityManager;
    public static Context context;
    public static String tag = "BaseLib";

    public static void init(Context context) {
        context = context;
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        }
    }

    public static void setContext(Context context) {
        context = context;
    }
}
