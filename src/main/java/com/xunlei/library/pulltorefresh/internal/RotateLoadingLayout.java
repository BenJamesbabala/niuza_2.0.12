package com.xunlei.library.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import com.niuza.android.R;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Orientation;
import com.xunlei.library.utils.ScreenUtils;

public class RotateLoadingLayout extends LoadingLayout {
    static final int ROTATION_ANIMATION_DURATION = 1200;
    private final Matrix mHeaderHourImageMatrix;
    private final Matrix mHeaderImageMatrix = new Matrix();
    private final Animation mHourRotateAnimation;
    private final Animation mRotateAnimation;
    private final boolean mRotateDrawableClockStyle;
    private final boolean mRotateDrawableWhilePulling;
    private float mRotationPivotX;
    private float mRotationPivotY;

    public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        this.mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
        this.mRotateDrawableClockStyle = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableClockStyle, false);
        this.mHeaderImage.setScaleType(ScaleType.MATRIX);
        this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
        if (this.mRotateDrawableClockStyle) {
            this.mHeaderClockBgImage.setImageResource(R.drawable.pullltorefresh_clock);
            this.mHeaderClockHourImage.setImageResource(R.drawable.pulltorefresh_hour);
            this.mHeaderImage.setImageResource(R.drawable.pulltorefresh_min);
            setLoadingDrawable(context.getResources().getDrawable(R.drawable.pulltorefresh_min));
            this.mRotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
            this.mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
            this.mRotateAnimation.setDuration(1200);
            this.mRotateAnimation.setRepeatCount(-1);
            this.mRotateAnimation.setRepeatMode(1);
            this.mHourRotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
            this.mHourRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
            this.mHourRotateAnimation.setDuration(14400);
            this.mHourRotateAnimation.setRepeatCount(-1);
            this.mHourRotateAnimation.setRepeatMode(1);
            this.mHeaderHourImageMatrix = new Matrix();
            this.mHeaderClockHourImage.setScaleType(ScaleType.MATRIX);
            this.mHeaderClockHourImage.setImageMatrix(this.mHeaderImageMatrix);
            centerDrawable();
            return;
        }
        this.mRotateAnimation = new RotateAnimation(0.0f, 720.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        this.mRotateAnimation.setDuration(1200);
        this.mRotateAnimation.setRepeatCount(-1);
        this.mRotateAnimation.setRepeatMode(1);
        this.mHourRotateAnimation = null;
        this.mHeaderHourImageMatrix = null;
    }

    private void centerDrawable() {
        View imageContainer = findViewById(R.id.imageContainer);
        LayoutParams params = (LayoutParams) imageContainer.getLayoutParams();
        params.gravity = 1;
        imageContainer.setLayoutParams(params);
        View textContainer = findViewById(R.id.textContainer);
        LayoutParams params2 = (LayoutParams) textContainer.getLayoutParams();
        params2.gravity = 1;
        textContainer.setLayoutParams(params2);
        textContainer.setPadding(0, ScreenUtils.dip2px(22.0f), 0, 0);
    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.mRotationPivotX = (float) Math.round(((float) imageDrawable.getIntrinsicWidth()) / 2.0f);
            this.mRotationPivotY = (float) Math.round(((float) imageDrawable.getIntrinsicHeight()) / 2.0f);
        }
    }

    protected void onPullImpl(float scaleOfLayout) {
        float angle;
        if (this.mRotateDrawableWhilePulling) {
            angle = scaleOfLayout * 90.0f;
        } else {
            angle = Math.max(0.0f, Math.min(180.0f, (360.0f * scaleOfLayout) - 180.0f));
        }
        if (this.mRotateDrawableClockStyle) {
            this.mHeaderImageMatrix.setRotate((-angle) * 4.0f, this.mRotationPivotX, this.mRotationPivotY);
            this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
            this.mHeaderHourImageMatrix.setRotate(((-angle) * 4.0f) / 12.0f, this.mRotationPivotX, this.mRotationPivotY);
            this.mHeaderClockHourImage.setImageMatrix(this.mHeaderHourImageMatrix);
            return;
        }
        this.mHeaderImageMatrix.setRotate(angle, this.mRotationPivotX, this.mRotationPivotY);
        this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
    }

    protected void refreshingImpl() {
        this.mHeaderImage.startAnimation(this.mRotateAnimation);
        if (this.mRotateDrawableClockStyle) {
            this.mHeaderClockHourImage.startAnimation(this.mHourRotateAnimation);
        }
    }

    protected void resetImpl() {
        this.mHeaderImage.clearAnimation();
        if (this.mRotateDrawableClockStyle) {
            this.mHeaderClockHourImage.clearAnimation();
        }
        resetImageRotation();
    }

    private void resetImageRotation() {
        if (this.mHeaderImageMatrix != null) {
            this.mHeaderImageMatrix.reset();
            this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
        }
        if (this.mHeaderHourImageMatrix != null) {
            this.mHeaderHourImageMatrix.reset();
            this.mHeaderClockHourImage.setImageMatrix(this.mHeaderHourImageMatrix);
        }
    }

    protected void pullToRefreshImpl() {
    }

    protected void releaseToRefreshImpl() {
    }

    protected int getDefaultDrawableResId() {
        return R.drawable.pulltorefresh_default_ptr_rotate;
    }
}
