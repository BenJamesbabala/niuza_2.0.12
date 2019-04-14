package my.base.update;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import my.base.App;
import my.base.net.HttpException;
import my.base.util.LogUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    private static final String PREFER_APN_URI = "content://telephony/carriers/preferapn";
    private static final String tag = "HttpUtils";

    public static String doGetText(String url, String parameters, boolean isProxyAPN) throws HttpException {
        HttpEntity httpEntity = null;
        if (isProxyAPN) {
            try {
                httpEntity = doGetEntity2(url, parameters);
            } catch (UnknownHostException e) {
                throw new HttpException(e.getLocalizedMessage());
            } catch (SocketException e2) {
                throw new HttpException(e2.getLocalizedMessage());
            } catch (ClientProtocolException e3) {
                throw new HttpException(e3.getLocalizedMessage());
            } catch (IOException e4) {
                throw new HttpException(e4.getLocalizedMessage());
            }
        }
        String httpResponseStr = "";
        try {
            httpEntity = doGetEntity(new StringBuilder(String.valueOf(url)).append(parameters).toString());

            httpResponseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e4) {
            throw new HttpException(e4.getLocalizedMessage());
        }
        if (httpEntity == null) {
            return null;
        }

//        String httpResponseStr = EntityUtils.toString(httpEntity);
        LogUtils.i(tag, "--------------------");
        LogUtils.i(tag, "response size: " + httpResponseStr.length());
        LogUtils.i(tag, "response: ");
        LogUtils.i(tag, httpResponseStr);
        LogUtils.i(tag, "--------------------");
        return httpResponseStr;
    }

    private static HttpEntity doGetEntity(String url) throws UnknownHostException, SocketException, ClientProtocolException, IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
        int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
        if (responseStatusCode == 200) {
            return httpResponse.getEntity();
        }
        LogUtils.e(tag, new StringBuilder(String.valueOf(responseStatusCode)).toString());
        return null;
    }

    private static HttpEntity doGetEntity2(String url, String parameters) throws ClientProtocolException, IOException {
        URL tmpurl = new URL(new StringBuilder(String.valueOf(url)).append(parameters).toString());
        HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
        HttpHost target = new HttpHost(tmpurl.getHost(), 80, "http");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.route.default-proxy", proxy);
        HttpGet req = new HttpGet(new StringBuilder(String.valueOf(url)).append(parameters).toString());
        System.out.println("executing request to " + target + " via " + proxy);
        HttpResponse rsp = httpclient.execute(target, req);
        System.out.println("----------------------------------------");
        System.out.println(rsp.getStatusLine());
        Header[] headers = rsp.getAllHeaders();
        for (Object println : headers) {
            System.out.println(println);
        }
        System.out.println("----------------------------------------");
        int responseStatusCode = rsp.getStatusLine().getStatusCode();
        if (responseStatusCode == 200) {
            return rsp.getEntity();
        }
        LogUtils.e(tag, new StringBuilder(String.valueOf(responseStatusCode)).toString());
        return null;
    }

    public static boolean isProxyAPN(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() == 1) {
            return false;
        }
        Cursor cursor_current = context.getContentResolver().query(Uri.parse(PREFER_APN_URI), null, null, null, null);
        if (cursor_current != null && cursor_current.moveToFirst()) {
            String proxy = cursor_current.getString(cursor_current.getColumnIndex("proxy"));
            String apn = cursor_current.getString(cursor_current.getColumnIndex("apn"));
            String port = cursor_current.getString(cursor_current.getColumnIndex("port"));
            String current = cursor_current.getString(cursor_current.getColumnIndex("current"));
            cursor_current.close();
            if (proxy == null || apn == null || port == null || current == null || proxy.equals("") || port.equals("")) {
                return false;
            }
            LogUtils.d(tag, "proxy=" + proxy + " port=" + port + " current=" + current);
            if ((proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172")) && port.equals("80") && current.equals("1")) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkHasWapAPN(Context context) {
        Cursor cursor_need = context.getContentResolver().query(Uri.parse("content://telephony/carriers"), null, null, null, null);
        while (cursor_need != null && cursor_need.moveToNext()) {
            String id = cursor_need.getString(cursor_need.getColumnIndex("_id"));
            String port = cursor_need.getString(cursor_need.getColumnIndex("port"));
            String proxy = cursor_need.getString(cursor_need.getColumnIndex("proxy"));
            String current = cursor_need.getString(cursor_need.getColumnIndex("current"));
            String mmsc = cursor_need.getString(cursor_need.getColumnIndex("mmsc"));
            LogUtils.d(tag, "proxy=" + proxy + " port=" + port + " current=" + current + " id=" + id);
            if (!(proxy == null || port == null || current == null)) {
                if ((proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172")) && port.equals("80") && current.equals("1") && mmsc == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean downloadFile(Context context, File file, URL dlurl, boolean isProxyAPN) throws IOException {
        HttpURLConnection conn;
        byte[] buf = new byte[8192];
        if (isProxyAPN) {
            try {
                String domain = "";
                String path = "";
                String requestURL = dlurl.toString();
                int index = requestURL.indexOf("http://");
                if (index >= 0) {
                    requestURL = requestURL.substring(index + 7);
                    index = requestURL.indexOf("/");
                    if (index > 0) {
                        domain = requestURL.substring(0, index);
                        path = requestURL.substring(index);
                    } else {
                        domain = requestURL;
                        path = "/";
                    }
                }
                LogUtils.d(App.tag, "domain:" + domain + " | path:" + path);
                conn = (HttpURLConnection) new URL("http://10.0.0.172" + path).openConnection();
                conn.setRequestProperty("X-Online-Host", domain);
            } catch (IOException e) {
                IOException e2 = e;
                e2.printStackTrace();
                Log.e(file.getAbsolutePath(), e2.getMessage());
                return false;
            }
        }
        conn = (HttpURLConnection) dlurl.openConnection();
        conn.setConnectTimeout(60000);
        conn.setRequestMethod("GET");
        conn.setAllowUserInteraction(true);
        conn.setRequestProperty("Connection", "Keep-Alive");
        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 8192);
        BufferedInputStream bufferedInputStream;
        try {
            FileOutputStream fOut;
            if (file.getAbsolutePath().startsWith("/data/data")) {
                fOut = context.openFileOutput(file.getName(), 1);
            } else {
                fOut = new FileOutputStream(file);
            }
            while (true) {
                int lenght = bis.read(buf, 0, 8192);
                if (lenght <= 0) {
                    bis.close();
                    fOut.close();
                    Log.d(file.getAbsolutePath(), "download done!");
                    bufferedInputStream = bis;
                    return true;
                }
                fOut.write(buf, 0, lenght);
            }
        } catch (IOException e3) {
            IOException e2 = e3;
            bufferedInputStream = bis;
            e2.printStackTrace();
            Log.e(file.getAbsolutePath(), e2.getMessage());
            return false;
        }
    }
}
