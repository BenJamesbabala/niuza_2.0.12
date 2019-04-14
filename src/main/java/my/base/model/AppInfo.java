package my.base.model;

import android.graphics.drawable.Drawable;
import java.io.File;

public class AppInfo {
    private File appFile = null;
    private String appName = "";
    private String fileName = "";
    private Drawable icon = null;
    private String installPath = "";
    private String packageName = "";
    private long size = 0;
    private int versionCode = 0;
    private String versionName = "";

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getInstallPath() {
        return this.installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public File getAppFile() {
        return this.appFile;
    }

    public void setAppFile(File appFile) {
        this.appFile = appFile;
    }
}
