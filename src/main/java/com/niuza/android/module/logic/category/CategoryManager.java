package com.niuza.android.module.logic.category;

//import com.alibaba.wireless.security.SecExceptionCode;
import android.os.Parcel;

import com.niuza.android.AppMode;
import com.niuza.android.R;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.entity.Category;
import com.niuza.android.module.logic.DataManagerInterface;
import com.niuza.android.module.logic.IDataResponse;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager implements DataManagerInterface<Category> {
    private List<Category> dataList = new ArrayList();
    private boolean isLoadingData;

    public CategoryManager() {
        if (AppMode.appMode == 1) {
            makeData();
        } else if (AppMode.appMode == 2) {
            makeRuyigCategory();
        } else if (AppMode.appMode == 3) {
            makeKiessCategory();
        }
    }

    public String getUrl() {
        return UrlConstance.getAllCategoryUrl();
    }

    public boolean isLoadingData() {
        return this.isLoadingData;
    }

    public boolean hasMoreData() {
        return false;
    }

    public void refreshData(IDataResponse<Category> response) {
        if (response != null) {
            response.onResponse(0, "", this.dataList);
        }
    }

    public void loadMoreData(IDataResponse<Category> iDataResponse) {
    }

    public List<Category> getDataList() {
        return this.dataList;
    }

    private List<Category> makeData() {
        List<Category> categories = this.dataList;

        Parcel p = Parcel.obtain();
        Category category = new Category(p);
        p.recycle();

        category.categoryId = 31;
        category.categoryName = "女士服饰";
        category.iconResId = R.drawable.category_icon_nv_fuzhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 3213;
        category.categoryName = "男士服装";
        category.iconResId = R.drawable.category_icon_nan_fuzhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 35;
        category.categoryName = "鞋帽箱包";
        category.iconResId = R.drawable.category_icon_xie_bao;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 33;
        category.categoryName = "母婴儿童";
        category.iconResId = R.drawable.category_icon_muyin_ertong;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 30;
        category.categoryName = "个护化妆";
        category.iconResId = R.drawable.category_icon_huazhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 28;
        category.categoryName = "电脑数码";
        category.iconResId = R.drawable.category_icon_diannao;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 27;
        category.categoryName = "家用电器";
        category.iconResId = R.drawable.category_icon_jiadian;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 29;
        category.categoryName = "家居生活";
        category.iconResId = R.drawable.category_icon_jiaju;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 32;
        category.categoryName = "食品饮料";
        category.iconResId = R.drawable.category_icon_shipin_yinliao;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 34;
        category.categoryName = "钟表首饰";
        category.iconResId = R.drawable.category_icon_zhongbiao_shoushi;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 3205;
        category.categoryName = "汽车用品";
        category.iconResId = R.drawable.category_icon_qiche;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 36;
        category.categoryName = "其他";
        category.iconResId = R.drawable.category_icon_qita;
        categories.add(category);
        return categories;
    }

    private List<Category> makeRuyigCategory() {
        List<Category> categories = this.dataList;
        Parcel p = Parcel.obtain();
        Category category = new Category(p);
        p.recycle();
        category.categoryId = 15;
        category.categoryName = "9.9包邮";
        category.iconResId = R.drawable.category_ruyig_99baoyou;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 16;
        category.categoryName = "促销活动";
        category.iconResId = R.drawable.category_ruyig_cuxiao;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 1;
        category.categoryName = "服装鞋帽";
        category.iconResId = R.drawable.category_ruyig_fz_xm;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 2;
        category.categoryName = "数码产品";
        category.iconResId = R.drawable.category_ruyig_3c;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 5;
        category.categoryName = "家居生活";
        category.iconResId = R.drawable.category_ruyig_jiaju;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 3;
        category.categoryName = "吃货日记";
        category.iconResId = R.drawable.category_ruyig_chihuo;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 7;
        category.categoryName = "母婴玩具";
        category.iconResId = R.drawable.category_ruyig_my_wj;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 6;
        category.categoryName = "家用电器";
        category.iconResId = R.drawable.category_ruyig_jiadian;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 14;
        category.categoryName = "文体用品";
        category.iconResId = R.drawable.category_ruyig_wenti;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 4;
        category.categoryName = "个护化妆";
        category.iconResId = R.drawable.category_ruyig_huazhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 9;
        category.categoryName = "礼品箱包";
        category.iconResId = R.drawable.category_ruyig_lipin;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 11;
        category.categoryName = "钟表首饰";
        category.iconResId = R.drawable.category_ruyig_zb_ss;
        categories.add(category);
        return categories;
    }

    private List<Category> makeKiessCategory() {
        List<Category> categories = this.dataList;
        Parcel p = Parcel.obtain();
        Category category = new Category(p);
        p.recycle();
        category.categoryId = 11;
        category.categoryName = "时尚女装";
        category.iconResId = R.drawable.category_kiees_lvzhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 41650;
        category.categoryName = "实穿男装";
        category.iconResId = R.drawable.category_kiees_nanzhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 10;
        category.categoryName = "电脑数码";
        category.iconResId = R.drawable.category_kiees_3c;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 8;
        category.categoryName = "家居生活";
        category.iconResId = R.drawable.category_kiees_jiaju;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 9;
        category.categoryName = "家用电器";
        category.iconResId = R.drawable.category_kiees_jiadian;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 1399;
        category.categoryName = "汽车用品";
        category.iconResId = R.drawable.category_kiees_car;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 14;
        category.categoryName = "箱包鞋帽";
        category.iconResId = R.drawable.category_kiees_xb_xm;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 6;
        category.categoryName = "美妆饰品";
        category.iconResId = R.drawable.category_kiees_huazhuang;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 382;
        category.categoryName = "运动户外";
        category.iconResId = R.drawable.category_kiees_yundong;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 12;
        category.categoryName = "母婴玩具";
        category.iconResId = R.drawable.category_kiees_my_wj;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 16;
        category.categoryName = "食品保健";
        category.iconResId = R.drawable.category_kiees_yinshi;
        categories.add(category);
        p = Parcel.obtain();
        category = new Category(p);
        p.recycle();
        category.categoryId = 545;
        category.categoryName = "书籍旅行";
        category.iconResId = R.drawable.category_kiees_book;
        categories.add(category);
        return categories;
    }
}
