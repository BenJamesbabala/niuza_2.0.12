package my.base.update;

import java.io.Serializable;

public class StatisticalInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String brand = "";
    private String cpuabi = "";
    private int height = 0;
    private String imei = "";
    private String model = "";
    private int sdk = 0;
    private String sysvel = "";
    private long usedTimes = 0;
    private int versionCode = 0;
    private String versionName = "";
    private int wap3_cid = 0;
    private int width = 0;

    public int getWap3_cid() {
        return this.wap3_cid;
    }

    public void setWap3_cid(int wap3_cid) {
        this.wap3_cid = wap3_cid;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
    }

    public int getSdk() {
        return this.sdk;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setUsedTimes(long usedTimes) {
        this.usedTimes = usedTimes;
    }

    public long getUsedTimes() {
        return this.usedTimes;
    }

    public void setModel(String mode) {
        this.model = mode;
    }

    public String getModel() {
        return this.model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setCpuabi(String cpuabi) {
        this.cpuabi = cpuabi;
    }

    public String getCpuabi() {
        return this.cpuabi;
    }

    public void setSysvel(String sysvel) {
        this.sysvel = sysvel;
    }

    public String getSysvel() {
        return this.sysvel;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return this.imei;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public String toString() {
        return "?brand=" + this.brand + "&cpuabi=" + this.cpuabi + "&model=" + this.model + "&sdk=" + this.sdk + "&height=" + this.height + "&width=" + this.width + "&sysvel=" + this.sysvel + "&usedTimes=" + this.usedTimes + "&versionCode=" + this.versionCode + "&versionName=" + this.versionName + "&imei=" + this.imei + "&wap3_cid=" + this.wap3_cid;
    }
}
