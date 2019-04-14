package com.niuza.android.ui.common;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.android.volley.DefaultRetryPolicy;
import com.niuza.android.R;
import com.niuza.android.ui.view.EmptyView;
//import com.umeng.analytics.MobclickAgent;
import org.szuwest.lib.BaseFragment;
import org.szuwest.utils.ScreenUtils;

public class NZBaseFragment extends BaseFragment {
    protected EmptyView mEmptyView;
    private ImageButton mToTopButton;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    public void addEmptyView() {
        if (getView() != null && this.mEmptyView == null) {
            this.mEmptyView = EmptyView.addEmptyViewToParent((ViewGroup) getView().findViewById(R.id.fragmentRootLayout));
        }
    }

    public void removeEmptyView() {
        if (this.mEmptyView != null && getView() != null) {
            ((ViewGroup) getView().findViewById(R.id.fragmentRootLayout)).removeView(this.mEmptyView);
            this.mEmptyView = null;
        }
    }

    private void addToTopButton() {
        RelativeLayout fragmentRootView = (RelativeLayout) getView().findViewById(R.id.fragmentRootLayout);
        this.mToTopButton = new ImageButton(getActivity());
        this.mToTopButton.setImageResource(R.drawable.to_top);
        this.mToTopButton.setBackgroundDrawable(null);
        this.mToTopButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NZBaseFragment.this.scrollToTop();
                NZBaseFragment.this.hideToTopBtn();
            }
        });
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(11, -1);
        params.addRule(12, -1);
        params.rightMargin = ScreenUtils.dip2px(15.0f);
        params.bottomMargin = ScreenUtils.dip2px(15.0f);
        fragmentRootView.addView(this.mToTopButton, params);
    }

    public void showToTopBtn() {
        if (this.mToTopButton == null) {
            addToTopButton();
        }
        if (this.mToTopButton.getVisibility() != View.VISIBLE || this.mToTopButton.getAlpha() <= 0.98f) {
            this.mToTopButton.setAlpha(0.0f);
            this.mToTopButton.setVisibility(View.VISIBLE);
            this.mToTopButton.animate().alpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).setDuration(300).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    public void hideToTopBtn() {
        if (this.mToTopButton != null && this.mToTopButton.getVisibility() == View.VISIBLE) {
            this.mToTopButton.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    NZBaseFragment.this.mToTopButton.setVisibility(View.INVISIBLE);
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
    }

    public void scrollToTop() {
    }
}
