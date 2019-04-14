package com.xunlei.library.quickscroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Pin extends View {
    private static final int mPinColor = Color.argb(224, 66, 66, 66);
    private Paint mPaint;
    private Path mPath;

    public Pin(Context context) {
        super(context);
        init();
    }

    public Pin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Pin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    private void init() {
        this.mPath = new Path();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        setColor(mPinColor);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            this.mPath.reset();
            this.mPath.moveTo(0.0f, (float) getHeight());
            this.mPath.lineTo((float) getWidth(), (float) (getHeight() / 2));
            this.mPath.lineTo(0.0f, 0.0f);
            this.mPath.close();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mPaint);
        super.onDraw(canvas);
    }
}
