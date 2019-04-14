package com.niuza.android.module;

import android.text.TextUtils;
import com.niuza.android.AppMode;
import com.niuza.android.module.account.LoginManager;

public class UrlConstance {
//    public static String getAPIHost() {
//        if (appMode == 1) {
//            return "app2.niuza.com/m/member";
//        }
//        if (appMode == 2) {
//            return "appp.ruyig.com";
//        }
//        return "appp.kiees.com";
//    }
    private static String getHost() {
        return "https://" + AppMode.getAPIHost() + "/88344365.php";
    }

    private static String appCommonParamsToUrl(String url) {
        return url + "&" + "client=android";
    }

    public static String getRecommendUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=recommend");
    }

    public static String getRankUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=rank");
    }

    public static String getAllCategoryUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=categoryall");
    }

    public static String getCategoryDataUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=category");
    }

    public static String getSearchUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=search");
    }

    public static String getProductDetailUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=detail");
    }

    public static String getProductNewsTextUrl() {
        return appCommonParamsToUrl(getHost() + "?" + "action=newstext");
    }

    public static String appendLoginParamsToUrl(String url) {
        if (!LoginManager.getInstance().isLogined()) {
            return url;
        }
        url = url + "?token=" + LoginManager.getInstance().getToken();
        if (TextUtils.isEmpty(LoginManager.getInstance().getOpenId())) {
            return url;
        }
        return url + "&openid=" + LoginManager.getInstance().getOpenId();
    }

    public static String getLoginUrl() {
        return AppMode.getAccountHost() + "/m/member/applogin.php";
    }

    public static String getLogoutUrl() {
        return appCommonParamsToUrl(appendLoginParamsToUrl(AppMode.getAccountHost() + "/m/member/applogout.php"));
    }

    public static String getUserInfoUrl() {
        return appCommonParamsToUrl(appendLoginParamsToUrl(AppMode.getAccountHost() + "/m/member/appuserinfo.php"));
    }

    public static String getAccountCenterUrl() {
        return appCommonParamsToUrl(appendLoginParamsToUrl(AppMode.getAccountHost() + "/m/member/index.php"));
    }

    public static String getForgetPsdUrl() {
        return AppMode.getAccountHost() + "/m/member/forgetpwd.php";
    }

    public static String getRegisterUrl() {
        return AppMode.getAccountHost() + "/m/member/reg.php";
    }

    public static String getFeedbackUrl() {
        return appCommonParamsToUrl(appendLoginParamsToUrl(AppMode.getAccountHost() + "/m/member/appfeedback.php"));
    }

    public static String getCommentUrl() {
        return appCommonParamsToUrl(appendLoginParamsToUrl(AppMode.getAccountHost() + "/m/member/apppl.php"));
    }
}
