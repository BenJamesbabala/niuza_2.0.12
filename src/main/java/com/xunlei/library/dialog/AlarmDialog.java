package com.xunlei.library.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.niuza.android.R;

public class AlarmDialog extends BaseDialog {
    public static final String TAG = "XLAlarmDialog";
    private TextView mContent;
    private Context mContext;
    private TextView mLeftBtn;
    private ImageView mLeftIcon;
    private TextView mRightBtn;
    private TextView mTitle;
    private Object mUserData;
    private OnClickListener mkeydownListener = null;

    public AlarmDialog(Context context) {
        super(context, R.style.bt_dialog);
        this.mContext = context;
        initUI();
    }

    public void setUserData(Object userData) {
        this.mUserData = userData;
    }

    public Object getUserData() {
        return this.mUserData;
    }

    public void setTitle(String titleStr) {
        if (titleStr != null) {
            this.mTitle.setText(titleStr);
        } else {
            this.mTitle.setText(R.string.tips);
        }
    }

    public void setContent(String titleStr) {
        if (titleStr != null) {
            this.mContent.setText(titleStr);
        }
    }

    public void setContentLineSpacing(int lineSpace) {
        this.mContent.setLineSpacing((float) lineSpace, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public void setContent(SpannableString text) {
        if (text != null) {
            this.mContent.setText(text);
        }
    }

    public void setSpanContent(SpannableString titleStr) {
        if (titleStr != null) {
            this.mContent.setText(titleStr);
        }
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            this.mLeftIcon.setVisibility(View.VISIBLE);
            this.mLeftIcon.setImageDrawable(drawable);
            return;
        }
        this.mLeftIcon.setVisibility(View.GONE);
    }

    public void setLeftBtnStr(String lStr) {
        if (lStr != null) {
            this.mLeftBtn.setText(lStr);
        }
    }

    public void setRightBtnStr(String rStr) {
        if (rStr != null) {
            this.mRightBtn.setText(rStr);
        }
    }

    public void setRightBtnBackground(Drawable drawable) {
        if (drawable != null) {
            this.mRightBtn.setBackgroundDrawable(drawable);
        }
    }

    public void setRightBtnBackground(int resid) {
        this.mRightBtn.setBackgroundResource(resid);
    }

    public void setRightBtnTextColor(int color) {
        this.mRightBtn.setTextColor(color);
    }

    public void setLeftBtnListener(OnClickListener lListener) {
        this.mLeftBtn.setTag(lListener);
        this.mLeftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ((OnClickListener) AlarmDialog.this.mLeftBtn.getTag()).onClick(AlarmDialog.this, 0);
            }
        });
    }

    public void setKeyDownListener(OnClickListener lListener) {
        this.mkeydownListener = lListener;
    }

    public void setRightBtnListener(OnClickListener rListener) {
        this.mRightBtn.setTag(rListener);
        this.mRightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ((OnClickListener) AlarmDialog.this.mRightBtn.getTag()).onClick(AlarmDialog.this, 0);
            }
        });
    }

    private void initUI() {
        View dlgView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_two_button, null);
        this.mTitle = (TextView) dlgView.findViewById(R.id.dlg_title);
        this.mContent = (TextView) dlgView.findViewById(R.id.dlg_content);
        this.mLeftIcon = (ImageView) dlgView.findViewById(R.id.dlg_left_icon);
        this.mLeftBtn = (TextView) dlgView.findViewById(R.id.dlg_left_btn);
        this.mRightBtn = (TextView) dlgView.findViewById(R.id.dlg_right_btn);
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        };
        setLeftBtnListener(listener);
        setRightBtnListener(listener);
        setContentView(dlgView);
        setCanceledOnTouchOutside(false);
    }

    public void setContentGravity(int gravity) {
        this.mContent.setGravity(gravity);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mkeydownListener != null) {
            this.mkeydownListener.onClick(this, keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }
}
