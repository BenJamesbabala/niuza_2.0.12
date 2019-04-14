package com.xunlei.library.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;
import com.alibaba.fastjson.asm.Opcodes;
import com.niuza.android.R;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Orientation;

@SuppressLint({"ViewConstructor"})
public class FlipLoadingLayout extends LoadingLayout {
    static final int FLIP_ANIMATION_DURATION = 150;
    private final Animation mResetRotateAnimation;
    private final Animation mRotateAnimation;

    public FlipLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        int rotateAngle = mode == Mode.PULL_FROM_START ? -180 : Opcodes.GETFIELD;
        this.mRotateAnimation = new RotateAnimation(0.0f, (float) rotateAngle, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        this.mRotateAnimation.setDuration(150);
        this.mRotateAnimation.setFillAfter(true);
        this.mResetRotateAnimation = new RotateAnimation((float) rotateAngle, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        this.mResetRotateAnimation.setDuration(150);
        this.mResetRotateAnimation.setFillAfter(true);
        this.mHeaderProgress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.pulltorefresh_progress_circle));
    }

    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        if (imageDrawable != null) {
            int dHeight = imageDrawable.getIntrinsicHeight();
            int dWidth = imageDrawable.getIntrinsicWidth();
            ViewGroup.LayoutParams lp = this.mHeaderImage.getLayoutParams();
            int max = Math.max(dHeight, dWidth);
            lp.height = max;
            lp.width = max;
            this.mHeaderImage.requestLayout();
            this.mHeaderImage.setScaleType(ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            matrix.postTranslate(((float) (lp.width - dWidth)) / 2.0f, ((float) (lp.height - dHeight)) / 2.0f);
            matrix.postRotate(getDrawableRotationAngle(), ((float) lp.width) / 2.0f, ((float) lp.height) / 2.0f);
            this.mHeaderImage.setImageMatrix(matrix);
        }
    }

    protected void onPullImpl(float scaleOfLayout) {
    }

    protected void pullToRefreshImpl() {
        if (this.mRotateAnimation == this.mHeaderImage.getAnimation()) {
            this.mHeaderImage.startAnimation(this.mResetRotateAnimation);
        }
    }

    protected void refreshingImpl() {
        this.mHeaderImage.clearAnimation();
        this.mHeaderImage.setVisibility(View.INVISIBLE);
        this.mHeaderProgress.setVisibility(View.VISIBLE);
    }

    protected void releaseToRefreshImpl() {
        this.mHeaderImage.startAnimation(this.mRotateAnimation);
    }

    protected void resetImpl() {
        this.mHeaderImage.clearAnimation();
        this.mHeaderProgress.setVisibility(View.GONE);
        this.mHeaderImage.setVisibility(View.VISIBLE);
    }

    protected int getDefaultDrawableResId() {
        return R.drawable.pulltorefresh_default_ptr_flip;
    }

    private float getDrawableRotationAngle() {
        switch (this.mMode) {
            case PULL_FROM_END:
                if (this.mScrollDirection == Orientation.HORIZONTAL) {
                    return 90.0f;
                }
                return 180.0f;
            case PULL_FROM_START:
                if (this.mScrollDirection == Orientation.HORIZONTAL) {
                    return 270.0f;
                }
                return 0.0f;
            default:
                return 0.0f;
        }
    }
}
