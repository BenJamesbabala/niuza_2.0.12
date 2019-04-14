package com.niuza.android.module.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Html;
import android.text.TextUtils;
import com.niuza.android.module.logic.gson.ProductResponse;
import com.niuza.android.module.logic.gson.ProductResponse.ProductItem;
import java.util.LinkedList;
import java.util.List;

public class Product implements Parcelable {
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product[] newArray(int size) {
            return new Product[size];
        }

        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
    };
    private static final String[] specialSite = new String[]{"img19.niuza.com", "upyun.kiees.com", "img1.ruyig.com"};
    private static final String[] stringSize = new String[]{"430x430", "430x430q90", "400x400", "360x360", "320x320", "300x300"};
    public String buyAddr;
    public int hot;
    public long hotTime;
    public long id;
    public String imgUrl;
    public boolean isZhiding = false;
    public String originalTitle;
    public String saler = "";
    public CharSequence title;
    public String titleUrl;

    public Product(ProductItem item) {
        this.originalTitle = item.title;
        this.title = Html.fromHtml(this.originalTitle);
        this.imgUrl = getThumbImage(item.img);
        this.buyAddr = item.buyaddr;
        this.saler = item.saler;
        this.titleUrl = item.titleurl;
        try {
            this.id = Long.parseLong(item.id);
            this.hot = Integer.parseInt(item.hot);
            this.hotTime = Long.parseLong(item.hottime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Product> fromProductResponse(ProductResponse productResponse) {
        List<Product> list = new LinkedList();
        if (!(productResponse == null || productResponse.products == null)) {
            for (ProductItem item : productResponse.products) {
                list.add(new Product(item));
            }
        }
        return list;
    }

    public Product() {
        super();
        this.saler = "";
        this.isZhiding = false;
    }

    public Product(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.imgUrl = in.readString();
        this.buyAddr = in.readString();
        this.hot = in.readInt();
        this.hotTime = in.readLong();
        this.saler = in.readString();
    }

    private String getThumbImage(String imageUrl) {
        int i = 0;
        if (TextUtils.isEmpty(imageUrl)) {
            return imageUrl;
        }
        for (String str : stringSize) {
            int index = imageUrl.lastIndexOf(str);
            if (index != -1) {
                return imageUrl.substring(0, index) + imageUrl.substring(index).replace(str, "160x160");
            }
        }
        String[] strArr = specialSite;
        int length = strArr.length;
        while (i < length) {
            if (imageUrl.contains(strArr[i]) && !imageUrl.endsWith("-150.jpg")) {
                return imageUrl + "-150.jpg";
            }
            i++;
        }
        return imageUrl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title.toString());
        dest.writeString(this.imgUrl);
        dest.writeString(this.buyAddr);
        dest.writeInt(this.hot);
        dest.writeLong(this.hotTime);
        dest.writeString(this.saler == null ? "" : this.saler);
    }
}
