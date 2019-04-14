package com.niuza.android;

//import com.alibaba.baichuan.android.trade.AlibcMiniTradeSDK;
//import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
//import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
//import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.niuza.android.utils.ImageLoaderUtil;
import com.niuza.android.utils.ShareHandler;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import my.base.util.LogUtils;
import org.szuwest.lib.BaseApplication;

public class NZApplication extends BaseApplication {
    private RequestQueue mQueue;

    public void onCreate() {
        super.onCreate();
        LogUtils.enableLogging(false);
//        MobclickAgent.setDebugMode(false);
//        MobclickAgent.startWithConfigure(new UMAnalyticsConfig(this, AppMode.getUmengKey(), "universe"));
//        MobclickAgent.setCatchUncaughtExceptions(true);
        ImageLoaderUtil.init();
        this.mQueue = Volley.newRequestQueue(getApplicationContext());
        this.mQueue.start();
        ShareHandler.umengConfig();
//        try {
//            AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
//                public void onSuccess() {
//                    LogUtils.d("NZApplication", "alibaichuan init success");
//                    AlibcMiniTradeSDK.setTaokeParams(new AlibcTaokeParams(AppMode.taokeMMKey(), "", ""));
//                }
//
//                public void onFailure(int code, String msg) {
//                    LogUtils.e("NZApplication", "error=" + msg);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        com.wanjian.sak.SAK.init(NZApplication.getApp());
    }

    public static NZApplication getApp() {
        return (NZApplication) BaseApplication.getInstance();
    }

    public RequestQueue getNetQueue() {
        return this.mQueue;
    }
}
