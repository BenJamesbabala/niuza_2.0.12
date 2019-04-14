package com.niuza.android.ui.common;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.android.volley.DefaultRetryPolicy;
import com.niuza.android.R;
import com.niuza.android.ui.view.EmptyView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
//import com.umeng.analytics.MobclickAgent;
import org.szuwest.lib.BaseActivity;
import org.szuwest.utils.ScreenUtils;

public class NABaseActivity extends BaseActivity {
    protected EmptyView mEmptyView = null;
    private ImageButton mToTopButton;
    protected boolean swipeAnyWhere = false;
    protected boolean swipeEnabled = true;
    private boolean swipeFinished = false;
    private SwipeLayout swipeLayout;

    class SwipeLayout extends FrameLayout {
        ObjectAnimator animator;
        boolean canSwipe = false;
        View content;
        float currentX;
        float currentY;
        float downX;
        float downY;
        private final int duration = 200;
        boolean hasIgnoreFirstMove;
        boolean ignoreSwipe = false;
        float lastX;
        private Drawable leftShadow;
        Activity mActivity;
        int screenWidth = 1080;
        int sideWidth = 72;
        int sideWidthInDP = 16;
        int touchSlop = 60;
        int touchSlopDP = 30;
        VelocityTracker tracker;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void replaceLayer(Activity activity) {
            this.leftShadow = activity.getResources().getDrawable(R.drawable.left_shadow);
            this.touchSlop = (int) (((float) this.touchSlopDP) * activity.getResources().getDisplayMetrics().density);
            this.sideWidth = (int) (((float) this.sideWidthInDP) * activity.getResources().getDisplayMetrics().density);
            this.mActivity = activity;
            this.screenWidth = NABaseActivity.getScreenWidth(activity);
            setClickable(true);
            ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
            this.content = root.getChildAt(0);
            ViewGroup.LayoutParams params = this.content.getLayoutParams();
            LayoutParams params2 = new LayoutParams(-1, -1);
            root.removeView(this.content);
            addView(this.content, params2);
            root.addView(this, params);
        }

        protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
            boolean result = super.drawChild(canvas, child, drawingTime);
            int shadowWidth = this.leftShadow.getIntrinsicWidth();
            int left = ((int) getContentX()) - shadowWidth;
            this.leftShadow.setBounds(left, child.getTop(), left + shadowWidth, child.getBottom());
            this.leftShadow.draw(canvas);
            return result;
        }

        public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
            if (!(!NABaseActivity.this.swipeEnabled || this.canSwipe || this.ignoreSwipe)) {
                if (NABaseActivity.this.swipeAnyWhere) {
                    switch (ev.getAction()) {
                        case 0:
                            this.downX = ev.getX();
                            this.downY = ev.getY();
                            this.currentX = this.downX;
                            this.currentY = this.downY;
                            this.lastX = this.downX;
                            break;
                        case 2:
                            float dx = ev.getX() - this.downX;
                            float dy = ev.getY() - this.downY;
                            if ((dx * dx) + (dy * dy) > ((float) (this.touchSlop * this.touchSlop))) {
                                if (dy != 0.0f && Math.abs(dx / dy) <= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                                    this.ignoreSwipe = true;
                                    break;
                                }
                                this.downX = ev.getX();
                                this.downY = ev.getY();
                                this.currentX = this.downX;
                                this.currentY = this.downY;
                                this.lastX = this.downX;
                                this.canSwipe = true;
                                this.tracker = VelocityTracker.obtain();
                                return true;
                            }
                            break;
                    }
                } else if (ev.getAction() == 0 && ev.getX() < ((float) this.sideWidth)) {
                    this.canSwipe = true;
                    this.tracker = VelocityTracker.obtain();
                    return true;
                }
            }
            if (ev.getAction() == 1 || ev.getAction() == 3) {
                this.ignoreSwipe = false;
            }
            return super.dispatchTouchEvent(ev);
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return this.canSwipe || super.onInterceptTouchEvent(ev);
        }

        public boolean onTouchEvent(@NonNull MotionEvent event) {
            if (this.canSwipe) {
                this.tracker.addMovement(event);
                switch (event.getAction()) {
                    case 0:
                        this.downX = event.getX();
                        this.downY = event.getY();
                        this.currentX = this.downX;
                        this.currentY = this.downY;
                        this.lastX = this.downX;
                        break;
                    case 1:
                    case 3:
                        this.tracker.computeCurrentVelocity(10000);
                        this.tracker.computeCurrentVelocity(1000, 20000.0f);
                        this.canSwipe = false;
                        this.hasIgnoreFirstMove = false;
                        if (Math.abs(this.tracker.getXVelocity()) > ((float) ((this.screenWidth / 200) * 1000))) {
                            animateFromVelocity(this.tracker.getXVelocity());
                        } else if (getContentX() > ((float) (this.screenWidth / 3))) {
                            animateFinish(false);
                        } else {
                            animateBack(false);
                        }
                        this.tracker.recycle();
                        break;
                    case 2:
                        this.currentX = event.getX();
                        this.currentY = event.getY();
                        float dx = this.currentX - this.lastX;
                        if (!(dx == 0.0f || this.hasIgnoreFirstMove)) {
                            this.hasIgnoreFirstMove = true;
                            dx /= dx;
                        }
                        if (getContentX() + dx < 0.0f) {
                            setContentX(0.0f);
                        } else {
                            setContentX(getContentX() + dx);
                        }
                        this.lastX = this.currentX;
                        break;
                }
            }
            return super.onTouchEvent(event);
        }

        public void cancelPotentialAnimation() {
            if (this.animator != null) {
                this.animator.removeAllListeners();
                this.animator.cancel();
            }
        }

        public void setContentX(float x) {
            this.content.setX((float) ((int) x));
            invalidate();
        }

        public float getContentX() {
            return this.content.getX();
        }

        private void animateBack(boolean withVel) {
            cancelPotentialAnimation();
            this.animator = ObjectAnimator.ofFloat(this, "contentX", new float[]{getContentX(), 0.0f});
            int tmpDuration = withVel ? (int) ((200.0f * getContentX()) / ((float) this.screenWidth)) : 200;
            if (tmpDuration < 100) {
                tmpDuration = 100;
            }
            this.animator.setDuration((long) tmpDuration);
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.start();
        }

        private void animateFinish(boolean withVel) {
            cancelPotentialAnimation();
            this.animator = ObjectAnimator.ofFloat(this, "contentX", new float[]{getContentX(), (float) this.screenWidth});
            int tmpDuration = withVel ? (int) ((200.0f * (((float) this.screenWidth) - getContentX())) / ((float) this.screenWidth)) : 200;
            if (tmpDuration < 100) {
                tmpDuration = 100;
            }
            this.animator.setDuration((long) tmpDuration);
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!SwipeLayout.this.mActivity.isFinishing()) {
                        NABaseActivity.this.swipeFinished = true;
                        SwipeLayout.this.mActivity.finish();
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
            this.animator.start();
        }

        private void animateFromVelocity(float v) {
            if (v > 0.0f) {
                if (getContentX() >= ((float) (this.screenWidth / 2)) || ((v * 200.0f) / 1000.0f) + getContentX() >= ((float) (this.screenWidth / 2))) {
                    animateFinish(true);
                } else {
                    animateBack(false);
                }
            } else if (getContentX() <= ((float) (this.screenWidth / 2)) || ((v * 200.0f) / 1000.0f) + getContentX() <= ((float) (this.screenWidth / 2))) {
                animateBack(true);
            } else {
                animateFinish(false);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        this.swipeLayout = new SwipeLayout(this);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
    }

    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void addEmptyView() {
        if (this.mEmptyView == null) {
            this.mEmptyView = EmptyView.addEmptyViewToParent((ViewGroup) findViewById(R.id.rootLayout));
        }
    }

    public void removeEmptyView() {
        if (this.mEmptyView != null) {
            ((ViewGroup) findViewById(R.id.rootLayout)).removeView(this.mEmptyView);
            this.mEmptyView = null;
        }
    }

    private void addToTopButton() {
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootLayout);
        this.mToTopButton = new ImageButton(this);
        this.mToTopButton.setImageResource(R.drawable.to_top);
        this.mToTopButton.setBackgroundDrawable(null);
        this.mToTopButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NABaseActivity.this.scrollToTop();
                NABaseActivity.this.hideToTopBtn();
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(11, -1);
        params.addRule(12, -1);
        params.rightMargin = ScreenUtils.dip2px(15.0f);
        params.bottomMargin = ScreenUtils.dip2px(15.0f);
        rootView.addView(this.mToTopButton, params);
    }

    public void showToTopBtn() {
        if (this.mToTopButton == null) {
            addToTopButton();
        }
        if (this.mToTopButton.getVisibility() != 0 || this.mToTopButton.getAlpha() <= 0.98f) {
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
        if (this.mToTopButton != null && this.mToTopButton.getVisibility() == 0) {
            this.mToTopButton.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    NABaseActivity.this.mToTopButton.setVisibility(View.INVISIBLE);
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

    public void setSwipeAnyWhere(boolean swipeAnyWhere) {
        this.swipeAnyWhere = swipeAnyWhere;
    }

    public boolean isSwipeAnyWhere() {
        return this.swipeAnyWhere;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    public boolean isSwipeEnabled() {
        return this.swipeEnabled;
    }

    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.swipeLayout.replaceLayer(this);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void finish() {
        if (this.swipeFinished) {
            super.finish();
            overridePendingTransition(0, 0);
            return;
        }
        this.swipeLayout.cancelPotentialAnimation();
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }
}
