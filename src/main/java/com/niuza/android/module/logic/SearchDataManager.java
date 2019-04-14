package com.niuza.android.module.logic;

import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue.RequestFilter;
import com.niuza.android.NZApplication;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.entity.Product;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchDataManager extends ProductDataManager {
    private String mKeyWord;

    public void searchKeyword(String keyWord, IDataResponse<Product> response) {
        if (!TextUtils.isEmpty(keyWord)) {
            if (this.isLoadingData) {
                NZApplication.getApp().getNetQueue().cancelAll(new RequestFilter() {
                    public boolean apply(Request<?> request) {
                        if (request.getUrl().contains(SearchDataManager.this.getUrl())) {
                            return true;
                        }
                        return false;
                    }
                });
            }
            this.isLoadingData = true;
            this.mStart = 0;
            this.dataList.clear();
            this.mKeyWord = keyWord;
            getData(response);
        }
    }

    public void refreshData(IDataResponse<Product> response) {
        if (response != null) {
            response.onResponse(-2, "not support refresh", null);
        }
    }

    public String getUrl() {
        String url = UrlConstance.getSearchUrl();
        try {
            return url + "&keyword=" + URLEncoder.encode(this.mKeyWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url + "&keyword=" + this.mKeyWord;
        }
    }
}
