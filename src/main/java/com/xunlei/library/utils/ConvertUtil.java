package com.xunlei.library.utils;

import java.math.BigDecimal;
//import mtopsdk.common.util.SymbolExpUtil;

public class ConvertUtil {
    private static final long BASE_B = 1;
    private static final long BASE_GB = 1073741824;
    private static final long BASE_KB = 1024;
    private static final long BASE_MB = 1048576;
    private static final long BASE_TB = 1099511627776L;
    public static final String UNIT_BIT = "B";
    public static final String UNIT_GB = "GB";
    public static final String UNIT_KB = "KB";
    public static final String UNIT_MB = "MB";
    public static final String UNIT_TB = "TB";

    public static class FileSize {
        public String mBaseUnit;
        public String mFileSizeStr;

        public FileSize(String fileSizeStr, String baseUnit) {
            this.mFileSizeStr = fileSizeStr;
            this.mBaseUnit = baseUnit;
        }
    }

    public static String byteConvert(long byteNum) {
        if (((double) byteNum) / 1.048576E9d >= 1.0d) {
            return Double.toString(new BigDecimal(((double) byteNum) / 1.073741824E9d).setScale(2, 1).doubleValue()) + UNIT_GB;
        } else if (((double) byteNum) / 1024000.0d >= 1.0d) {
            return Double.toString(new BigDecimal(((double) byteNum) / 1048576.0d).setScale(1, 1).doubleValue()) + UNIT_MB;
        } else if (((double) byteNum) / 1000.0d < 1.0d) {
            return byteNum + UNIT_BIT;
        } else {
            return Double.toString(new BigDecimal(((double) byteNum) / 1024.0d).setScale(1, 1).doubleValue()) + UNIT_KB;
        }
    }

    public static String byteConvert(long byteNum, boolean trim) {
        String res = byteConvert(byteNum);
        if (!trim || res.length() < 7) {
            return res;
        }
        String original = res;
        res = original.substring(0, 4);
        if (res.endsWith(".")) {
            res = res.substring(0, 3);
        }
        return res + original.substring(original.length() - 2, original.length());
    }

    public static int levelToScore(int level) {
        return ((level + 1) * 50) * (level + 4);
    }

    public static int scoreToLevel(int score) {
        int rank = 0;
        while (score >= (rank * 50) * (rank + 3)) {
            rank++;
        }
        return rank > 1 ? rank - 1 : 0;
    }

    public static long stringToLong(String string) {
        try {
            return Long.valueOf(string).longValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int stringToInt(String string) {
        try {
            return Integer.valueOf(string).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int ipAddrToInt(String IPAddress) {
        if (IPAddress == null || !IPAddress.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$")) {
            return 0;
        }
        String[] array = IPAddress.split("\\.");
        return ((Integer.parseInt(array[0]) | (Integer.parseInt(array[1]) << 8)) | (Integer.parseInt(array[2]) << 16)) | (Integer.parseInt(array[3]) << 24);
    }

    public static String[] convertSpeeds(long speed, int precision) {
        String[] ret = new String[2];
        String str = convertFileSize(speed, 0);
        String unit = str.substring(str.length() - 1);
        ret[0] = str.substring(0, str.lastIndexOf(unit));
        if (unit.equals(UNIT_BIT)) {
            ret[1] = unit + "/s";
        } else {
            ret[1] = unit + "B/s";
        }
        return ret;
    }

    public static String convertPercent(float value, int scale, String zeroStr) {
        value = new BigDecimal((double) (100.0f * value)).divide(new BigDecimal(1), scale, 4).floatValue();
        return Float.compare(value, (float) Math.pow(10.0d, (double) (-scale))) < 0 ? zeroStr : String.valueOf(value);
    }

    public static String convertFileSize(long file_size, int precision) {
        double fileSize = (double) file_size;
        long temp = file_size;
        int i = 0;
        long base = 1;
        String baseUnit = "M";
        while (temp / 1024 > 0) {
            temp /= 1024;
            i++;
            if (i == 4) {
                break;
            }
        }
        switch (i) {
            case 0:
                base = 1;
                baseUnit = UNIT_BIT;
                break;
            case 1:
                base = 1024;
                baseUnit = UNIT_KB;
                break;
            case 2:
                base = BASE_MB;
                baseUnit = UNIT_MB;
                break;
            case 3:
                base = BASE_GB;
                baseUnit = UNIT_GB;
                break;
            case 4:
                base = BASE_TB;
                baseUnit = UNIT_TB;
                break;
        }
        String fileSizeStr = Double.toString(new BigDecimal(fileSize).divide(new BigDecimal(base), precision, 4).doubleValue());
        if (precision == 0) {
            int indexMid = fileSizeStr.indexOf(46);
            if (-1 == indexMid) {
                return fileSizeStr + baseUnit;
            }
            return fileSizeStr.substring(0, indexMid) + baseUnit;
        }
        if (baseUnit.equals(UNIT_BIT)) {
            fileSizeStr = fileSizeStr.substring(0, fileSizeStr.indexOf(46));
        }
        if (baseUnit.equals(UNIT_KB)) {
            int pos = fileSizeStr.indexOf(46);
            if (pos != -1) {
                fileSizeStr = fileSizeStr.substring(0, pos + 2);
            } else {
                fileSizeStr = fileSizeStr + ".0";
            }
        }
        return fileSizeStr + baseUnit;
    }

    public static FileSize convertFileSizeComp(long file_size, int precision) {
        double fileSize = (double) file_size;
        long temp = file_size;
        int i = 0;
        long base = 1;
        String baseUnit = "M";
        while (temp / 1024 > 0) {
            temp /= 1024;
            i++;
            if (i == 4) {
                break;
            }
        }
        switch (i) {
            case 0:
                base = 1;
                baseUnit = UNIT_BIT;
                break;
            case 1:
                base = 1024;
                baseUnit = UNIT_KB;
                break;
            case 2:
                base = BASE_MB;
                baseUnit = UNIT_MB;
                break;
            case 3:
                base = BASE_GB;
                baseUnit = UNIT_GB;
                break;
            case 4:
                base = BASE_TB;
                baseUnit = UNIT_TB;
                break;
        }
        String fileSizeStr = Double.toString(new BigDecimal(fileSize).divide(new BigDecimal(base), precision, 4).doubleValue());
        if (precision == 0) {
            int indexMid = fileSizeStr.indexOf(46);
            if (-1 == indexMid) {
                return new FileSize(fileSizeStr, baseUnit);
            }
            return new FileSize(fileSizeStr.substring(0, indexMid), baseUnit);
        }
        if (baseUnit.equals(UNIT_BIT)) {
            fileSizeStr = fileSizeStr.substring(0, fileSizeStr.indexOf(46));
        }
        if (baseUnit.equals(UNIT_KB)) {
            int pos = fileSizeStr.indexOf(46);
            if (pos != -1) {
                fileSizeStr = fileSizeStr.substring(0, pos + 2);
            } else {
                fileSizeStr = fileSizeStr + ".0";
            }
        }
        return new FileSize(fileSizeStr, baseUnit);
    }

    public static String ipAddressToString(int addr) {
        StringBuffer buf = new StringBuffer(16);
        addr >>>= 8;
        addr >>>= 8;
        buf.append(addr & 255).append('.').append(addr & 255).append('.').append(addr & 255).append('.').append((addr >>> 8) & 255);
        return buf.toString();
    }

    public static int parseInt(String mEpisodeIdStr2) {
        int result = 0;
        if (mEpisodeIdStr2 == null) {
            return 0;
        }
        int i = 0;
        while (i < mEpisodeIdStr2.length()) {
            if (mEpisodeIdStr2.charAt(i) >= '0' && mEpisodeIdStr2.charAt(i) <= '9') {
                result = (result * 10) + (mEpisodeIdStr2.charAt(i) - 48);
            }
            i++;
        }
        return result;
    }

    public static int Str2Int(String strInt, int defaultValue) {
        if (strInt == null) {
            return defaultValue;
        }
        int result;
        try {
            result = Integer.parseInt(strInt.trim());
        } catch (NumberFormatException e) {
            result = defaultValue;
        }
        return result;
    }

    public static String byteConvertToSpeed(long byteNum, boolean trim) {
        String res;
        if (((double) byteNum) / 1.048576E9d >= 1.0d) {
            res = Double.toString(new BigDecimal(((double) byteNum) / 1.073741824E9d).setScale(2, 1).doubleValue()) + UNIT_GB;
        } else if (((double) byteNum) / 1024000.0d >= 1.0d) {
            res = Double.toString(new BigDecimal(((double) byteNum) / 1048576.0d).setScale(1, 1).doubleValue()) + UNIT_MB;
        } else if (((double) byteNum) / 1000.0d >= 1.0d) {
            res = Double.toString(new BigDecimal(((double) byteNum) / 1024.0d).setScale(1, 1).doubleValue()) + UNIT_KB;
        } else {
            res = byteNum + UNIT_BIT;
            trim = false;
        }
        if (!trim || res.length() < 7) {
            return res;
        }
        String original = res;
        res = original.substring(0, 4);
        if (res.endsWith(".")) {
            res = res.substring(0, 3);
        }
        return res + original.substring(original.length() - 2, original.length());
    }

    public static String ToSBC(String input) {
        char[] c = input.toCharArray();
        int i = 0;
        while (i < c.length) {
            if (c[i] == ' ') {
                c[i] = '　';
            } else if (c[i] < '' && c[i] > ' ') {
                c[i] = (char) (c[i] + 65248);
            }
            i++;
        }
        return new String(c);
    }

    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        int i = 0;
        while (i < c.length) {
            if (c[i] == '　') {
                c[i] = ' ';
            } else if (c[i] > '＀' && c[i] < '｟') {
                c[i] = (char) (c[i] - 65248);
            }
            i++;
        }
        return new String(c);
    }
}
