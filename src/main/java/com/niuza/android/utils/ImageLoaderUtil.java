package com.niuza.android.utils;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageView;
//import com.alibaba.wireless.security.SecExceptionCode;
import com.niuza.android.AppMode;
import com.niuza.android.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;
//import com.xunlei.library.utils.ConvertUtil;
//import com.xunlei.library.utils.XLLog;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.szuwest.lib.BaseApplication;

public class ImageLoaderUtil {
    private static int PIC_DEFAULT;
//    private static DisplayImageOptions defaultOption = new Builder().showImageOnLoading((int) R.drawable.pic_loading_stub).showImageForEmptyUri(PIC_DEFAULT).showImageOnFail(PIC_DEFAULT).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Config.RGB_565).resetViewBeforeLoading(true).considerExifParams(true).displayer(new FadeInBitmapDisplayer(SecExceptionCode.SEC_ERROR_PKG_VALID, true, true, false)).build();
private static DisplayImageOptions defaultOption = new Builder().showImageOnLoading((int) R.drawable.pic_loading_stub).showImageForEmptyUri(PIC_DEFAULT).showImageOnFail(PIC_DEFAULT).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Config.RGB_565).resetViewBeforeLoading(true).considerExifParams(true).displayer(new FadeInBitmapDisplayer(800, true, true, false)).build();
    static {
        PIC_DEFAULT = R.drawable.pic_default;
        ActivityManager am = (ActivityManager) BaseApplication.getInstance().getSystemService("activity");
        int memoryClass = am.getMemoryClass();
        int largeMemoryClass = am.getLargeMemoryClass();
        int memoryCacheSize = (1048576 * largeMemoryClass) / 8;
//        XLLog.d("XlImageLoader", "memoryClass=" + memoryClass + " largeMemoryClass=" + largeMemoryClass + " runTimeMaxMemory=" + ((Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
//        XLLog.d("XlImageLoader", "memoryCacheSize=" + memoryCacheSize);
        File cacheDir = StorageUtils.getCacheDirectory(BaseApplication.getInstance());
        long diskCacheSize = DeviceHelper.getAvailableExternalMemorySize();
//        XLLog.d("XlImageLoader", "AvailableExternalMemorySize=" + ((diskCacheSize / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / 1204) + ConvertUtil.UNIT_MB);
        diskCacheSize /= 5;
        if (diskCacheSize < 104857600) {
            diskCacheSize = 104857600;
        }
        if (diskCacheSize > 1073741824) {
            diskCacheSize = 1073741824;
        }
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(BaseApplication.getInstance()).threadPoolSize(4).threadPriority(5).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LRULimitedMemoryCache(memoryCacheSize)).diskCacheSize((int) diskCacheSize).diskCache(new UnlimitedDiscCache(cacheDir)).imageDecoder(new BaseImageDecoder(true)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(BaseApplication.getInstance())).build());
        PIC_DEFAULT = AppMode.getPicErrorImageResId();
    }

    public static void init() {
    }

    public static void showImageWithUrl(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView, defaultOption);
    }

    public static void showImageWithUrl(String url, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public static void showImageWithUrl(String url, ImageView imageView, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, imageView, defaultOption, listener);
    }

    public static void showImageWithUrl(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
    }

    public static void showRoundedImageWithUrl(String url, ImageView imageView, int roundPixels) {
        ImageLoader.getInstance().displayImage(url, imageView, new Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(roundPixels)).build());
    }

    public static void showRoundedImageWithUrl(String url, ImageView imageView, boolean cacheInMemory, boolean cacheOnDisk, int roundPixels) {
        ImageLoader.getInstance().displayImage(url, imageView, new Builder().cacheOnDisk(cacheOnDisk).cacheInMemory(cacheInMemory).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(roundPixels)).build());
    }

    public static void showRoundedImageWithUrl(String url, ImageView imageView, int roundPixels, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, imageView, new Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Config.RGB_565).displayer(new RoundedBitmapDisplayer(roundPixels)).build(), listener);
    }

    public static String getCacheImagePath(String url) {
        return ImageLoader.getInstance().getDiskCache().get(url).getPath();
    }

    public static File getCacheImageFile(String url) {
        return ImageLoader.getInstance().getDiskCache().get(url);
    }

    public static boolean saveCache(String url, Bitmap bitmap) {
        try {
            return ImageLoader.getInstance().getDiskCache().save(url, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getBitmapCache(String url) {
        List<Bitmap> values = MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache());
        if (values.size() > 0) {
            return (Bitmap) values.get(0);
        }
        return null;
    }
}
