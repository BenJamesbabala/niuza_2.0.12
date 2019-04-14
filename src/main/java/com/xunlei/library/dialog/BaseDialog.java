package com.xunlei.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

public class BaseDialog extends Dialog {
    private Context mCtx;
    private Object mTag;

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mCtx = context;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.mCtx = context;
    }

    public BaseDialog(Context context) {
        super(context);
        this.mCtx = context;
    }

    public void dismiss() {
        try {
            if (this.mCtx instanceof Activity) {
                if (!((Activity) this.mCtx).isFinishing()) {
                    super.dismiss();
                }
            } else if (isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        try {
            Activity activity = getOwnerActivity();
            if (this.mCtx instanceof Activity) {
                activity = (Activity) this.mCtx;
            }
            if (activity == null) {
                super.show();
            } else if (!activity.isFinishing() && activity.getWindow() != null) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag() {
        return this.mTag;
    }
}
