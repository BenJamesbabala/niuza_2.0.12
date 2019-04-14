package com.xunlei.library.asymmetricgridview.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;

public class IcsColorDrawable extends Drawable {
    private int color;
    private final Paint paint = new Paint();

    public IcsColorDrawable(ColorDrawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
        drawable.draw(new Canvas(bitmap));
        this.color = bitmap.getPixel(0, 0);
        bitmap.recycle();
    }

    public IcsColorDrawable(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas) {
        if ((this.color >>> 24) != 0) {
            this.paint.setColor(this.color);
            canvas.drawRect(getBounds(), this.paint);
        }
    }

    public void setAlpha(int alpha) {
        if (alpha != (this.color >>> 24)) {
            this.color = (this.color & ViewCompat.MEASURED_SIZE_MASK) | (alpha << 24);
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public int getOpacity() {
        return this.color >>> 24;
    }
}
