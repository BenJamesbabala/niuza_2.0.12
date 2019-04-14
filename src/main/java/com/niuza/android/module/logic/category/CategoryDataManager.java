package com.niuza.android.module.logic.category;

import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.logic.ProductDataManager;

public class CategoryDataManager extends ProductDataManager {
    private int mCategoryId;

    public CategoryDataManager(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getUrl() {
        return UrlConstance.getCategoryDataUrl() + "&classid=" + this.mCategoryId;
    }
}
