package com.niuza.android.module.logic.gson;

import java.io.Serializable;
import java.util.List;

public class ProductResponse implements Serializable {
    public List<ProductItem> products;
    public int rtn;

    public class ProductItem implements Serializable {
        public String buyaddr;
        public String hot;
        public String hottime;
        public String id;
        public String img;
        public String saler;
        public String title;
        public String titleurl;
    }
}
