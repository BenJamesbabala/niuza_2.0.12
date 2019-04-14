package my.base.update;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import my.base.net.HttpException;
import my.base.util.ApplicationUtils;
import my.base.util.FileUtils;
import my.base.util.LogUtils;
import my.base.util.PhoneUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetUpdateInfoThread extends Thread {
    public static final int CHENCKING_UPDATE = 199;
    public static final int DOWNLOAD_APKEXIST = 209;
    public static final int DOWNLOAD_BEGIN = 205;
    public static final int DOWNLOAD_DONE = 206;
    public static final int DOWNLOAD_ERROR = 207;
    public static final int DOWNLOAD_NOSDCARD = 208;
    public static final int FORCE_UPDATE = 202;
    public static final int NEED_UPDATE = 201;
    public static final int NET_ERROR = 203;
    public static final int NO_UPDATE = 200;
    public static final int SERVER_ERROR = 204;
    private static final String TAG = "GetUpdateInfoThread";
    private Activity activity;
    private boolean autoUpdate;
    String dlurl = UpdateSettings.updateUrl;
    private boolean isProxyAPN;
    Handler upHandler;

    class DownloadThread extends Thread {
        private String apkPath = "";
        Context context;
        private File file;

        public DownloadThread(Context context) {
            this.context = context;
        }

        public void run() {
            URL url;
            MalformedURLException e;
            GetUpdateInfoThread.this.upHandler.sendEmptyMessage(205);
            try {
                URL url2 = new URL(GetUpdateInfoThread.this.dlurl);
                try {
                    LogUtils.d(GetUpdateInfoThread.TAG, GetUpdateInfoThread.this.dlurl);
                    String apkFileName = new StringBuilder(String.valueOf(GetUpdateInfoThread.this.activity.getPackageName())).append(ApplicationUtils.getVerCode(GetUpdateInfoThread.this.activity, GetUpdateInfoThread.this.activity.getPackageName())).append(".apk").toString();
                    if (FileUtils.hasSd()) {
                        this.apkPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        this.file = new File(this.apkPath, apkFileName);
                        if (this.file.exists()) {
                            GetUpdateInfoThread.this.upHandler.sendMessage(GetUpdateInfoThread.this.upHandler.obtainMessage(209, this.file.getAbsolutePath()));
                            Log.e(GetUpdateInfoThread.TAG, new StringBuilder(String.valueOf(this.file.getAbsolutePath())).append("exist!!!!!!!!!!!!!!!").toString());
                            url = url2;
                            return;
                        }
                        try {
                            this.file.createNewFile();
                            LogUtils.d(GetUpdateInfoThread.TAG, "DownloadThread apkAbsolutePath=" + this.file.getAbsolutePath());
                            try {
                                if (HttpUtils.downloadFile(this.context, this.file, url2, GetUpdateInfoThread.this.isProxyAPN)) {
                                    GetUpdateInfoThread.this.upHandler.sendMessage(GetUpdateInfoThread.this.upHandler.obtainMessage(206, this.file.getAbsolutePath()));
                                } else {
                                    GetUpdateInfoThread.this.upHandler.sendEmptyMessage(207);
                                }
                                url = url2;
                                return;
                            } catch (IOException e2) {
                                GetUpdateInfoThread.this.upHandler.sendEmptyMessage(207);
                                LogUtils.d(GetUpdateInfoThread.TAG, e2.getMessage());
                                e2.printStackTrace();
                                url = url2;
                                return;
                            }
                        } catch (IOException e1) {
                            GetUpdateInfoThread.this.upHandler.sendEmptyMessage(207);
                            Log.e(GetUpdateInfoThread.TAG, new StringBuilder(String.valueOf(this.file.getAbsolutePath())).append("not create!!!!!!!!!!!!!!!").toString());
                            e1.printStackTrace();
                            url = url2;
                            return;
                        }
                    }
                    GetUpdateInfoThread.this.upHandler.sendEmptyMessage(208);
                    url = url2;
                } catch (Exception e3) {
//                    Exception e = e3;
                    url = url2;
                    GetUpdateInfoThread.this.upHandler.sendEmptyMessage(207);
                    LogUtils.d(GetUpdateInfoThread.TAG, e3.getMessage());
                    e3.printStackTrace();
                }
            } catch (MalformedURLException e4) {
                e = e4;
                GetUpdateInfoThread.this.upHandler.sendEmptyMessage(207);
                LogUtils.d(GetUpdateInfoThread.TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public class UpdateHandler extends Handler {
        Context context;
        ProgressDialog pDialog;

        public UpdateHandler(Context context) {
            this.context = context;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.pDialog != null && this.pDialog.isShowing()) {
                this.pDialog.dismiss();
            }
            switch (msg.what) {
                case 199:
                    this.pDialog = new ProgressDialog(this.context);
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_checking"));
                    this.pDialog.setCancelable(false);
                    this.pDialog.show();
                    return;
                case 200:
                    showDialog(GetUpdateInfoThread.this.getString("update_noNewversion"), 0);
                    return;
                case 201:
                    showUpdate((String) msg.obj);
                    return;
                case 202:
                    GetUpdateInfoThread.this.dlurl = (String) msg.obj;
                    showDialog(GetUpdateInfoThread.this.getString("update_forceUpdate"), 3);
                    return;
                case 203:
                    Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_net_error"), 1).show();
                    return;
                case 204:
                    Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_server_error"), 1).show();
                    return;
                case 205:
                    if (this.pDialog == null) {
                        this.pDialog = new ProgressDialog(this.context);
                    }
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_download_begin"));
                    this.pDialog.setCancelable(false);
                    this.pDialog.show();
                    return;
                case 206:
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_download_done"));
                    if (this.pDialog.isShowing()) {
                        this.pDialog.dismiss();
                    }
                    Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_download_done"), 0).show();
                    installFromIntent(new File((String) msg.obj));
                    return;
                case 207:
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_download_error"));
                    if (this.pDialog.isShowing()) {
                        this.pDialog.dismiss();
                    }
                    Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_download_error"), 1).show();
                    return;
                case 208:
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_nosdcard"));
                    if (this.pDialog.isShowing()) {
                        this.pDialog.dismiss();
                    }
                    Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_nosdcard"), 1).show();
                    return;
                case 209:
                    this.pDialog.setMessage(GetUpdateInfoThread.this.getString("update_apkexists"));
                    if (this.pDialog.isShowing()) {
                        this.pDialog.dismiss();
                    }
                    installFromIntent(new File((String) msg.obj));
                    return;
                default:
                    return;
            }
        }

        private void showUpdate(String verJsonInfo) {
            String newVersionName = "";
            String newFeatures = "";
            String apkPath = "";
            String appStoreId = "";
            try {
                JSONArray array = new JSONArray(verJsonInfo);
                if (array.length() > 1) {
                    JSONObject obj = array.getJSONObject(1);
                    newVersionName = obj.getString("versionName");
                    int versionCode = obj.getInt(UpdateSettings.VERSIONCODE);
                    newFeatures = obj.getString("newFeatures");
                    GetUpdateInfoThread.this.dlurl = obj.getString(UpdateSettings.DOWNLOAD_URL).trim();
                    apkPath = obj.getString("apkPath").trim();
                    appStoreId = obj.getString("appStoreId");
                    LogUtils.v(GetUpdateInfoThread.TAG, "UpdateHandler  " + verJsonInfo);
                    if (versionCode != 0) {
                        Editor editor = this.context.getSharedPreferences(UpdateSettings.UPDATE_SETTING, 0).edit();
                        editor.putInt(UpdateSettings.VERSIONCODE, versionCode);
                        editor.putString(UpdateSettings.DOWNLOAD_URL, GetUpdateInfoThread.this.dlurl);
                        editor.commit();
                    }
                    showDialog(new StringBuilder(String.valueOf(GetUpdateInfoThread.this.getString("update_hasNewVersion"))).append(newVersionName).append("\n").append(newFeatures).toString(), 1);
                }
            } catch (Exception e) {
                LogUtils.e(GetUpdateInfoThread.TAG, "UpdateHandler   " + e.getMessage());
            }
        }

        private void showDialog(String msg, int whitch) {
            Builder builder = new Builder(this.context);
            builder.setTitle(GetUpdateInfoThread.this.getString("update_software"));
            builder.setMessage(msg);
            if (whitch == 1) {
                builder.setPositiveButton(GetUpdateInfoThread.this.getString("update_updatenow"), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!GetUpdateInfoThread.this.dlurl.equals(UpdateSettings.updateUrl)) {
                            if (GetUpdateInfoThread.this.dlurl.endsWith("apk")) {
                                new DownloadThread(UpdateHandler.this.context).start();
                            } else {
                                UpdateHandler.this.go2DownSite();
                            }
                        }
                    }
                }).setNegativeButton(GetUpdateInfoThread.this.getString("update_updatelater"), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
            } else {
                builder.setPositiveButton(GetUpdateInfoThread.this.getString("update_sure"), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!GetUpdateInfoThread.this.dlurl.equals(UpdateSettings.updateUrl)) {
                            if (GetUpdateInfoThread.this.dlurl.endsWith("apk")) {
                                new DownloadThread(UpdateHandler.this.context).start();
                            } else {
                                UpdateHandler.this.go2DownSite();
                            }
                        }
                    }
                });
            }
            builder.create().show();
        }

        void go2DownSite() {
            try {
                this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(GetUpdateInfoThread.this.dlurl)));
            } catch (Exception e) {
                Toast.makeText(this.context, GetUpdateInfoThread.this.getString("update_server_error"), 1).show();
                LogUtils.e(GetUpdateInfoThread.TAG, "UpdateHandler" + e.getMessage());
            }
        }

        void installFromIntent(File pkgFile) {
            if (pkgFile.exists()) {
                Uri uri = Uri.fromFile(pkgFile);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(1);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                this.context.startActivity(intent);
            }
        }
    }

    public GetUpdateInfoThread(Activity activity, boolean autoUpdate) {
        this.activity = activity;
        this.upHandler = new UpdateHandler(activity);
        this.autoUpdate = autoUpdate;
        this.isProxyAPN = HttpUtils.isProxyAPN(activity);
    }

    public void run() {
        if (!this.autoUpdate) {
            this.upHandler.sendEmptyMessage(199);
        }
        String url = UpdateSettings.updateUrl;
        int versionCode = ApplicationUtils.getVerCode(this.activity, this.activity.getPackageName());
        String versionName = ApplicationUtils.getVerName(this.activity, this.activity.getPackageName());
        StatisticalInfo info = new StatisticalInfo();
        info.setBrand(Build.BRAND);
        info.setCpuabi(Build.CPU_ABI);
        info.setHeight(PhoneUtils.getDisplayMetricsHeight(this.activity));
        info.setWidth(PhoneUtils.getDisplayMetricsWidth(this.activity));
        info.setModel(Build.MODEL);
        info.setImei(PhoneUtils.getIMEI(this.activity));
        info.setSdk(VERSION.SDK_INT);
        info.setSysvel(VERSION.RELEASE);
        info.setUsedTimes(UpdateSettings.getUsedTimes(this.activity));
        info.setVersionCode(versionCode);
        info.setVersionName(versionName);
        info.setWap3_cid(UpdateSettings.wap3_cid);
        String parameters = info.toString().trim().replace(" ", "_");
        int updateState = 0;
        int updateCycle = 48;
        String verjsonInfo = "";
        try {
            LogUtils.d(TAG, new StringBuilder(String.valueOf(url)).append(parameters).toString());
            verjsonInfo = HttpUtils.doGetText(url, parameters, this.isProxyAPN);
            if (verjsonInfo == null) {
                throw new HttpException("");
            }
            LogUtils.d(TAG, verjsonInfo);
            JSONArray array = new JSONArray(verjsonInfo);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                updateState = obj.getInt("updateState");
                updateCycle = obj.getInt(UpdateSettings.UPDATECYCLE);
            }
            if (updateState == 1) {
                saveUpdateCycle(updateCycle, false);
                this.upHandler.sendMessage(this.upHandler.obtainMessage(201, verjsonInfo));
            } else if (updateState == 2) {
                saveUpdateCycle(0, true);
                this.upHandler.sendMessage(this.upHandler.obtainMessage(201, verjsonInfo));
            } else {
                saveUpdateCycle(updateCycle, false);
                if (!this.autoUpdate) {
                    this.upHandler.sendEmptyMessage(200);
                }
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, e.getMessage());
//            throw e;
        } catch (HttpException e2) {
            saveUpdateCycle(1, false);
            if (!this.autoUpdate) {
                this.upHandler.sendEmptyMessage(203);
            }
            LogUtils.e(TAG, new StringBuilder(String.valueOf(e2.getMessage())).toString());
            e2.printStackTrace();
//        } catch (JSONException e3) {
//            saveUpdateCycle(48, false);
//            if (!this.autoUpdate) {
//                this.upHandler.sendEmptyMessage(204);
//            }
//            LogUtils.e(TAG, new StringBuilder(String.valueOf(e3.getMessage())).toString());
        }
    }

    private void saveUpdateCycle(int updateCycle, boolean noMorePlay) {
        SharedPreferences prefs = this.activity.getSharedPreferences(UpdateSettings.UPDATE_SETTING, 0);
        long lastUpdateTime = System.currentTimeMillis();
        Editor editor = prefs.edit();
        editor.putLong(UpdateSettings.LASTUPDATETIME, lastUpdateTime);
        editor.putInt(UpdateSettings.UPDATECYCLE, updateCycle);
        editor.putBoolean(UpdateSettings.NOMOREPLAY, noMorePlay);
        editor.commit();
    }

    private String getString(String name) {
        return this.activity.getString(this.activity.getResources().getIdentifier(name, "string", this.activity.getPackageName()));
    }
}
