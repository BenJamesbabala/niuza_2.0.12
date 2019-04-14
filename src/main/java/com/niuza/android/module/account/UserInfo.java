package com.niuza.android.module.account;

import android.text.TextUtils;
//import com.alibaba.wireless.security.open.nocaptcha.INoCaptchaComponent;
import com.niuza.android.module.AppSharePref;
import org.json.JSONObject;

public class UserInfo {
    private String headImageUrl = "";
    private String nickName = "";
    private String openId = "";
    private int score = 0;
    private String token;

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadImageUrl() {
        return this.headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("openid", this.openId);
//            jsonObject.put(INoCaptchaComponent.token, this.token);
            jsonObject.put("nickname", this.nickName);
            jsonObject.put("score", this.score);
            jsonObject.put("headurl", this.headImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public void initWithJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.openId = jsonObject.optString("openid");
//            this.token = jsonObject.optString(INoCaptchaComponent.token);
            this.nickName = jsonObject.optString("nickname");
            this.score = jsonObject.optInt("score");
            this.headImageUrl = jsonObject.optString("headurl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserInfo(UserInfo info) {
        AppSharePref.getInstance().saveString("user_info", info.toJson());
    }

    public static UserInfo restoreUserInfo() {
        String userString = AppSharePref.getInstance().getString("user_info");
        if (TextUtils.isEmpty(userString)) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.initWithJson(userString);
        return userInfo;
    }

    public static void clearUserInfo() {
        AppSharePref.getInstance().saveString("user_info", "");
    }
}
