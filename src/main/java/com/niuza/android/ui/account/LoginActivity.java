package com.niuza.android.ui.account;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.niuza.android.R;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.account.LoginManager;
import com.niuza.android.module.account.LoginManager.LoginListener;
import com.niuza.android.ui.common.NABaseActivity;
import com.niuza.android.ui.webview.WebViewActivity;
//import com.umeng.socialize.UMShareAPI;
import org.szuwest.view.CustomDialog;

public class LoginActivity extends NABaseActivity {
    EditText accountEditor;
    View loginBtn;
    EditText passwordEditor;

    public static void showLoginActivity(Activity from) {
        from.startActivity(new Intent(from, LoginActivity.class));
    }

    public static void showLoginDialog(final Activity from) {
        if (!LoginManager.getInstance().isLogined()) {
            CustomDialog dialog = new CustomDialog(from, 5);
            dialog.setTitle((CharSequence) "温馨提示");
            dialog.setMessage("请先登录账号才能正常使用此功能");
            dialog.setCancelBtnText("知道了");
            dialog.setSureBtnText("去登陆");
            dialog.setSureBtnListener(new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    LoginActivity.showLoginActivity(from);
                }
            });
            dialog.show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSwipeEnabled(false);
        setTitle("账号登录");
        initUI();
    }

    private void initUI() {
        this.accountEditor = (EditText) findViewById(R.id.accountEdit);
        this.passwordEditor = (EditText) findViewById(R.id.passwordEdit);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.loginWithAccoutNPwd();
            }
        });
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.goToRegister();
            }
        });
        findViewById(R.id.forgetPwdBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.goToForgetPassword();
            }
        });
        findViewById(R.id.wechatLoginBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginManager.getInstance().loginWithWeinxin(LoginActivity.this, new LoginListener() {
                    public void onLoginResult(int code, String msg) {
                        LoginActivity.this.loginBtn.setEnabled(true);
//                        LoginActivity.this.dismissDialog();
                        if (code != 0) {
                            Toast.makeText(LoginActivity.this, "登录失败:" + msg, 1).show();
                            return;
                        }
                        Toast.makeText(LoginActivity.this, "登录成功", 1).show();
                        LoginActivity.this.finish();
                    }
                });
            }
        });
        findViewById(R.id.qqLoginBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginManager.getInstance().loginWithQQ(LoginActivity.this, new LoginListener() {
                    public void onLoginResult(int code, String msg) {
                        LoginActivity.this.loginBtn.setEnabled(true);
//                        LoginActivity.this.dismissDialog();
                        if (code == 0) {
                            Toast.makeText(LoginActivity.this, "登录成功", 1).show();
                            LoginActivity.this.finish();
                        } else if (code != -2) {
                            Toast.makeText(LoginActivity.this, "登录失败:" + msg, 1).show();
                        }
                    }
                });
            }
        });
        this.accountEditor.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LoginActivity.this.passwordEditor.requestFocus();
                return true;
            }
        });
        this.passwordEditor.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LoginActivity.this.loginWithAccoutNPwd();
                return true;
            }
        });
    }

    private void loginWithAccoutNPwd() {
        String account = this.accountEditor.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不能为空", 0).show();
            return;
        }
        String password = this.passwordEditor.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", 0).show();
            return;
        }
        this.loginBtn.setEnabled(false);
        showLoadingDialog("正在登录...");
        LoginManager.getInstance().loginWithUserNameAndPwd(account, password, new LoginListener() {
            public void onLoginResult(int code, String msg) {
                LoginActivity.this.loginBtn.setEnabled(true);
                LoginActivity.this.dismissDialog();
                if (code != 0) {
                    Toast.makeText(LoginActivity.this, "登录失败:" + msg, 1).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "登录成功", 1).show();
                LoginActivity.this.finish();
            }
        });
    }

    private void goToRegister() {
        WebViewActivity.goToWebView(this, UrlConstance.getRegisterUrl(), "注册账号", false, false);
    }

    private void goToForgetPassword() {
        WebViewActivity.goToWebView(this, UrlConstance.getForgetPsdUrl(), "忘记密码", false, false);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    protected void onDestroy() {
        super.onDestroy();
//        UMShareAPI.get(this).release();
    }
}
