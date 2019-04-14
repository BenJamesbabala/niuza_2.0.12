package com.niuza.android.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
//import com.alibaba.alibclinkpartner.constants.open.ALPLinkKeyType;
//import com.alibaba.baichuan.android.trade.AlibcTrade;
//import com.alibaba.baichuan.trade.biz.core.usertracker.UserTrackerConstants;
import com.niuza.android.R;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.account.LoginManager;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.entity.ProductDetail;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.ProductDataManager;
import com.niuza.android.module.logic.detail.IProductDetailResponse;
import com.niuza.android.module.logic.detail.ProductDetailManager;
import com.niuza.android.module.logic.detail.ProductDetailManager.FeedbackListener;
import com.niuza.android.ui.account.LoginActivity;
import com.niuza.android.ui.common.NABaseActivity;
import com.niuza.android.ui.webview.WebViewActivity;
import com.niuza.android.utils.ImageLoaderUtil;
import com.niuza.android.utils.ShareHandler;
import com.niuza.android.utils.TimeUtil;
//import com.umeng.socialize.UMShareAPI;
import com.xunlei.library.pulltorefresh.ILoadingLayout;
import com.xunlei.library.pulltorefresh.PullToRefreshBase;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.xunlei.library.pulltorefresh.PullToRefreshScrollView;
import java.util.List;
import java.util.regex.Pattern;
import my.base.util.LogUtils;

public class DetailActivity extends NABaseActivity {
    public static final String TAG = DetailActivity.class.getSimpleName();
    private static Product mProduct;
    private static ProductDataManager productDataManager;
    private OnClickListener buyTap = new OnClickListener() {
        public void onClick(View v) {
            String buyUrl = DetailActivity.mProduct.buyAddr;
            if (DetailActivity.this.mProductDetail != null) {
                buyUrl = DetailActivity.this.mProductDetail.buyAddr;
            }
            if (TextUtils.isEmpty(buyUrl)) {
                WebViewActivity.gotWebView(DetailActivity.this, DetailActivity.mProduct.titleUrl, DetailActivity.mProduct.title);
                return;
            }
//            if (buyUrl.contains("taobao") || buyUrl.contains(ALPLinkKeyType.TMALL)) {
            if (buyUrl.contains("taobao")) {
                int ret;
                try {
                    if (DetailActivity.this.mProductDetail != null) {
                        ret = OpenHandler.openProduct(DetailActivity.this, DetailActivity.this.mProductDetail);
                    } else {
                        ret = OpenHandler.openAliProductUrl(DetailActivity.this, buyUrl);
                    }
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "无法拉起淘宝APP", 0).show();
                    e.printStackTrace();
                    ret = -1;
                }
                if (ret >= 0) {
                    return;
                }
            }
            WebViewActivity.gotWebView(DetailActivity.this, buyUrl, DetailActivity.mProduct.title);
        }
    };
    private OnClickListener commentTap = new OnClickListener() {
        public void onClick(View v) {
            if (LoginManager.getInstance().isLogined()) {
                WebViewActivity.goToWebView(DetailActivity.this, UrlConstance.getCommentUrl() + "&id=" + DetailActivity.this.mProductDetail.id, "评论", false, false);
                return;
            }
            LoginActivity.showLoginDialog(DetailActivity.this);
        }
    };
//    final String encoding = SymbolExpUtil.CHARSET_UTF8;
final String encoding = "utf-8";
    private OnClickListener feedbackTap = new OnClickListener() {
        public void onClick(View v) {
            if (LoginManager.getInstance().isLogined()) {
                DetailActivity.this.showLoadingDialog("反馈中..");
                ProductDetailManager.getInstance().feedback(DetailActivity.this.mProductDetail, new FeedbackListener() {
                    public void onFeedbackResult(int code, String msg) {
                        DetailActivity.this.dismissDialog();
                        if (code == 0) {
                            Toast.makeText(DetailActivity.this, "谢谢您的反馈，小编会尽快检查商品情况！", 1).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "反馈失败,请稍后再试", 0).show();
                        }
                    }
                });
                return;
            }
            LoginActivity.showLoginDialog(DetailActivity.this);
        }
    };
    private int from = 0;
    private Button mBuyBtn;
    private ProductDetailManager mDetailManager = new ProductDetailManager();
    private TextView mDetailTextView;
    private WebView mDetailWebView;
    private TextView mHotTextView;
    private ImageView mImageView;
    private LinearLayout mOthersLayout;
    private ProductDetail mProductDetail;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private TextView mTimeTextView;
    private TextView mTitleView;
    final String mimeType = "text/html";
    private Button quanBtn;
    private OnClickListener quanTap = new OnClickListener() {
        public void onClick(View v) {
            if (OpenHandler.openAliProductUrl(DetailActivity.this, DetailActivity.this.mProductDetail.quanUrl) < 0) {
                WebViewActivity.gotWebView(DetailActivity.this, DetailActivity.this.mProductDetail.quanUrl, DetailActivity.this.mProductDetail.quanTitle);
            }
        }
    };
    private OnRefreshListener2<ScrollView> refreshListener2 = new OnRefreshListener2<ScrollView>() {
        public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            Product preP = DetailActivity.productDataManager.getPreProductFrom(DetailActivity.mProduct);
            ILoadingLayout layout = refreshView.getLoadingLayoutProxy(true, false);
            String countMsg = "上一条商品";
            if (preP == null) {
                countMsg = "到顶了";
                refreshView.onRefreshComplete();
            } else {
                DetailActivity.mProduct = preP;
                DetailActivity.this.fillProduct();
                DetailActivity.this.getDetail();
            }
            layout.setPullLabel(countMsg);
            layout.setRefreshingLabel(countMsg);
            layout.setReleaseLabel(countMsg);
        }

        public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            ILoadingLayout layout = refreshView.getLoadingLayoutProxy(false, true);
            String countMsg = "下一条商品";
            Product p = DetailActivity.productDataManager.getNextProductFrom(DetailActivity.mProduct);
            if (p != null) {
                DetailActivity.mProduct = p;
                DetailActivity.this.fillProduct();
                DetailActivity.this.getDetail();
                if (DetailActivity.productDataManager.indexOf(DetailActivity.mProduct) > DetailActivity.productDataManager.getDataList().size() - 5) {
                    DetailActivity.productDataManager.loadMoreData(null);
                }
            } else if (DetailActivity.productDataManager.hasMoreData()) {
                DetailActivity.productDataManager.loadMoreData(new IDataResponse<Product>() {
                    public void onResponse(int code, String msg, List<Product> list) {
                        if (code != 0) {
                            Toast.makeText(DetailActivity.this, "加载失败", 0).show();
                            DetailActivity.this.mPullToRefreshScrollView.onRefreshComplete();
                            return;
                        }
                        Product p = DetailActivity.productDataManager.getNextProductFrom(DetailActivity.mProduct);
                        if (p != null) {
                            DetailActivity.mProduct = p;
                            DetailActivity.this.getDetail();
                        }
                    }
                });
            } else {
                countMsg = "到底了";
                refreshView.onRefreshComplete();
            }
            layout.setPullLabel(countMsg);
            layout.setRefreshingLabel(countMsg);
            layout.setReleaseLabel(countMsg);
        }
    };

    public static void goToDetail(Context context, Product product) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("from", 1);
        mProduct = product;
        context.startActivity(i);
    }

    public static void goToDetail(Context context, Product product, ProductDataManager productDataManager) {
        DetailActivity.productDataManager = productDataManager;
        Intent i = new Intent(context, DetailActivity.class);
        mProduct = product;
        i.putExtra("from", 2);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.from = getIntent().getIntExtra("from", 0);
        if (mProduct == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);
        initView();
        showLoadingDialog("正在加载数据...");
        getDetail();
    }

    private void initView() {
        this.mImageView = (ImageView) findViewById(R.id.productImage);
        this.mTitleView = (TextView) findViewById(R.id.titleText);
        this.mTimeTextView = (TextView) findViewById(R.id.timeText);
        this.mHotTextView = (TextView) findViewById(R.id.hotText);
        this.mDetailTextView = (TextView) findViewById(R.id.detailText);
        this.mDetailWebView = (WebView) findViewById(R.id.detailWebView);
        WebSettings webseting = this.mDetailWebView.getSettings();
        webseting.setJavaScriptEnabled(true);
        webseting.setDefaultTextEncodingName("utf-8");
        webseting.setLoadsImagesAutomatically(true);
        webseting.setAllowContentAccess(true);
        webseting.setUseWideViewPort(true);
        webseting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webseting.setLoadWithOverviewMode(true);
        if (VERSION.SDK_INT >= 21) {
            webseting.setMixedContentMode(0);
        }
        this.mDetailWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.d(DetailActivity.TAG, "shouldOverrideUrlLoading url=" + url);
                if (url.equals(DetailActivity.this.getNewstextUrl())) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                if (url.contains("taobao.com/shop/coupon.htm") && OpenHandler.openAliProductUrl(DetailActivity.this, url) >= 0) {
                    return true;
                }
                WebViewActivity.gotWebView(DetailActivity.this, url, DetailActivity.this.mProductDetail.title);
                return true;
            }

            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                LogUtils.d(DetailActivity.TAG, "shouldInterceptRequest url=" + url);
                return super.shouldInterceptRequest(view, url);
            }
        });
        this.mBuyBtn = (Button) findViewById(R.id.buyBtn);
        this.mBuyBtn.setOnClickListener(this.buyTap);
        this.quanBtn = (Button) findViewById(R.id.quanBtn);
        this.quanBtn.setOnClickListener(this.quanTap);
        this.quanBtn.setVisibility(View.GONE);
        findViewById(R.id.feedbackBtn).setOnClickListener(this.feedbackTap);
        findViewById(R.id.commentBtn).setOnClickListener(this.commentTap);
        fillProduct();
        this.mOthersLayout = (LinearLayout) findViewById(R.id.othersLayout);
        this.mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        if (productDataManager == null || this.from != 2) {
            this.mPullToRefreshScrollView.setMode(Mode.DISABLED);
        } else if (productDataManager.indexOf(mProduct) == -1) {
            this.mPullToRefreshScrollView.setMode(Mode.DISABLED);
        } else {
            this.mPullToRefreshScrollView.setMode(Mode.BOTH);
            this.mPullToRefreshScrollView.setOnRefreshListener(this.refreshListener2);
            ILoadingLayout layout = this.mPullToRefreshScrollView.getLoadingLayoutProxy(true, false);
            String countMsg = "上一条商品";
            layout.setPullLabel(countMsg);
            layout.setRefreshingLabel(countMsg);
            layout.setReleaseLabel(countMsg);
            layout = this.mPullToRefreshScrollView.getLoadingLayoutProxy(false, true);
            countMsg = "下一条商品";
            layout.setPullLabel(countMsg);
            layout.setRefreshingLabel(countMsg);
            layout.setReleaseLabel(countMsg);
        }
    }

    private void getDetail() {
        this.mDetailManager.requestDetail(mProduct, new IProductDetailResponse() {
            public void onResponse(int code, String msg, ProductDetail productDetail) {
                DetailActivity.this.dismissDialog();
                if (DetailActivity.this.mPullToRefreshScrollView != null) {
                    DetailActivity.this.mPullToRefreshScrollView.onRefreshComplete();
                }
                if (code != 0 || productDetail == null) {
                    Toast.makeText(DetailActivity.this, "获取商品信息失败", 0).show();
                    return;
                }
                DetailActivity.this.mProductDetail = productDetail;
                DetailActivity.this.fillDetailData();
            }
        });
    }

    private void fillProduct() {
        this.mImageView.setVisibility(View.GONE);
        this.mTitleView.setText(mProduct.title);
        setHotInfo(mProduct);
        this.mDetailWebView.setVisibility(View.GONE);
    }

    private boolean shouldShowWebView() {
        if (!this.mProductDetail.originalDetaiInfo.contains("<a href=") && this.mProductDetail.originalDetaiInfo.indexOf("<img src=") == this.mProductDetail.originalDetaiInfo.lastIndexOf("<img src=")) {
            return false;
        }
        return true;
    }

    private String getNewstextUrl() {
        return UrlConstance.getProductNewsTextUrl() + "&id=" + this.mProductDetail.id;
    }

    private void setHotInfo(Product product) {
        String timeString = TimeUtil.getTimeString(product.hotTime);
        this.mTimeTextView.setText(timeString);
        this.mHotTextView.setText("人气：" + product.hot + "°C");
        setTitle(Html.fromHtml("<font size=\"3\" color=\"#666666\">" + (timeString + " | " + "人气：" + product.hot + "°C") + "</font>"));
    }

    private void fillDetailData() {
        if (!TextUtils.isEmpty(this.mProductDetail.heyiUrl)) {
            this.quanBtn.setVisibility(View.GONE);
            this.mBuyBtn.setVisibility(View.VISIBLE);
            this.mBuyBtn.setText("领券购买");
        } else if (TextUtils.isEmpty(this.mProductDetail.buyAddr)) {
            this.quanBtn.setVisibility(View.GONE);
            this.mBuyBtn.setVisibility(View.INVISIBLE);
        } else {
            this.mBuyBtn.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(this.mProductDetail.quanUrl)) {
                this.quanBtn.setVisibility(View.GONE);
                this.mBuyBtn.setText("购买链接");
            } else {
                this.quanBtn.setVisibility(View.VISIBLE);
                this.mBuyBtn.setText("再购买");
            }
        }
        this.mTitleView.setText(this.mProductDetail.title);
        if (shouldShowWebView()) {
            this.mImageView.setVisibility(View.GONE);
            this.mDetailWebView.setVisibility(View.VISIBLE);
            this.mDetailWebView.loadUrl(getNewstextUrl());
            this.mDetailTextView.setVisibility(View.GONE);
            this.mPullToRefreshScrollView.requestLayout();
        } else {
            this.mImageView.setVisibility(View.VISIBLE);
            this.mDetailTextView.setVisibility(View.VISIBLE);
            String newsText = Pattern.compile("\t|\r|\n").matcher(this.mProductDetail.detailInfo).replaceAll("");
            if (newsText.contains(" ￼ ")) {
                newsText = newsText.replace(" ￼ ", "");
            }
            this.mDetailTextView.setText(newsText);
            this.mDetailWebView.setVisibility(View.GONE);
            ImageLoaderUtil.showImageWithUrl(this.mProductDetail.imgUrl, this.mImageView);
        }
        setHotInfo(this.mProductDetail);
        List<Product> othersProduct = this.mProductDetail.others;
        if (othersProduct == null || othersProduct.size() == 0) {
            this.mOthersLayout.setVisibility(View.GONE);
            return;
        }
        this.mOthersLayout.setVisibility(View.VISIBLE);
        LinearLayout otherItemLayout1 = (LinearLayout) findViewById(R.id.otherItemLayout1);
        ImageView imageView = (ImageView) otherItemLayout1.findViewById(R.id.otherImageView1);
        ((TextView) otherItemLayout1.findViewById(R.id.otherTitleText1)).setText(((Product) othersProduct.get(0)).title);
        OnClickListener clickListener = new OnClickListener() {
            public void onClick(View v) {
                DetailActivity.goToDetail(DetailActivity.this, (Product) v.getTag());
            }
        };
        otherItemLayout1.setTag(othersProduct.get(0));
        otherItemLayout1.setOnClickListener(clickListener);
        ImageLoaderUtil.showImageWithUrl(((Product) othersProduct.get(0)).imgUrl, imageView);
        LinearLayout otherItemLayout2 = (LinearLayout) findViewById(R.id.otherItemLayout2);
        LinearLayout otherItemLayout3 = (LinearLayout) findViewById(R.id.otherItemLayout3);
        if (othersProduct.size() > 1) {
            imageView = (ImageView) otherItemLayout2.findViewById(R.id.otherImageView2);
            ((TextView) otherItemLayout2.findViewById(R.id.otherTitleText2)).setText(((Product) othersProduct.get(1)).title);
            ImageLoaderUtil.showImageWithUrl(((Product) othersProduct.get(1)).imgUrl, imageView);
            otherItemLayout2.setTag(othersProduct.get(1));
            otherItemLayout2.setOnClickListener(clickListener);
        } else {
            otherItemLayout2.setVisibility(View.INVISIBLE);
            otherItemLayout3.setVisibility(View.INVISIBLE);
        }
        if (othersProduct.size() > 2) {
            imageView = (ImageView) otherItemLayout3.findViewById(R.id.otherImageView3);
            ((TextView) otherItemLayout3.findViewById(R.id.otherTitleText3)).setText(((Product) othersProduct.get(2)).title);
            ImageLoaderUtil.showImageWithUrl(((Product) othersProduct.get(2)).imgUrl, imageView);
            otherItemLayout3.setTag(othersProduct.get(2));
            otherItemLayout3.setOnClickListener(clickListener);
            return;
        }
        otherItemLayout3.setVisibility(View.INVISIBLE);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.refreshListener2 = null;
        this.mProductDetail = null;
//        UMShareAPI.get(this).release();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_share) {
            return super.onOptionsItemSelected(item);
        }
        showSharePanel();
        return true;
    }

    private void showSharePanel() {
        ShareHandler.shareProduct(this.mProductDetail, this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
