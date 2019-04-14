package com.niuza.android.module.logic.gson;

import com.niuza.android.module.logic.gson.ProductResponse.ProductItem;
import java.io.Serializable;
import java.util.List;

public class ProductDetailResponse implements Serializable {
    public ProductDetailItem product;
    public int rtn;

    public class ProductDetailItem implements Serializable {
        public String buyaddr;
        public String heyi;
        public String hot;
        public String hottime;
        public String id;
        public String img;
        public String newstext;
        public List<ProductItem> others;
        public String quan_title;
        public String quan_url;
        public String saler;
        public String taokouling;
        public String title;
        public String titleurl;
    }
}
