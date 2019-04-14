package com.xunlei.library.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class XLLog {
    public static final int LOG_BOTH = 3;
    public static final int LOG_FILE = 2;
    public static final int LOG_LOGCAT = 1;
    private static final String LOG_NAME = "/log.txt";
    public static final int LOG_NO = 0;
    public static final String SD_FILE_PATH = "/xunlei/log";
    private static LogHandler mLogHandler = null;
    private static int mLogLevel = 1;
    private static SimpleDateFormat mSdf = null;
    private static boolean sEnable = true;

    private static class LogHandler extends Handler {
        private File mLogFile = null;
        private FileOutputStream mLogOutput = null;

        public LogHandler() {
            createLogFile();
        }

        public void handleMessage(Message msg) {
            if (this.mLogFile != null) {
                try {
                    if (this.mLogOutput == null) {
                        this.mLogOutput = new FileOutputStream(this.mLogFile, true);
                    }
                } catch (FileNotFoundException e) {
                    if (!createLogFile()) {
                        return;
                    }
                }
                if (this.mLogOutput != null) {
                    String content = ((String) msg.obj) + "\n\n";
                    if (content != null) {
                        byte[] logData = content.getBytes();
                        try {
                            this.mLogOutput.write(logData, 0, logData.length);
                        } catch (IOException e2) {
                            this.mLogOutput = null;
                        }
                    }
                }
            }
        }

        private boolean createLogFile() {
            if (XLLog.isExternalStorageAvailable()) {
                File dir = XLLog.getLogFileDirectory();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                this.mLogFile = new File(dir.getAbsolutePath() + XLLog.LOG_NAME);
                if (!this.mLogFile.exists()) {
                    try {
                        this.mLogFile.createNewFile();
                    } catch (IOException e) {
                        this.mLogFile = null;
                        return false;
                    }
                }
                return true;
            }
            this.mLogFile = null;
            return false;
        }
    }

    @Deprecated
    public static void log(String tag, String content) {
        if (!TextUtils.isEmpty(content) && content != null) {
            d(tag, content);
        }
    }

    private XLLog() {
    }

    public void setEnable(boolean enable) {
        sEnable = enable;
    }

    public static void i(String tag, String msg) {
        if (sEnable) {
            if (!((mLogLevel != 1 && mLogLevel != 3) || tag == null || msg == null)) {
                Log.i(tag, msg);
            }
            if (mLogLevel == 2 || mLogLevel == 3) {
                initFileHandler();
                if (tag != null && msg != null) {
                    sendHandlerMsg("Info", tag, msg);
                }
            }
        }
    }

    public static void d(String tag, String msg) {
        if (sEnable) {
            if (!((mLogLevel != 1 && mLogLevel != 3) || tag == null || msg == null)) {
                Log.d(tag, msg);
            }
            if (mLogLevel == 2 || mLogLevel == 3) {
                initFileHandler();
                if (tag != null && msg != null) {
                    sendHandlerMsg("Debug", tag, msg);
                }
            }
        }
    }

    public static void v(String tag, String msg) {
        if (sEnable) {
            if (!((mLogLevel != 1 && mLogLevel != 3) || tag == null || msg == null)) {
                Log.v(tag, msg);
            }
            if (mLogLevel == 2 || mLogLevel == 3) {
                initFileHandler();
                if (tag != null && msg != null) {
                    sendHandlerMsg("Verbose", tag, msg);
                }
            }
        }
    }

    public static void w(String tag, String msg) {
        if (sEnable) {
            if (!((mLogLevel != 1 && mLogLevel != 3) || tag == null || msg == null)) {
                Log.w(tag, msg);
            }
            if (mLogLevel == 2 || mLogLevel == 3) {
                initFileHandler();
                if (tag != null && msg != null) {
                    sendHandlerMsg("Warn", tag, msg);
                }
            }
        }
    }

    public static void e(String tag, String msg) {
        if (!((mLogLevel != 1 && mLogLevel != 3) || tag == null || msg == null)) {
            Log.e(tag, msg);
        }
        if (mLogLevel == 2 || mLogLevel == 3) {
            initFileHandler();
            if (tag != null && msg != null) {
                sendHandlerMsg("Error", tag, msg);
            }
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (throwable != null) {
            if (mLogLevel == 1 || mLogLevel == 3) {
                throwable.printStackTrace();
            }
            if (mLogLevel == 2 || mLogLevel == 3) {
                initFileHandler();
                for (StackTraceElement stack : new Throwable().getStackTrace()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("class:").append(stack.getClassName()).append(";line:").append(stack.getLineNumber());
                    Log.e(tag, sb.toString());
                }
            }
        }
    }

    public static void logHeapStats(String tag, Context context) {
        MemoryInfo sysMemInfo = new MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(sysMemInfo);
        Debug.MemoryInfo proMemInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(proMemInfo);
        long heapTotalSize = Debug.getNativeHeapSize();
        long heapAllocatedSize = Debug.getNativeHeapAllocatedSize();
        long heapFreeSize = Debug.getNativeHeapFreeSize();
        DecimalFormat df = new DecimalFormat("0.000");
        Log.d(tag, "heap_stats heap_size=" + df.format((double) (((float) heapTotalSize) / 1048576.0f)) + "M allocated=" + df.format((double) (((float) heapAllocatedSize) / 1048576.0f)) + "M free=" + df.format((double) (((float) heapFreeSize) / 1048576.0f)) + "M " + "memory_stats " + "memory_usage=" + df.format((double) (((float) proMemInfo.getTotalPss()) / 1024.0f)) + "M dalvik_usage=" + df.format((double) (((float) proMemInfo.dalvikPss) / 1024.0f)) + "M native_usage=" + df.format((double) (((float) proMemInfo.nativePss) / 1024.0f)) + "M other_usage=" + df.format((double) (((float) proMemInfo.otherPss) / 1024.0f)) + "M " + "system_stats " + "system_available=" + df.format((double) (((float) sysMemInfo.availMem) / 1048576.0f)) + "M");
    }

    public static void logStackTrace(String tag) {
        for (StackTraceElement s : (StackTraceElement[]) Thread.getAllStackTraces().get(Thread.currentThread())) {
            Log.d(tag, s.toString());
        }
    }

    private static void initFileHandler() {
        if (mLogHandler == null) {
            mLogHandler = new LogHandler();
        }
    }

    private static void sendHandlerMsg(String level, String tag, String content) {
        if (mSdf == null) {
            mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
        StringBuilder buf = new StringBuilder();
        buf.append(mSdf.format(Calendar.getInstance().getTime())).append(" [");
        buf.append("Thread-").append(Thread.currentThread().getId()).append("] ");
        buf.append(level.toUpperCase()).append(" ");
        buf.append(tag).append(" : ").append(content);
        Message msg = mLogHandler.obtainMessage();
        msg.obj = buf.toString();
        mLogHandler.sendMessage(msg);
    }

    private static boolean isExternalStorageAvailable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    private static File getLogFileDirectory() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + SD_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String getLogPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + SD_FILE_PATH;
    }
}
