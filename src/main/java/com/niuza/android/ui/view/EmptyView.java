package com.niuza.android.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.niuza.android.AppMode;
import com.niuza.android.R;

public class EmptyView extends LinearLayout {
    private ImageView emptyImageView;
    private TextView emptyTextView;

    public EmptyView(Context context) {
        super(context);
        initUI();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_emptyview, this, true);
        this.emptyImageView = (ImageView) view.findViewById(R.id.emptyImageView);
        this.emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        this.emptyImageView.setImageResource(AppMode.getEmptyViewImageResId());
    }

    public void setEmptyText(CharSequence emptyText) {
        this.emptyTextView.setText(emptyText);
    }

    public void setEmptyImage(int imageResId) {
        this.emptyImageView.setImageResource(imageResId);
    }

    public static EmptyView addEmptyViewToParent(ViewGroup parentView) {
        if (parentView == null) {
            return null;
        }
        EmptyView emptyView = new EmptyView(parentView.getContext());
        if (parentView instanceof LinearLayout) {
            LayoutParams params = new LayoutParams(-2, -2);
            params.gravity = 17;
            parentView.addView(emptyView, params);
            return emptyView;
        } else if (parentView instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(-2, -2);
            params2.addRule(13, -1);
            parentView.addView(emptyView, params2);
            return emptyView;
        } else {
            parentView.addView(emptyView);
            return emptyView;
        }
    }
}
