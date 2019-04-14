package com.niuza.android.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.niuza.android.AppMode;
import com.niuza.android.NZApplication;
import com.niuza.android.module.entity.ProductDetail;
//import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;
//import com.umeng.socialize.shareboard.SnsPlatform;
//import com.umeng.socialize.utils.ShareBoardlistener;

public class ShareHandler {
    public static void umengConfig() {
//        UMShareAPI.get(NZApplication.getApp());
//        PlatformConfig.setWeixin(AppMode.weixinAppId(), AppMode.weixinSecret());
//        PlatformConfig.setQQZone(AppMode.qqAppId(), AppMode.qqSecret());
    }

    public static void shareProduct(final ProductDetail productDetail, final Activity activity) {
//        final ShareAction shareAction = new ShareAction(activity);
//        if (productDetail.taokouLing == null || productDetail.taokouLing.length() <= 0) {
//            UMWeb web = new UMWeb(productDetail.buyAddr);
//            web.setTitle(productDetail.title.toString());
//            web.setThumb(new UMImage((Context) activity, productDetail.imgUrl));
//            web.setDescription(productDetail.detailInfo.toString());
//            shareAction.withMedia(web);
//        } else {
//            shareAction.withText(productDetail.taokouLing);
//        }
//        shareAction.setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ);
//        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
//            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//                if (share_media == SHARE_MEDIA.QQ && productDetail.taokouLing != null && productDetail.taokouLing.length() > 0) {
//                    shareAction.withText("");
//                    UMWeb web = new UMWeb(productDetail.buyAddr);
//                    web.setTitle(productDetail.title.toString());
//                    web.setThumb(new UMImage(activity, productDetail.imgUrl));
//                    web.setDescription(productDetail.taokouLing);
//                    shareAction.withMedia(web);
//                }
//                shareAction.setPlatform(share_media);
//                shareAction.share();
//            }
//        });
//        shareAction.setCallback(new UMShareListener() {
//            public void onStart(SHARE_MEDIA share_media) {
//            }
//
//            public void onResult(SHARE_MEDIA share_media) {
//            }
//
//            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                Toast.makeText(activity, throwable.getMessage(), 0).show();
//            }
//
//            public void onCancel(SHARE_MEDIA share_media) {
//            }
//        });
//        shareAction.open();
    }
}
