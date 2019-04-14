package com.xunlei.library.asymmetricgridview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {
    public static int dpToPx(Context context, float dp) {
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        return getDisplayMetrics(context).widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
