package my.base.util;

public class StringUtils {
    public static boolean isBlank(String s) {
        if (s == null || s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String s) {
        if (s == null || s.trim().length() == 0) {
            return false;
        }
        return true;
    }
}
