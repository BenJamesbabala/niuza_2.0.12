package com.niuza.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.niuza.android.AppMode;
import com.niuza.android.R;
import org.szuwest.lib.BaseActivity;
import org.szuwest.utils.ScreenUtils;

public class SplashActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ScreenUtils.setScreenSize(this);
        TextView appText = (TextView) findViewById(R.id.hostText);
        appText.setText(AppMode.getHost());
        TextView appText2 = (TextView) findViewById(R.id.slogonText);
        appText2.setText(AppMode.getAppName() + "只收性价比");
        if (AppMode.appMode != 1) {
            appText.setTextColor(-1);
            appText2.setTextColor(-1);
        }
        ((ImageView) findViewById(R.id.splashImageView)).setImageResource(AppMode.getSplashImageResId());
        appText.postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.goToMain();
                SplashActivity.this.finish();
            }
        }, 1500);
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
