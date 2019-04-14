package com.xunlei.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
//import mtopsdk.common.util.SymbolExpUtil;

public abstract class NetHelper {
    public static boolean isWifi(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo wifiInfo = cm.getNetworkInfo(1);
        if (wifiInfo == null || !wifiInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static boolean isWifiConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo wifiInfo = cm.getNetworkInfo(1);
        if (wifiInfo == null || !wifiInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static boolean isMobileNet(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo mobileInfo = cm.getNetworkInfo(0);
        if (mobileInfo == null || !mobileInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        NetworkInfo[] info = cm.getAllNetworkInfo();
        if (info == null) {
            return false;
        }
        for (NetworkInfo state : info) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getNetTypeName(Context context) {
        String typeName = "null";
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return "null";
        }
        if (info.getTypeName() == null) {
            return typeName;
        }
        typeName = info.getTypeName().toLowerCase();
        if (typeName.equals("wifi")) {
            return typeName;
        }
        if (info.getExtraInfo() != null) {
            typeName = info.getExtraInfo().toLowerCase();
        }
        if (!typeName.equals("#777") || info.getSubtypeName() == null) {
            return typeName;
        }
        return info.getSubtypeName();
    }

    public static String getNetTypeNameEx(Context context) {
        String type = "null";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mobileInfo = cm.getNetworkInfo(0);
        NetworkInfo wifiInfo = cm.getNetworkInfo(1);
        if (mobileInfo != null && mobileInfo.isConnected()) {
            switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    type = "2g";
                    break;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    type = "3g";
                    break;
                case 13:
                    type = "4g";
                    break;
                default:
                    type = "null";
                    break;
            }
        }
        if (wifiInfo == null || !wifiInfo.isConnected()) {
            return type;
        }
        return "wifi";
    }

    public static int getNetType(Context context) {
        int type = -1;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mobileInfo = cm.getNetworkInfo(0);
        NetworkInfo wifiInfo = cm.getNetworkInfo(1);
        if (mobileInfo != null && mobileInfo.isConnected()) {
            type = 0;
        }
        if (wifiInfo == null || !wifiInfo.isConnected()) {
            return type;
        }
        return 1;
    }

    public static int getCurrentNetType(Context ctx) {
        NetworkInfo ni = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        return ni == null ? -1 : ni.getType();
    }

    public static String getNetworkSubType(int subType) {
        switch (subType) {
            case 1:
            case 2:
            case 4:
                return "2g";
            case 3:
            case 5:
            case 6:
            case 8:
                return "3g";
            default:
                return "other";
        }
    }

    public static String getIPAddress(Context context) throws SocketException {
        String ret = getIPAddressPrivate(context);
        if (ret != null) {
            return ret;
        }
        Enumeration<NetworkInterface> en;
        Enumeration<InetAddress> enumIpAddr;
        InetAddress inetAddress;
        if (VERSION.SDK_INT >= 14) {
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } else {
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        return null;
    }

    private static String getIPAddressPrivate(Context context) {
        DhcpInfo dhcp = ((WifiManager) context.getSystemService("wifi")).getDhcpInfo();
        if (dhcp == null) {
            return null;
        }
        int ipAddress = dhcp.ipAddress;
        return (ipAddress & 255) + "." + ((ipAddress >> 8) & 255) + "." + ((ipAddress >> 16) & 255) + "." + ((ipAddress >> 24) & 255);
    }

    public static String getSsid(Context ctx) {
        WifiManager mWifiManager = (WifiManager) ctx.getSystemService("wifi");
        if (mWifiManager.getConnectionInfo() != null) {
            return mWifiManager.getConnectionInfo().getSSID();
        }
        return null;
    }

    public static boolean isSSIDSame(Context context, String ssid) {
        if (context == null || ssid == null) {
            if (ssid == null || ssid.equals("")) {
                return true;
            }
            return false;
        } else if (ssid == null || ssid.equals("")) {
            return true;
        } else {
            return ssid.equals(getSsid(context));
        }
    }

    public static boolean isNetSame(Context context, String PCIP) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        if (mWifiManager != null) {
            DhcpInfo dhcp = mWifiManager.getDhcpInfo();
            if (dhcp != null) {
                int networkIP = dhcp.ipAddress;
                int networkMask = dhcp.netmask;
                int networkId = networkIP & networkMask;
                int pcaddress = ConvertUtil.ipAddrToInt(PCIP);
                if (pcaddress != 0 && (pcaddress & networkMask) == networkId) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }
}
