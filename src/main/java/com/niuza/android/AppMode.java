package com.niuza.android;

public class AppMode {
    public static final int APP_MODE_KIEES = 3;
    public static final int APP_MODE_NIUZA = 1;
    public static final int APP_MODE_RUYI = 2;
    public static int appMode = 1;

    public static String getAppName() {
        if (appMode == 1) {
            return "牛杂网";
        }
        if (appMode == 2) {
            return "如意购";
        }
        return "发现值得买";
    }

    public static String getHost() {
        if (appMode == 1) {
            return "www.niuza.com";
        }
        if (appMode == 2) {
            return "www.ruyig.com";
        }
        return "www.kiees.com";
    }

    public static String getAPIHost() {
        if (appMode == 1) {
            return "app2.niuza.com/m/member";
        }
        if (appMode == 2) {
            return "appp.ruyig.com";
        }
        return "appp.kiees.com";
    }

    public static String getAccountHost() {
        if (appMode == 1) {
            return "https://app2.niuza.com";
        }
        if (appMode == 2) {
            return "appp.ruyig.com";
        }
        return "appp.kiees.com";
    }

    public static String getIntroduction() {
        if (appMode == 1) {
            return "        牛杂网是中立的网购推荐博客，用心甄选商品，在控制网购风险基础上向网友推荐高性价比商品。\n        如觉得牛杂网不错，请把 www.niuza.com 推荐给身边的朋友、同学、同事或网友，感谢支持！";
        }
        if (appMode == 2) {
            return "        〖如意购〗是网购分享平台，每天收集各商城高性价比网购商品：9.9包邮、网购促销、白菜价。\n        如觉得如意购不错，请把 www.ruyig.com 推荐给身边的朋友、同学、同事或网友，感谢！";
        }
        return "        发现值得买是中立的网购推荐平台，通过人工甄选出各商城的高性价比商品和促销活动攻略，让网友发现什么值得买。\n        如觉得〖发现值得买〗不错，请把 www.kiees.com 分享给朋友、同学、同事或网友，非常感谢！";
    }

    public static String getQQText() {
        if (appMode == 1) {
            return "27875037";
        }
        if (appMode == 2) {
            return "543209797";
        }
        return "28489037";
    }

    public static String getWeixinText() {
        if (appMode == 1) {
            return "niuzaw (备用渠道,平时不发消息)";
        }
        if (appMode == 2) {
            return "www.ruyig.com";
        }
        return "kiees-com";
    }

    public static String getEmailText() {
        if (appMode == 1) {
            return "kf@niuza.com";
        }
        if (appMode == 2) {
            return "kf@ruyig.com";
        }
        return "kf@kiees.com";
    }

    public static int getAboutLogoResId() {
        if (appMode == 2) {
            return R.drawable.logo_ruyig;
        }
        if (appMode == 3) {
            return R.drawable.logo_kiees;
        }
        return R.drawable.logo;
    }

    public static int getPicErrorImageResId() {
        if (appMode == 2) {
            return R.drawable.pic_default_ruyig;
        }
        if (appMode == 3) {
            return R.drawable.pic_default_kiees;
        }
        return R.drawable.pic_default;
    }

    public static int getEmptyViewImageResId() {
        if (appMode == 2) {
            return R.drawable.empty_image_ruyig;
        }
        if (appMode == 3) {
            return R.drawable.empty_image_kiees;
        }
        return R.drawable.empty_image;
    }

    public static int getSplashImageResId() {
        if (appMode == 2) {
            return R.drawable.splash_image_ruyig;
        }
        if (appMode == 3) {
            return R.drawable.splash_image_kiees;
        }
        return R.drawable.splash_image;
    }

    public static int getZhidingCategoryId() {
        if (appMode == 2) {
            return 17;
        }
        if (appMode == 3) {
            return 41639;
        }
        return 3211;
    }

    public static String weixinAppId() {
        if (appMode == 2) {
            return "wxbfcf25ef95982768";
        }
        if (appMode == 3) {
            return "wxbfcf25ef95982768";
        }
        return "wxbfcf25ef95982768";
    }

    public static String weixinSecret() {
        if (appMode == 2) {
            return "bc5370000f0534462a4b0c2364e00866";
        }
        if (appMode == 3) {
            return "bc5370000f0534462a4b0c2364e00866";
        }
        return "bc5370000f0534462a4b0c2364e00866";
    }

    public static String qqAppId() {
        if (appMode == 2) {
            return "101381193";
        }
        if (appMode == 3) {
            return "101381193";
        }
        return "101381193";
    }

    public static String qqSecret() {
        if (appMode == 2) {
            return "53e31e1c0ea3f986daf5f30d4c6db6fc";
        }
        if (appMode == 3) {
            return "53e31e1c0ea3f986daf5f30d4c6db6fc";
        }
        return "53e31e1c0ea3f986daf5f30d4c6db6fc";
    }

    public static String aliBaichuanKey() {
        if (appMode == 2) {
            return "23705380";
        }
        if (appMode == 3) {
            return "23705380";
        }
        return "23705380";
    }

    public static String taokeMMKey() {
        if (appMode == 2) {
            return "mm_33217035_12778067_48488286";
        }
        if (appMode == 3) {
            return "mm_33217035_12778067_48488286";
        }
        return "mm_33217035_13738401_55448930";
    }

    public static String getUmengKey() {
        if (appMode == 2) {
            return "56db9babe0f55a3455001359";
        }
        if (appMode == 3) {
            return "56db9bfee0f55a51b30017d5";
        }
        return "56aac8c967e58ee85a0001cb";
    }
}
