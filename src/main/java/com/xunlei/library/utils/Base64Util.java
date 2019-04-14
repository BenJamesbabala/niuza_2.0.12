package com.xunlei.library.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.JSONLexer;
import java.io.UnsupportedEncodingException;

public class Base64Util {
//    private static byte[] base64DecodeChars = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 62, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 58, (byte) 59, (byte) 60, (byte) 61, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12, o.k, o.l, o.m, o.n, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, JSONLexer.EOI, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1};
    private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String encode(byte[] data) {
        int i;
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i2 = 0;
        while (i2 < len) {
            i = i2 + 1;
            int b1 = data[i2] & 255;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 3) << 4]);
                sb.append("==");
                break;
            }
            i2 = i + 1;
            int b2 = data[i] & 255;
            if (i2 == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 3) << 4) | ((b2 & 240) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 15) << 2]);
                sb.append("=");
                i = i2;
                break;
            }
            i = i2 + 1;
            int b3 = data[i2] & 255;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 3) << 4) | ((b2 & 240) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 15) << 2) | ((b3 & Opcodes.CHECKCAST) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 63]);
            i2 = i;
        }
        i = i2;
        return sb.toString();
    }

    public static byte[] decode(String str) throws UnsupportedEncodingException {
//        StringBuffer sb = new StringBuffer();
//        byte[] data = str.getBytes("US-ASCII");
//        int len = data.length;
//        int i = 0;
//        while (i < len) {
//            int b2;
//            while (true) {
//                int b3;
//                int b4;
//                int i2 = i + 1;
//                int b1 = base64DecodeChars[data[i]];
//                if (i2 < len && b1 == -1) {
//                    i = i2;
//                } else if (b1 == -1) {
//                    i = i2;
//                    break;
//                } else {
//                    do {
//                        i = i2;
//                        i2 = i + 1;
//                        b2 = base64DecodeChars[data[i]];
//                        if (i2 >= len) {
//                            break;
//                        }
//                    } while (b2 == -1);
//                    if (b2 == -1) {
//                        i = i2;
//                        break;
//                    }
//                    sb.append((char) ((b1 << 2) | ((b2 & 48) >>> 4)));
//                    do {
//                        i = i2;
//                        i2 = i + 1;
//                        b3 = data[i];
//                        if (b3 == 61) {
//                            b3 = base64DecodeChars[b3];
//                            if (i2 >= len) {
//                                break;
//                            }
//                        } else {
//                            i = i2;
//                            return sb.toString().getBytes("iso8859-1");
//                        }
//                    } while (b3 == -1);
//                    if (b3 == -1) {
//                        i = i2;
//                        break;
//                    }
//                    sb.append((char) (((b2 & 15) << 4) | ((b3 & 60) >>> 2)));
//                    do {
//                        i = i2;
//                        i2 = i + 1;
//                        b4 = data[i];
//                        if (b4 == 61) {
//                            b4 = base64DecodeChars[b4];
//                            if (i2 >= len) {
//                                break;
//                            }
//                        } else {
//                            i = i2;
//                            return sb.toString().getBytes("iso8859-1");
//                        }
//                    } while (b4 == -1);
//                    if (b4 == -1) {
//                        i = i2;
//                        break;
//                    }
//                    sb.append((char) (((b3 & 3) << 6) | b4));
//                    i = i2;
//                }
//            }
//            if (b1 == -1) {
//                i = i2;
//                break;
//            }
//            do {
//                i = i2;
//                i2 = i + 1;
//                b2 = base64DecodeChars[data[i]];
//                if (i2 >= len) {
//                    break;
//                }
//                break;
//            } while (b2 == -1);
//            if (b2 == -1) {
//                i = i2;
//                break;
//            }
//            sb.append((char) ((b1 << 2) | ((b2 & 48) >>> 4)));
//            do {
//                i = i2;
//                i2 = i + 1;
//                b3 = data[i];
//                if (b3 == 61) {
//                    b3 = base64DecodeChars[b3];
//                    if (i2 >= len) {
//                        break;
//                    }
//                    break;
//                }
//                i = i2;
//                return sb.toString().getBytes("iso8859-1");
//            } while (b3 == -1);
//            if (b3 == -1) {
//                i = i2;
//                break;
//            }
//            sb.append((char) (((b2 & 15) << 4) | ((b3 & 60) >>> 2)));
//            do {
//                i = i2;
//                i2 = i + 1;
//                b4 = data[i];
//                if (b4 == 61) {
//                    b4 = base64DecodeChars[b4];
//                    if (i2 >= len) {
//                        break;
//                    }
//                    break;
//                }
//                i = i2;
//                return sb.toString().getBytes("iso8859-1");
//            } while (b4 == -1);
//            if (b4 == -1) {
//                i = i2;
//                break;
//            }
//            sb.append((char) (((b3 & 3) << 6) | b4));
//            i = i2;
//        }
//        return sb.toString().getBytes("iso8859-1");
        return  null;
    }

    public static String encode(String src, String encoding) {
        try {
            return encode(src.getBytes(encoding));
        } catch (Exception e) {
            return null;
        }
    }

    public static String decode(String src, String encoding) {
        try {
            return new String(decode(src), encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFromBase64(String s) {
        if (s == null) {
            return null;
        }
        try {
            byte[] bs = Base64.decode(s, 0);
            if (bs != null) {
                return new String(bs);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String xunleiBase64Decoder(String s) {
        String result = s;
        if (TextUtils.isEmpty(s)) {
            return result;
        }
        String code = new String(Base64.decode(s.substring("thunder://".length() + s.indexOf("thunder://")), 0));
        if (TextUtils.isEmpty(code) || code.length() <= 4) {
            return result;
        }
        return code.substring(2, code.length() - 2);
    }
}
