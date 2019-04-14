package com.xunlei.library.asymmetricgridview.widget;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.android.volley.DefaultRetryPolicy;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class AnimatorProxy extends Animation {
    public static final boolean NEEDS_PROXY = (VERSION.SDK_INT < 11);
    private static final WeakHashMap<View, AnimatorProxy> PROXIES = new WeakHashMap();
    private final RectF mAfter = new RectF();
    private float mAlpha = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    private final RectF mBefore = new RectF();
    private float mScaleX = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    private float mScaleY = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    private final Matrix mTempMatrix = new Matrix();
    private float mTranslationX;
    private float mTranslationY;
    private final WeakReference<View> mView;

    public static AnimatorProxy wrap(View view) {
        AnimatorProxy proxy = (AnimatorProxy) PROXIES.get(view);
        if (proxy != null) {
            return proxy;
        }
        proxy = new AnimatorProxy(view);
        PROXIES.put(view, proxy);
        return proxy;
    }

    private AnimatorProxy(View view) {
        setDuration(0);
        setFillAfter(true);
        view.setAnimation(this);
        this.mView = new WeakReference(view);
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float alpha) {
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            View view = (View) this.mView.get();
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public void setScaleX(float scaleX) {
        if (this.mScaleX != scaleX) {
            prepareForUpdate();
            this.mScaleX = scaleX;
            invalidateAfterUpdate();
        }
    }

    public float getScaleY() {
        return this.mScaleY;
    }

    public void setScaleY(float scaleY) {
        if (this.mScaleY != scaleY) {
            prepareForUpdate();
            this.mScaleY = scaleY;
            invalidateAfterUpdate();
        }
    }

    public int getScrollX() {
        View view = (View) this.mView.get();
        if (view == null) {
            return 0;
        }
        return view.getScrollX();
    }

    public void setScrollX(int value) {
        View view = (View) this.mView.get();
        if (view != null) {
            view.scrollTo(value, view.getScrollY());
        }
    }

    public int getScrollY() {
        View view = (View) this.mView.get();
        if (view == null) {
            return 0;
        }
        return view.getScrollY();
    }

    public void setScrollY(int value) {
        View view = (View) this.mView.get();
        if (view != null) {
            view.scrollTo(view.getScrollY(), value);
        }
    }

    public float getTranslationX() {
        return this.mTranslationX;
    }

    public void setTranslationX(float translationX) {
        if (this.mTranslationX != translationX) {
            prepareForUpdate();
            this.mTranslationX = translationX;
            invalidateAfterUpdate();
        }
    }

    public float getTranslationY() {
        return this.mTranslationY;
    }

    public void setTranslationY(float translationY) {
        if (this.mTranslationY != translationY) {
            prepareForUpdate();
            this.mTranslationY = translationY;
            invalidateAfterUpdate();
        }
    }

    private void prepareForUpdate() {
        View view = (View) this.mView.get();
        if (view != null) {
            computeRect(this.mBefore, view);
        }
    }

    private void invalidateAfterUpdate() {
        View view = (View) this.mView.get();
        if (view != null) {
            View parent = (View) view.getParent();
            if (parent != null) {
                view.setAnimation(this);
                RectF after = this.mAfter;
                computeRect(after, view);
                after.union(this.mBefore);
                parent.invalidate((int) Math.floor((double) after.left), (int) Math.floor((double) after.top), (int) Math.ceil((double) after.right), (int) Math.ceil((double) after.bottom));
            }
        }
    }

    private void computeRect(RectF r, View view) {
        r.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        Matrix m = this.mTempMatrix;
        m.reset();
        transformMatrix(m, view);
        this.mTempMatrix.mapRect(r);
        r.offset((float) view.getLeft(), (float) view.getTop());
        if (r.right < r.left) {
            float f = r.right;
            r.right = r.left;
            r.left = f;
        }
        if (r.bottom < r.top) {
            float f = r.top;
            r.top = r.bottom;
            r.bottom = f;
        }
    }

    private void transformMatrix(Matrix m, View view) {
        float w = (float) view.getWidth();
        float h = (float) view.getHeight();
        float sX = this.mScaleX;
        float sY = this.mScaleY;
        if (!(sX == DefaultRetryPolicy.DEFAULT_BACKOFF_MULT && sY == DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)) {
            float deltaSX = ((sX * w) - w) / 2.0f;
            float deltaSY = ((sY * h) - h) / 2.0f;
            m.postScale(sX, sY);
            m.postTranslate(-deltaSX, -deltaSY);
        }
        m.postTranslate(this.mTranslationX, this.mTranslationY);
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        View view = (View) this.mView.get();
        if (view != null) {
            t.setAlpha(this.mAlpha);
            transformMatrix(t.getMatrix(), view);
        }
    }

    public void reset() {
    }
}
