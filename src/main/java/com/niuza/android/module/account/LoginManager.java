package com.niuza.android.module.account;

import android.app.Activity;
import android.text.TextUtils;
//import com.alibaba.wireless.security.open.nocaptcha.INoCaptchaComponent;
import com.alipay.sdk.util.e;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.niuza.android.NZApplication;
import com.niuza.android.module.UrlConstance;
//import com.umeng.socialize.UMAuthListener;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
import java.util.HashMap;
import java.util.Map;
//import mtopsdk.mtop.antiattack.CheckCodeDO;
import my.base.util.LogUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginManager {
    private static final String TAG = LoginManager.class.getSimpleName();
    private static LoginManager instance = null;
    private int loginState = 0;
    private String openId;
    private String token;
    private UserInfo userInfo = UserInfo.restoreUserInfo();

    public interface LoginListener {
        void onLoginResult(int i, String str);
    }

    public interface LogoutListener {
        void logoutResult(int i, String str);
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    private LoginManager() {
        if (this.userInfo != null) {
            this.openId = this.userInfo.getOpenId();
            this.token = this.userInfo.getToken();
            if (!TextUtils.isEmpty(this.token)) {
                this.loginState = 2;
            }
        }
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

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public boolean isLogined() {
        return this.loginState == 2 && !TextUtils.isEmpty(getToken());
    }

    public boolean isLogining() {
        return this.loginState == 1;
    }

    public void loginWithUserNameAndPwd(String usrName, String password, LoginListener listener) {
        if (!isLogining()) {
            this.loginState = 1;
            HashMap<String, String> params = new HashMap(3);
            params.put("nickname", usrName);
            params.put("password", password);
            params.put("apptype", "web");
            login(params, listener);
        }
    }

    public void loginWithWeinxin(Activity activity, final LoginListener listener) {
//        if (!isLogining()) {
//            UMShareAPI.get(NZApplication.getApp()).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//                public void onStart(SHARE_MEDIA share_media) {
//                }
//
//                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                    LoginManager.this.loginState = 1;
//                    HashMap<String, String> params = new HashMap(4);
//                    params.put("nickname", map.get("name"));
//                    params.put("openid", map.get("uid"));
//                    LoginManager.this.openId = (String) map.get("uid");
//                    params.put("apptype", "weixin");
//                    params.put("headimgurl", map.get("iconurl"));
//                    LoginManager.this.login(params, listener);
//                }
//
//                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                    LogUtils.w(LoginManager.TAG, "error:" + throwable.getMessage());
//                    LoginManager.this.handleLoginError();
//                    if (listener != null) {
//                        listener.onLoginResult(-1, e.b);
//                    }
//                }
//
//                public void onCancel(SHARE_MEDIA share_media, int i) {
//                    if (listener != null) {
//                        listener.onLoginResult(-2, "cancel");
//                    }
//                }
//            });
//        }
    }

    public void loginWithQQ(Activity activity, final LoginListener listener) {
//        if (!isLogining()) {
//            UMShareAPI.get(NZApplication.getApp()).getPlatformInfo(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
//                public void onStart(SHARE_MEDIA platform) {
//                }
//
//                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//                    LoginManager.this.loginState = 1;
//                    HashMap<String, String> params = new HashMap(4);
//                    params.put("nickname", data.get("name"));
//                    params.put("openid", data.get("uid"));
//                    LoginManager.this.openId = (String) data.get("uid");
//                    params.put("apptype", "qq");
//                    params.put("headimgurl", data.get("iconurl"));
//                    LoginManager.this.login(params, listener);
//                }
//
//                public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//                    LogUtils.w(LoginManager.TAG, "error:" + t.getMessage());
//                    LoginManager.this.handleLoginError();
//                    if (listener != null) {
//                        listener.onLoginResult(-1, e.b);
//                    }
//                }
//
//                public void onCancel(SHARE_MEDIA platform, int action) {
//                    if (listener != null) {
//                        listener.onLoginResult(-2, "cancel");
//                    }
//                }
//            });
//        }
    }

    private void login(HashMap<String, String> params, final LoginListener listener) {
//        final HashMap<String, String> hashMap = params;
//        StringRequest stringRequest = new StringRequest(1, UrlConstance.getLoginUrl(), new Listener<String>() {
//            public void onResponse(String response) {
//                LogUtils.d(LoginManager.TAG, "response:" + response);
//                boolean success = false;
//                String msg = "unknown error";
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    int code = jsonObject.optInt(CheckCodeDO.CHECKCODE_USER_INPUT_KEY);
//                    LoginManager.this.token = jsonObject.optString(INoCaptchaComponent.token);
//                    msg = jsonObject.optString("msg");
//                    if (code == 0 && !TextUtils.isEmpty(LoginManager.this.token)) {
//                        success = true;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (success) {
//                    LoginManager.this.requestUserInfo();
//                    LoginManager.this.userInfo = new UserInfo();
//                    LoginManager.this.userInfo.setToken(LoginManager.this.token);
//                    LoginManager.this.userInfo.setOpenId(LoginManager.this.openId);
//                    UserInfo.saveUserInfo(LoginManager.this.userInfo);
//                    LoginManager.this.loginState = 2;
//                    if (listener != null) {
//                        listener.onLoginResult(0, "success");
//                        return;
//                    }
//                    return;
//                }
//                LoginManager.this.handleLoginError();
//                if (listener != null) {
//                    listener.onLoginResult(-1, msg);
//                }
//            }
//        }, new ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                LogUtils.w(LoginManager.TAG, "error:" + error.getMessage());
//                LoginManager.this.handleLoginError();
//                if (listener != null) {
//                    listener.onLoginResult(-1, error.getMessage());
//                }
//            }
//        }) {
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return hashMap;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        NZApplication.getApp().getNetQueue().add(stringRequest);
    }

    public void logout(final LogoutListener listener) {
        StringRequest stringRequest = new StringRequest(0, UrlConstance.getLogoutUrl(), new Listener<String>() {
            public void onResponse(String response) {
                LoginManager.this.handleLoginError();
                UserInfo.clearUserInfo();
                if (listener != null) {
                    listener.logoutResult(0, "success");
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.logoutResult(-1, error.getMessage());
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NZApplication.getApp().getNetQueue().add(stringRequest);
    }

    public void requestUserInfo() {
    }

    private void handleLoginError() {
        this.token = null;
        this.openId = null;
        this.loginState = 0;
        this.userInfo = null;
    }
}
