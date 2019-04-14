package com.niuza.android.module.logic.gson;

import java.io.Serializable;
import java.util.List;

public class CategoryResponse implements Serializable {
    public List<CategoryItem> categoryItems;
    public int rtn;

    public class CategoryItem implements Serializable {
        public String bclassid;
        public String classid;
        public String classname;
    }
}
