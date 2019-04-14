package com.xunlei.library.utils;

import android.text.TextUtils;
import android.webkit.URLUtil;

public class StringUtil {
    static final byte[] HEX_DATA_MAP = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};

    public static String truncateString(String string, String postFix) {
        if (string.endsWith(postFix)) {
            return string.substring(0, string.length() - postFix.length());
        }
        return null;
    }

    public static String textFilter(String str) {
        if (str == null) {
            return null;
        }
        String reg = "^[a-zA-Z0-9\\s一-龥]";
        int length = str.length();
        StringBuffer sb = new StringBuffer(str.length());
        for (int i = 0; i < length; i++) {
            String c = str.substring(i, i + 1);
            if (!c.matches(reg)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getMainDomain(String URL) {
        int pos = URL.indexOf("://");
        if (pos <= 0) {
            pos = URL.indexOf("/", 1);
        } else {
            pos = URL.indexOf("/", pos + 3);
        }
        if (pos > 0) {
            return URL.substring(0, pos);
        }
        return URL;
    }

    public static String getDomainBefore(String URL, String flag) {
        int pos = URL.indexOf(flag);
        if (pos > 0) {
            return URL.substring(0, pos);
        }
        return URL;
    }

    public static String parseFileName(String url) {
        String PR_HTTP = "http://";
        String PR_HTTPS = "https://";
        String PR_FTP = "ftp://";
        String PR_THUNDER = "thunder://";
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")) {
            return URLUtil.guessFileName(url, null, null);
        }
        if (url.startsWith("thunder://")) {
            return URLUtil.guessFileName(Base64Util.getFromBase64(url.substring("thunder://".length())), null, null);
        }
        return null;
    }

    public static byte[] byte_to_hex(byte b) {
        byte[] buf = new byte[2];
        int h2 = b & 15;
        try {
            buf[0] = HEX_DATA_MAP[(b & 240) >> 4];
            buf[1] = HEX_DATA_MAP[h2];
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return buf;
    }

    public static byte[] bytes_to_hex(byte[] data, int len) {
        if (data == null || len <= 0) {
            return null;
        }
        byte[] hexData = new byte[(len * 2)];
        int i = 0;
        while (i < len) {
            try {
                byte[] buf = byte_to_hex(data[i]);
                hexData[i * 2] = buf[0];
                hexData[(i * 2) + 1] = buf[1];
                i++;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return hexData;
            }
        }
        return hexData;
    }

    public static int hex_char_value(byte c) {
        if (c >= (byte) 48 && c <= (byte) 57) {
            return c - 48;
        }
        if (c >= (byte) 65 && c <= (byte) 90) {
            return (c - 65) + 10;
        }
        if (c < (byte) 97 || c > (byte) 122) {
            return -1;
        }
        return (c - 97) + 10;
    }

    public static byte[] hex_to_bytes(byte[] hex, int len) {
        if (len % 2 != 0) {
            return null;
        }
        int nbytes = len / 2;
        byte[] buffer = new byte[nbytes];
        int i = 0;
        while (i < nbytes) {
            try {
                int h1 = hex_char_value(hex[i * 2]);
                if (h1 < 0) {
                    return null;
                }
                int h2 = hex_char_value(hex[(i * 2) + 1]);
                if (h2 < 0) {
                    return null;
                }
                buffer[i] = (byte) ((h1 << 4) | h2);
                i++;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return buffer;
            }
        }
        return buffer;
    }

    public static String stringArrayToString(String[] strarray, String splitkey) {
        if (strarray == null) {
            return null;
        }
        String str = "";
        for (int i = 0; i < strarray.length; i++) {
            str = String.format("%s%s%s", new Object[]{str, splitkey, strarray[i]});
        }
        return str;
    }

    public static String[] stringToStringArray(String string, String splitkey) {
        String[] strarray = string.split(splitkey);
        if (strarray == null || strarray.length < 1) {
            return null;
        }
        String[] strArr = new String[(strarray.length - 1)];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = strarray[i + 1];
        }
        return strArr;
    }
}
