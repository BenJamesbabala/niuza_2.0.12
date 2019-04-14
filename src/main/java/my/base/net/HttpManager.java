package my.base.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import my.base.App;
import my.base.util.LogUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpManager {
    private static final String tag = "HttpManager";

    public static String doGetText(String url) throws HttpException {
        return doGetText(url, 0);
    }

    private static HttpEntity doGetEntity(String url) throws UnknownHostException, SocketException, ClientProtocolException, IOException {
        return doGetEntity(url, 0);
    }

    public static Bitmap downloadBitmap(String url) {
        Exception e;
        LogUtils.d(App.tag, url);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 120000);
        HttpConnectionParams.setSoTimeout(httpParams, 120000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet httpGet = null;
        NetManager.checkNetwork(App.context);
        HttpEntity entity = null;
        InputStream inputStream = null;
        try {
            HttpResponse response;
            HttpGet getRequest;
            if (NetManager.networkType == 3) {
                String domain = "";
                String path = "";
                int index = url.indexOf("http://");
                if (index >= 0) {
                    url = url.substring(index + 7);
                    index = url.indexOf("/");
                    if (index > 0) {
                        domain = url.substring(0, index);
                        path = url.substring(index);
                    } else {
                        domain = url;
                        path = "/";
                    }
                }
                LogUtils.d(App.tag, "domain:" + domain + " | path:" + path);
                HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
                HttpHost httpHost = new HttpHost(domain, 80, "http");
                client.getParams().setParameter("http.route.default-proxy", proxy);
                getRequest = new HttpGet(path);
                try {
                    response = client.execute(httpHost, getRequest);
                    httpGet = getRequest;
                } catch (Exception e2) {
                    e = e2;
                    httpGet = getRequest;
                    LogUtils.e(App.tag, e.getMessage());
                    httpGet.abort();
                    return null;
                }
            }
            getRequest = new HttpGet(url);
            response = client.execute(getRequest);
            httpGet = getRequest;
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                LogUtils.d(App.tag, new StringBuilder(String.valueOf(statusCode)).toString());
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                inputStream = null;
                inputStream = entity.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
                entity.consumeContent();
                return bitmap;
            }
            return null;
        } catch (Exception e3) {
            e = e3;
            LogUtils.e(App.tag, e.getMessage());
            httpGet.abort();
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ie) {

                }
            }
            try {
                entity.consumeContent();
            } catch (IOException ie) {

            }
        }

        return null;
    }

    private static HttpEntity doGetEntity(String url, int timeout) throws UnknownHostException, SocketException, ClientProtocolException, IOException {
        int i;
        HttpResponse httpResponse;
        HttpParams httpParams = new BasicHttpParams();
        if (timeout > 0) {
            i = timeout;
        } else {
            i = 30000;
        }
        HttpConnectionParams.setConnectionTimeout(httpParams, i);
        if (timeout <= 0) {
            timeout = 30000;
        }
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpClient client = new DefaultHttpClient(httpParams);
        NetManager.checkNetwork(App.context);
        if (NetManager.networkType == 3) {
            String domain = "";
            String path = "";
            int index = url.indexOf("http://");
            if (index >= 0) {
                url = url.substring(index + 7);
                index = url.indexOf("/");
                if (index > 0) {
                    domain = url.substring(0, index);
                    path = url.substring(index);
                } else {
                    domain = url;
                    path = "/";
                }
            }
            LogUtils.d(App.tag, "domain:" + domain + " | path:" + path);
            HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
            HttpHost target = new HttpHost(domain, 80, "http");
            client.getParams().setParameter("http.route.default-proxy", proxy);
            httpResponse = client.execute(target, new HttpGet(path));
        } else {
            httpResponse = client.execute(new HttpGet(url));
        }
        int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
        if (responseStatusCode == 200) {
            return httpResponse.getEntity();
        }
        LogUtils.e(tag, new StringBuilder(String.valueOf(responseStatusCode)).toString());
        return null;
    }

    public static String doGetText(String url, int timeout) throws HttpException {
        try {
            HttpEntity httpEntity = doGetEntity(url, timeout);
            if (httpEntity == null) {
                return null;
            }
            String httpResponseStr = EntityUtils.toString(httpEntity);
            LogUtils.i(tag, "--------------------");
            LogUtils.i(tag, "response size: " + httpResponseStr.length());
            LogUtils.i(tag, "response: ");
            LogUtils.i(tag, httpResponseStr);
            LogUtils.i(tag, "--------------------");
            return httpResponseStr;
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
}
