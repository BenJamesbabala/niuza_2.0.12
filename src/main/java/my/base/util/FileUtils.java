package my.base.util;

import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
//import mtopsdk.common.util.SymbolExpUtil;

public class FileUtils {
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index + 1);
        }
        return "";
    }

    public static String getFileSizeReadable(long fileSize) {
        if (fileSize < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            return fileSize + "";
        }
        if (fileSize >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID && fileSize < 1048576) {
            return new StringBuilder(String.valueOf((long) (((double) fileSize) / 1024.0d))).append("K").toString();
        }
        double r;
        String temp3;
        String result;
        if (fileSize >= 1048576 && fileSize < 1073741824) {
            r = ((double) fileSize) / 1048576.0d;
            temp3 = new StringBuilder(String.valueOf(((double) ((int) r)) + (((double) ((int) (100.0d * (r - ((double) ((int) r)))))) * 0.01d))).toString();
            result = temp3;
            if (temp3.length() - temp3.lastIndexOf(".") > 3) {
                result = temp3.substring(0, temp3.lastIndexOf(".") + 3);
            }
            return new StringBuilder(String.valueOf(result)).append("M").toString();
        } else if (fileSize < 1073741824 || fileSize >= 1099511627776L) {
            return new StringBuilder(String.valueOf((long) (((double) fileSize) / 0.0d))).append("T").toString();
        } else {
            r = ((double) fileSize) / 1.073741824E9d;
            temp3 = new StringBuilder(String.valueOf(((double) ((int) r)) + (((double) ((int) (100.0d * (r - ((double) ((int) r)))))) * 0.01d))).toString();
            result = temp3;
            if (temp3.length() - temp3.lastIndexOf(".") > 3) {
                result = temp3.substring(0, temp3.lastIndexOf(".") + 3);
            }
            return new StringBuilder(String.valueOf(result)).append("G").toString();
        }
    }

    public static boolean hasSd() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long phoneStorageFree() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return (long) (stat.getAvailableBlocks() * stat.getBlockSize());
    }

    public static long phoneStorageUsed() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return (long) ((stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize());
    }

    public static long phoneStorageTotal() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return (long) (stat.getBlockCount() * stat.getBlockSize());
    }

    public static long sdCardFree() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) (stat.getAvailableBlocks() * stat.getBlockSize());
    }

    public static long sdCardUsed() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) ((stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize());
    }

    public static long sdCardTotal() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) (stat.getBlockCount() * stat.getBlockSize());
    }
}
