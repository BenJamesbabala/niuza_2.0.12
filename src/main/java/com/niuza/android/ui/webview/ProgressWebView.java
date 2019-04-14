package com.niuza.android.ui.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ProgressBar;

public class ProgressWebView extends WebView {
    private ProgressBar progressbar;

    public class WebChromeClient extends android.webkit.WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                ProgressWebView.this.progressbar.setVisibility(View.GONE);
            } else {
                if (ProgressWebView.this.progressbar.getVisibility() == 8) {
                    ProgressWebView.this.progressbar.setVisibility(View.VISIBLE);
                }
                ProgressWebView.this.progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.progressbar = new ProgressBar(context, null, 16842872);
        this.progressbar.setLayoutParams(new LayoutParams(-1, 3, 0, 0));
        addView(this.progressbar);
        setWebChromeClient(new WebChromeClient());
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) this.progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        this.progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
