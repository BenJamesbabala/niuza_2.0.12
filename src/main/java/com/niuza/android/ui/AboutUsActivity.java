package com.niuza.android.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.niuza.android.AppMode;
import com.niuza.android.R;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.account.LoginManager;
import com.niuza.android.module.account.LoginManager.LogoutListener;
import com.niuza.android.ui.account.LoginActivity;
import com.niuza.android.ui.common.NABaseActivity;
import com.niuza.android.ui.webview.WebViewActivity;
import com.nostra13.universalimageloader.utils.StorageUtils;
//import com.umeng.socialize.common.SocializeConstants;
import com.xunlei.library.utils.ConvertUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import org.szuwest.lib.BaseApplication;
import org.szuwest.utils.Config;
//import org.szuwest.utils.FileSizeUtil;
import org.szuwest.utils.FileUtils;
import org.szuwest.view.CustomDialog;

public class AboutUsActivity extends NABaseActivity {
    private MenuItem accountMenuItem;
    private TextView cacheText;
    private Button logoutBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        setTitle("关于" + AppMode.getAppName());
        ((TextView) findViewById(R.id.copyright)).setText("Copyright © 2012-" + Calendar.getInstance().get(1));
        this.cacheText = (TextView) findViewById(R.id.cacheText);
        TextView versionName = (TextView) findViewById(R.id.versionNameText);
        try {
            versionName.setText(Config.getVerName(this, getApplication().getPackageName()));
        } catch (Exception e) {
            versionName.setText("2.0");
        }
        ((TextView) findViewById(R.id.aboutText)).setText(AppMode.getIntroduction());
        ((TextView) findViewById(R.id.email)).setText(AppMode.getEmailText());
        ((TextView) findViewById(R.id.QQText)).setText(AppMode.getQQText());
        TextView text = (TextView) findViewById(R.id.weixinText);
        if (AppMode.appMode == 2) {
            text.setVisibility(View.INVISIBLE);
            findViewById(R.id.weixinTitle).setVisibility(View.INVISIBLE);
        } else {
            text.setText(AppMode.getWeixinText());
        }
        ((TextView) findViewById(R.id.hostText)).setText(AppMode.getHost());
        ((ImageView) findViewById(R.id.aboutLogo)).setImageResource(AppMode.getAboutLogoResId());
        try {
            updateCache();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        findViewById(R.id.cache).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AboutUsActivity.this.showClearCacheDialog();
            }
        });
        this.logoutBtn = (Button) findViewById(R.id.logoutBtn);
        this.logoutBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AboutUsActivity.this.showLoadingDialog("正在退出登录...");
                LoginManager.getInstance().logout(new LogoutListener() {
                    public void logoutResult(int code, String msg) {
                        AboutUsActivity.this.dismissDialog();
                        if (code == 0) {
                            AboutUsActivity.this.logoutBtn.setVisibility(View.INVISIBLE);
                            if (AboutUsActivity.this.accountMenuItem != null) {
                                AboutUsActivity.this.accountMenuItem.setTitle("登录");
                                return;
                            }
                            return;
                        }
                        Toast.makeText(AboutUsActivity.this, "退出登录失败", 0).show();
                    }
                });
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (LoginManager.getInstance().isLogined()) {
            this.logoutBtn.setVisibility(View.VISIBLE);
            if (this.accountMenuItem != null) {
                this.accountMenuItem.setTitle("个人中心");
                return;
            }
            return;
        }
        this.logoutBtn.setVisibility(View.INVISIBLE);
        if (this.accountMenuItem != null) {
            this.accountMenuItem.setTitle("登录");
        }
    }

    private void updateCache() {
        final File cacheDir = StorageUtils.getCacheDirectory(BaseApplication.getInstance());
        if (cacheDir == null) {
            this.cacheText.setText("未知");
        } else {
//            new Thread(new Runnable() {
//                public void run() {
//                    final double cacheSize = FileSizeUtil.getFileOrFilesSize(cacheDir.getAbsolutePath(), 3);
//                    BaseApplication.getInstance().post(new Runnable() {
//                        public void run() {
//                            DecimalFormat df = new DecimalFormat("#.00");
//                            double cacheSize_ = cacheSize / 3.0d;
//                            if (cacheSize_ < 1.0d) {
//                                AboutUsActivity.this.cacheText.setText("0" + df.format(cacheSize_) + ConvertUtil.UNIT_MB);
//                            } else {
//                                AboutUsActivity.this.cacheText.setText(df.format(cacheSize_) + ConvertUtil.UNIT_MB);
//                            }
//                        }
//                    });
//                }
//            }).start();
        }
    }

    private void showClearCacheDialog() {
        CustomDialog dialog = new CustomDialog(this, 5);
        dialog.setCancelable(true);
        dialog.setMessage("是否要清除缓存，清除缓存后一些数据将重新下载");
        dialog.setSureBtnListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    public void run() {
                        File cacheDir = StorageUtils.getCacheDirectory(BaseApplication.getInstance());
                        if (cacheDir != null) {
                            FileUtils.deleteDir(cacheDir);
                        }
                    }
                }).start();
                dialog.dismiss();
                AboutUsActivity.this.cacheText.setText("0B");
            }
        });
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        if (menu != null && menu.size() > 0) {
            MenuItem item = menu.findItem(R.id.action_account);
            this.accountMenuItem = item;
            if (LoginManager.getInstance().isLogined()) {
                item.setTitle(R.string.account);
            } else {
                item.setTitle(R.string.login);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_account) {
            return super.onOptionsItemSelected(item);
        }
        if (LoginManager.getInstance().isLogined()) {
            WebViewActivity.goToWebView(this, UrlConstance.getAccountCenterUrl(), "个人中心", false, false);
        } else {
            LoginActivity.showLoginActivity(this);
        }
        return true;
    }
}
