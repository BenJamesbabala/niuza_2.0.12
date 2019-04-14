package com.niuza.android.module.entity;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.Html;
import com.niuza.android.module.logic.gson.ProductDetailResponse.ProductDetailItem;
import java.util.List;

public class ProductDetail extends Product {
    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }

        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }
    };
    public CharSequence detailInfo;
    public String heyiUrl;
    public String originalDetaiInfo;
    public List<Product> others;
    public String quanTitle;
    public String quanUrl;
    public String taokouLing;
    public String titleUrl;

    public ProductDetail(Parcel in) {
        super(in);
        this.originalDetaiInfo = in.readString();
        this.detailInfo = Html.fromHtml(this.originalDetaiInfo);
    }

    public ProductDetail(ProductDetailItem item) {
        super();
        this.originalTitle = item.title;
        this.title = Html.fromHtml(this.originalTitle);
        this.imgUrl = item.img;
        this.buyAddr = item.buyaddr;
        this.saler = item.saler;
        this.originalDetaiInfo = item.newstext;
        this.detailInfo = Html.fromHtml(this.originalDetaiInfo);
        this.titleUrl = item.titleurl;
        this.heyiUrl = item.heyi;
        this.taokouLing = item.taokouling;
        this.quanTitle = item.quan_title;
        this.quanUrl = item.quan_url;
        try {
            this.id = Long.parseLong(item.id);
            this.hot = Integer.parseInt(item.hot);
            this.hotTime = Long.parseLong(item.hottime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.originalDetaiInfo);
    }
}
