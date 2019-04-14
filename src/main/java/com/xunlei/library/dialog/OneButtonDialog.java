package com.xunlei.library.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.niuza.android.R;

public class OneButtonDialog extends BaseDialog {
    public static final String TAG = "XLOneButtonDialog";
    private TextView mBottomBtn;
    private OnClickListener mBottomBtnListener = null;
    private TextView mContent;
    private Context mContext;
    private ImageView mLeftIcon;
    private View mSingleBtnView = null;
    private TextView mTitle;

    public OneButtonDialog(Context context) {
        super(context, R.style.dc_bt_dialog);
        this.mContext = context;
        initUI();
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

    public void setBottomBtnStr(String bottomStr) {
        if (bottomStr != null) {
            this.mBottomBtn.setText(bottomStr);
        }
    }

    public void setBottomBtnStrColor(int color) {
        this.mBottomBtn.setTextColor(color);
    }

    public void setBottomBtnListener(OnClickListener bListener) {
        if (bListener != null) {
            this.mBottomBtnListener = bListener;
            this.mSingleBtnView.setTag(bListener);
            this.mSingleBtnView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    ((OnClickListener) arg0.getTag()).onClick(OneButtonDialog.this, 0);
                }
            });
        }
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            this.mLeftIcon.setVisibility(View.VISIBLE);
            this.mLeftIcon.setImageDrawable(drawable);
        }
    }

    public void setContentGravity(int gravity) {
        this.mContent.setGravity(gravity);
    }

    private void initUI() {
        View dlgView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_one_button, null);
        this.mTitle = (TextView) dlgView.findViewById(R.id.dlg_title);
        this.mContent = (TextView) dlgView.findViewById(R.id.dlg_content);
        setCanceledOnTouchOutside(false);
        this.mSingleBtnView = dlgView.findViewById(R.id.dlg_bottom_btn);
        this.mBottomBtn = (TextView) dlgView.findViewById(R.id.dlg_bottom_text);
        this.mBottomBtn.setVisibility(View.VISIBLE);
        this.mSingleBtnView.setVisibility(View.VISIBLE);
        if (this.mBottomBtnListener == null) {
            setBottomBtnListener(new OnClickListener() {
                public void onClick(DialogInterface dlg, int arg1) {
                    dlg.dismiss();
                }
            });
        }
        setContentView(dlgView);
    }
}
