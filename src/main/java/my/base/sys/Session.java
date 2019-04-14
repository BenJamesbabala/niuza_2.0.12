package my.base.sys;

import android.content.Context;

public class Session {
    private static final String MYPREFS = "mySharedPreferences";
    private Context context;

    public Session(Context context) {
        this.context = context;
    }

    public void setAttribute(String name, int value) {
    }

    public int getAttribute(String name, int defaultValue) {
        return defaultValue;
    }

    public void setAttribute(String name, String value) {
    }

    public String getAttribute(String name, String defaultValue) {
        return defaultValue;
    }

    public void setAttributeLocal(String name, int value) {
    }

    public int getAttributeLocal(String name, int defaultValue) {
        return defaultValue;
    }

    public void setAttributeLocal(String name, String value) {
    }

    public String getAttributeLocal(String name, String defaultValue) {
        return defaultValue;
    }
}
