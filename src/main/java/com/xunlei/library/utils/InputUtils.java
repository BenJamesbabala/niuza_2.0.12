package com.xunlei.library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {
    public static void hiddenInput(Context ctx, View v) {
        if ((ctx instanceof Context) && (v instanceof View)) {
            ((InputMethodManager) ctx.getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void showInput(Context ctx) {
        if (ctx instanceof Context) {
            ((InputMethodManager) ctx.getSystemService("input_method")).toggleSoftInput(0, 2);
        }
    }

    public static boolean isInputActive(Context ctx) {
        if (ctx instanceof Context) {
            return ((InputMethodManager) ctx.getSystemService("input_method")).isActive();
        }
        return false;
    }
}
