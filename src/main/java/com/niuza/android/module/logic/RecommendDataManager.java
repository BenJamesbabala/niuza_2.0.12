package com.niuza.android.module.logic;

import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.entity.Product;
import java.util.List;

public class RecommendDataManager extends ProductDataManager {
    private static RecommendDataManager instance;

    public static RecommendDataManager getInstance() {
        if (instance == null) {
            synchronized (RecommendDataManager.class) {
                if (instance == null) {
                    instance = new RecommendDataManager();
                }
            }
        }
        return instance;
    }

    public RecommendDataManager() {
        List<Product> cacheList = getFromDiskCache(getUrl() + "&start=" + this.mStart + "&count=" + 30);
        if (cacheList != null) {
            addProducts(cacheList);
        }
    }

    public String getUrl() {
        return UrlConstance.getRecommendUrl();
    }
}
