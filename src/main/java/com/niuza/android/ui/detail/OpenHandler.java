package com.niuza.android.ui.detail;

import android.app.Activity;
import android.text.TextUtils;
//import com.alibaba.baichuan.android.trade.AlibcTrade;
//import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
//import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
//import com.alibaba.baichuan.android.trade.model.OpenType;
//import com.alibaba.baichuan.android.trade.page.AlibcPage;
//import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
//import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.niuza.android.AppMode;
import com.niuza.android.module.entity.ProductDetail;
import java.util.HashMap;
import java.util.Map;

public class OpenHandler {
    public static int openProduct(Activity activity, ProductDetail product) {
        Map<String, String> exParams = new HashMap();
        String detailUrl = product.buyAddr;
        if (!TextUtils.isEmpty(product.heyiUrl)) {
            detailUrl = product.heyiUrl;
        }
//        return AlibcTrade.show(activity, new AlibcPage(detailUrl), new AlibcShowParams(OpenType.Native, false), new AlibcTaokeParams(AppMode.taokeMMKey(), "", ""), exParams, new AlibcTradeCallback() {
//            public void onTradeSuccess(AlibcTradeResult tradeResult) {
//            }
//
//            public void onFailure(int code, String msg) {
//            }
//        });
        return 0;
    }

    public static int openAliProductUrl(Activity activity, String productUrl) {
//        return AlibcTrade.show(activity, new AlibcPage(productUrl), new AlibcShowParams(OpenType.Native, false), new AlibcTaokeParams(AppMode.taokeMMKey(), "", ""), new HashMap(), new AlibcTradeCallback() {
//            public void onTradeSuccess(AlibcTradeResult tradeResult) {
//            }
//
//            public void onFailure(int code, String msg) {
//            }
//        });
        return 0;
    }
}
