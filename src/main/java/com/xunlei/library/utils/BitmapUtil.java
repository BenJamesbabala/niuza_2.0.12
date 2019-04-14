package com.xunlei.library.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.xunlei.library.BaseApplication;
import java.lang.reflect.Array;

public class BitmapUtil {
    public static int getAvalageColor(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);
                pixelCount++;
                redBucket += (long) Color.red(c);
                greenBucket += (long) Color.green(c);
                blueBucket += (long) Color.blue(c);
            }
        }
        return Color.rgb((int) (redBucket / pixelCount), (int) (greenBucket / pixelCount), (int) (blueBucket / pixelCount));
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;
        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int h = bitmap.getHeight();
        for (int y = 0; y < h; y++) {
            int w = bitmap.getWidth();
            for (int x = 0; x < w; x++) {
                int color = pixels[(y * w) + x];
                redBucket += (color >> 16) & 255;
                greenBucket += (color >> 8) & 255;
                blueBucket += color & 255;
                if (hasAlpha) {
                    alphaBucket += color >>> 24;
                }
            }
        }
        return Color.argb(hasAlpha ? alphaBucket / pixelCount : 255, redBucket / pixelCount, greenBucket / pixelCount, blueBucket / pixelCount);
    }

    public static Bitmap cutBitmapForRect(Bitmap bmp, Rect r, Config config) {
        int width = r.width();
        int height = r.height();
        Bitmap croppedImage = Bitmap.createBitmap(width, height, config);
        new Canvas(croppedImage).drawBitmap(bmp, r, new Rect(0, 0, width, height), null);
        return croppedImage;
    }

    public static Bitmap cutBitmapForSquare(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        if (bmpHeight > bmpWidth) {
            return Bitmap.createBitmap(bmp, 0, (bmpHeight - bmpWidth) / 2, bmpWidth, bmpWidth);
        } else if (bmpHeight >= bmpWidth) {
            return bmp;
        } else {
            return Bitmap.createBitmap(bmp, (bmpWidth - bmpHeight) / 2, 0, bmpHeight, bmpHeight);
        }
    }

    public static Bitmap cutBitmapForCircle(Bitmap bmp, int radius) {
        Bitmap squareBitmap;
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        Bitmap bitmap;
        int i;
        if (bmpHeight > bmpWidth) {
            bitmap = bmp;
            i = (bmpHeight - bmpWidth) / 2;
            squareBitmap = Bitmap.createBitmap(bitmap, 0, i, bmpWidth, bmpWidth);
        } else if (bmpHeight < bmpWidth) {
            int x = (bmpWidth - bmpHeight) / 2;
            bitmap = bmp;
            i = 0;
            squareBitmap = Bitmap.createBitmap(bitmap, x, i, bmpHeight, bmpHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() == diameter && squareBitmap.getHeight() == diameter) {
            scaledSrcBmp = squareBitmap;
        } else {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((float) (scaledSrcBmp.getWidth() / 2), (float) (scaledSrcBmp.getHeight() / 2), (float) (scaledSrcBmp.getWidth() / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        return output;
    }

    @TargetApi(17)
    public static Bitmap blurBitmap(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        RenderScript rs = RenderScript.create(BaseApplication.getInstance());
        Allocation input = Allocation.createFromBitmap(rs, sentBitmap, MipmapControl.MIPMAP_NONE, 1);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius((float) radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }
//
//    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
//        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig() == null ? Config.ARGB_8888 : sentBitmap.getConfig(), true);
//        if (radius < 1 || bitmap == null) {
//            return sentBitmap;
//        }
//        int i;
//        int y;
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        int[] pix = new int[(w * h)];
//        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//        int wm = w - 1;
//        int hm = h - 1;
//        int wh = w * h;
//        int div = (radius + radius) + 1;
//        int[] r = new int[wh];
//        int[] g = new int[wh];
//        int[] b = new int[wh];
//        int[] vmin = new int[Math.max(w, h)];
//        int divsum = (div + 1) >> 1;
//        divsum *= divsum;
//        int[] dv = new int[(divsum * 256)];
//        for (i = 0; i < divsum * 256; i++) {
//            dv[i] = i / divsum;
//        }
//        int yi = 0;
//        int yw = 0;
//        int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{div, 3});
//        int r1 = radius + 1;
//        for (y = 0; y < h; y++) {
//            int x;
//            int bsum = 0;
//            int gsum = 0;
//            int rsum = 0;
//            int boutsum = 0;
//            int goutsum = 0;
//            int routsum = 0;
//            int binsum = 0;
//            int ginsum = 0;
//            int rinsum = 0;
//            for (i = -radius; i <= radius; i++) {
//                int p = pix[Math.min(wm, Math.max(i, 0)) + yi];
//                int[] sir = stack[i + radius];
//                sir[0] = (16711680 & p) >> 16;
//                sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
//                sir[2] = p & 255;
//                int rbs = r1 - Math.abs(i);
//                rsum += sir[0] * rbs;
//                gsum += sir[1] * rbs;
//                bsum += sir[2] * rbs;
//                if (i > 0) {
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                } else {
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                }
//            }
//            int stackpointer = radius;
//            for (x = 0; x < w; x++) {
//                r[yi] = dv[rsum];
//                g[yi] = dv[gsum];
//                b[yi] = dv[bsum];
//                rsum -= routsum;
//                gsum -= goutsum;
//                bsum -= boutsum;
//                sir = stack[((stackpointer - radius) + div) % div];
//                routsum -= sir[0];
//                goutsum -= sir[1];
//                boutsum -= sir[2];
//                if (y == 0) {
//                    vmin[x] = Math.min((x + radius) + 1, wm);
//                }
//                p = pix[vmin[x] + yw];
//                sir[0] = (16711680 & p) >> 16;
//                sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
//                sir[2] = p & 255;
//                rinsum += sir[0];
//                ginsum += sir[1];
//                binsum += sir[2];
//                rsum += rinsum;
//                gsum += ginsum;
//                bsum += binsum;
//                stackpointer = (stackpointer + 1) % div;
//                sir = stack[stackpointer % div];
//                routsum += sir[0];
//                goutsum += sir[1];
//                boutsum += sir[2];
//                rinsum -= sir[0];
//                ginsum -= sir[1];
//                binsum -= sir[2];
//                yi++;
//            }
//            yw += w;
//        }
//        for (x = 0; x < w; x++) {
//            bsum = 0;
//            gsum = 0;
//            rsum = 0;
//            boutsum = 0;
//            goutsum = 0;
//            routsum = 0;
//            binsum = 0;
//            ginsum = 0;
//            rinsum = 0;
//            int yp = (-radius) * w;
//            for (i = -radius; i <= radius; i++) {
//                yi = Math.max(0, yp) + x;
//                sir = stack[i + radius];
//                sir[0] = r[yi];
//                sir[1] = g[yi];
//                sir[2] = b[yi];
//                rbs = r1 - Math.abs(i);
//                rsum += r[yi] * rbs;
//                gsum += g[yi] * rbs;
//                bsum += b[yi] * rbs;
//                if (i > 0) {
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                } else {
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                }
//                if (i < hm) {
//                    yp += w;
//                }
//            }
//            yi = x;
//            stackpointer = radius;
//            for (y = 0; y < h; y++) {
//                pix[yi] = (((ViewCompat.MEASURED_STATE_MASK & pix[yi]) | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
//                rsum -= routsum;
//                gsum -= goutsum;
//                bsum -= boutsum;
//                sir = stack[((stackpointer - radius) + div) % div];
//                routsum -= sir[0];
//                goutsum -= sir[1];
//                boutsum -= sir[2];
//                if (x == 0) {
//                    vmin[y] = Math.min(y + r1, hm) * w;
//                }
//                p = x + vmin[y];
//                sir[0] = r[p];
//                sir[1] = g[p];
//                sir[2] = b[p];
//                rinsum += sir[0];
//                ginsum += sir[1];
//                binsum += sir[2];
//                rsum += rinsum;
//                gsum += ginsum;
//                bsum += binsum;
//                stackpointer = (stackpointer + 1) % div;
//                sir = stack[stackpointer];
//                routsum += sir[0];
//                goutsum += sir[1];
//                boutsum += sir[2];
//                rinsum -= sir[0];
//                ginsum -= sir[1];
//                binsum -= sir[2];
//                yi += w;
//            }
//        }
//        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
//        return bitmap;
//    }
}
