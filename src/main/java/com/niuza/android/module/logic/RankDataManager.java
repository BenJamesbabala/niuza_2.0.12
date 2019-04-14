package com.niuza.android.module.logic;

import com.niuza.android.module.UrlConstance;

public class RankDataManager extends ProductDataManager {
    private static RankDataManager instance;

    public static RankDataManager getInstance() {
        if (instance == null) {
            synchronized (RankDataManager.class) {
                if (instance == null) {
                    instance = new RankDataManager();
                }
            }
        }
        return instance;
    }

    public String getUrl() {
        return UrlConstance.getRankUrl();
    }
}
