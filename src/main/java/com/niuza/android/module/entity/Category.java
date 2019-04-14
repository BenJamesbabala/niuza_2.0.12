package com.niuza.android.module.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category implements Parcelable {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category[] newArray(int size) {
            return new Category[size];
        }

        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }
    };
    public int bCategoryId;
    public int categoryId;
    public String categoryName;
    public int iconResId;

    public void fromJsonObj(JSONObject rootObj) {
        try {
            String classId = rootObj.optString("classid");
            if (classId != null) {
                this.categoryId = Integer.parseInt(classId);
            }
            String bclassId = rootObj.optString("bclassid");
            if (bclassId != null) {
                this.bCategoryId = Integer.parseInt(bclassId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.categoryName = rootObj.optString("classname");
    }

    public static List<Category> loadListFromJson(JSONObject rootObj) {
        List<Category> list = new ArrayList();
        if (rootObj.has("rtn") && rootObj.optInt("rtn") == 0) {
            JSONArray categoryList = rootObj.optJSONArray("categorys");
            if (categoryList != null) {
                for (int i = 0; i < categoryList.length(); i++) {
//                    Category c = new Category();
                    Parcel p = Parcel.obtain();
                    Category c = Category.CREATOR.createFromParcel(p);
                    p.recycle();
                    try {
                        c.fromJsonObj(categoryList.getJSONObject(i));
                        list.add(c);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    public Category(Parcel in) {
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
        this.bCategoryId = in.readInt();
        this.iconResId = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeInt(this.bCategoryId);
        dest.writeInt(this.iconResId);
    }
}
