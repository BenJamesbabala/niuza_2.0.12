package com.niuza.android.module.logic;

import android.os.Looper;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
//import com.xunlei.library.utils.XLLog;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public abstract class AbstractDataManager<T> implements DataManagerInterface<T> {
    public static final int COUNT = 30;
    protected List<T> dataList = new LinkedList();
    protected boolean hasMore = true;
    protected boolean isLoadingData = false;
    protected int mStart;

    public boolean isLoadingData() {
        return this.isLoadingData;
    }

    public boolean hasMoreData() {
        return this.hasMore;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    public static Response<String> handleNetResponse(NetworkResponse networkResponse) {
//        XLLog.d("AbstractDataManager", "isMainThread " + (Looper.myLooper() == Looper.getMainLooper()));
        try {
            if (networkResponse.headers.containsKey("Content-Encoding") && ((String) networkResponse.headers.get("Content-Encoding")).equals("gzip")) {
                return Response.success(getRealString(networkResponse.data), HttpHeaderParser.parseCacheHeaders(networkResponse));
            }
            return Response.success(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        }
    }

    private static int getShort(byte[] data) {
        return (data[0] << 8) | (data[1] & 255);
    }

    private static String getRealString(byte[] data) {
        boolean t = true;
        if (getShort(new byte[]{data[0], data[1]}) != 8075) {
            t = false;
        }
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in;
            InputStream bis = new ByteArrayInputStream(data);
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
