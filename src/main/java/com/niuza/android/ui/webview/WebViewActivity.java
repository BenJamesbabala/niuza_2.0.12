package com.niuza.android.ui.webview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.niuza.android.R;
import com.niuza.android.ui.common.NABaseActivity;
import org.szuwest.view.XLToastDialog;

@SuppressLint({"SetJavaScriptEnabled"})
public class WebViewActivity extends NABaseActivity implements ObservableScrollViewCallbacks {
    public static final String TITLE = "title";
    public static final String URL = "url";
    private ImageButton backBtn;
    private ViewGroup bottomBar;
    private ImageButton browserBtn;
    private ImageButton forwardBtn;
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        public void onReceivedTitle(WebView view, String title) {
            WebViewActivity.this.onTitleChanged(title);
            super.onReceivedTitle(view, title);
        }

        public void onProgressChanged(WebView view, int newProgress) {
            WebViewActivity.this.setProgress(newProgress * 100);
        }
    };
    private ImageButton refreshBtn;
    private ViewGroup rootLayout;
    private boolean showBottomBar = true;
    private CharSequence titleString = null;
    private String urlString = null;
    private boolean useWebTitle = true;
    private ObservableWebView webView;
    private WebViewClient webViewClient = new WebViewClient() {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            WebViewActivity.this.onReceivedError(errorCode, description, failingUrl);
            WebViewActivity.this.hideLoading();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewActivity.this.onPageFinish(url);
            WebViewActivity.this.hideLoading();
            if (view.getTitle() == null || view.getTitle().length() > 0) {
                WebViewActivity.this.configureBtns();
            } else {
                WebViewActivity.this.configureBtns();
            }
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!WebViewActivity.this.isFinishing()) {
                WebViewActivity.this.onPageStart(url);
                WebViewActivity.this.showLoading();
                WebViewActivity.this.configureBtns();
            }
        }
    };
    private XLToastDialog xlToastDialog;

    public static void gotWebView(Context context, String url, CharSequence title) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("title", title);
        context.startActivity(i);
    }

    public static void goToWebView(Context context, String url, CharSequence title, boolean showBottomBar, boolean useWebTitle) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("title", title);
        i.putExtra("show_bottom_bar", showBottomBar);
        i.putExtra("useWebTitle", useWebTitle);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(2);
        super.onCreate(savedInstanceState);
        getWindow().setFeatureInt(2, -1);
        Intent i = getIntent();
        this.urlString = i.getStringExtra("url");
        this.titleString = i.getCharSequenceExtra("title");
        if (this.titleString == null) {
            this.titleString = "";
        }
        this.useWebTitle = i.getBooleanExtra("useWebTitle", true);
        this.showBottomBar = i.getBooleanExtra("show_bottom_bar", true);
        setContentView(R.layout.activity_webview);
        initView();
        setTitle(this.titleString);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.webView.setWebChromeClient(null);
        this.webView.setWebViewClient(null);
        this.webView.destroy();
        this.webView = null;
    }

    private void initWebView() {
        this.webView = (ObservableWebView) findViewById(R.id.webView);
        this.webView.setWebChromeClient(this.mWebChromeClient);
        this.webView.setWebViewClient(this.webViewClient);
        this.webView.setScrollBarStyle(0);
        this.webView.loadUrl(this.urlString);
        if (this.showBottomBar) {
            this.webView.setScrollViewCallbacks(this);
        }
        WebSettings webseting = this.webView.getSettings();
        webseting.setJavaScriptEnabled(true);
        webseting.setDomStorageEnabled(true);
        webseting.setAppCacheMaxSize(8388608);
        webseting.setAppCachePath(getApplicationContext().getDir("cache", 0).getPath());
        webseting.setAllowFileAccess(true);
        webseting.setAppCacheEnabled(true);
        webseting.setCacheMode(-1);
        webseting.setSupportZoom(true);
        webseting.setBuiltInZoomControls(true);
        webseting.setDisplayZoomControls(false);
        webseting.setLoadWithOverviewMode(true);
        webseting.setUseWideViewPort(true);
        if (VERSION.SDK_INT >= 21) {
            webseting.setMixedContentMode(0);
        }
    }

    private void initView() {
        this.rootLayout = (ViewGroup) findViewById(R.id.rootLayout);
        initWebView();
        this.bottomBar = (ViewGroup) findViewById(R.id.bottomBar);
        this.bottomBar.setVisibility(this.showBottomBar ? 0 : 8);
        this.backBtn = (ImageButton) findViewById(R.id.backBtn);
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WebViewActivity.this.webView.canGoBack()) {
                    WebViewActivity.this.webView.goBack();
                    WebViewActivity.this.forwardBtn.setEnabled(true);
                    return;
                }
                WebViewActivity.this.backBtn.setEnabled(false);
            }
        });
        this.forwardBtn = (ImageButton) findViewById(R.id.forwardBtn);
        this.forwardBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WebViewActivity.this.webView.canGoForward()) {
                    WebViewActivity.this.webView.goForward();
                    WebViewActivity.this.backBtn.setEnabled(true);
                    return;
                }
                WebViewActivity.this.forwardBtn.setEnabled(false);
            }
        });
        this.refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);
        this.refreshBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WebViewActivity.this.webView.reload();
            }
        });
        this.browserBtn = (ImageButton) findViewById(R.id.browserBtn);
        this.browserBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(WebViewActivity.this.webView.getUrl())) {
                    try {
                        Intent i = new Intent("android.intent.action.VIEW");
                        i.setData(Uri.parse(WebViewActivity.this.webView.getUrl()));
                        WebViewActivity.this.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public WebView getWebView() {
        return this.webView;
    }

    public void reload(String url) {
        if (url != null) {
            this.webView.loadUrl(url);
        } else {
            this.webView.loadUrl(this.urlString);
        }
    }

    private void configureBtns() {
        this.backBtn.setEnabled(this.webView.canGoBack());
        this.forwardBtn.setEnabled(this.webView.canGoForward());
    }

    private void showLoading() {
        if (this.xlToastDialog == null || !this.xlToastDialog.isShowing()) {
            this.xlToastDialog = new XLToastDialog(this, "正在加载网页...");
            this.xlToastDialog.show();
        }
    }

    private void hideLoading() {
        if (this.xlToastDialog != null) {
            this.xlToastDialog.dismiss();
        }
        this.xlToastDialog = null;
    }

    public void onPageStart(String url) {
    }

    public void onPageFinish(String url) {
    }

    public void onReceivedError(int errorCode, String description, String failingUrl) {
    }

    public void onTitleChanged(String title) {
        if (this.useWebTitle) {
            setTitle(title);
        }
    }

    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    public void onDownMotionEvent() {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
                this.bottomBar.clearAnimation();
                this.bottomBar.animate().translationYBy((float) this.bottomBar.getHeight()).setListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        WebViewActivity.this.bottomBar.setVisibility(View.GONE);
                    }

                    public void onAnimationCancel(Animator animation) {
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }
        } else if (scrollState == ScrollState.DOWN && !ab.isShowing()) {
            ab.show();
            this.bottomBar.setVisibility(View.VISIBLE);
            this.bottomBar.animate().translationYBy((float) (-this.bottomBar.getHeight())).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    WebViewActivity.this.bottomBar.setVisibility(View.VISIBLE);
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            }).start();
        }
    }
}
