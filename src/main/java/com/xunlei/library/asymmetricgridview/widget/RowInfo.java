package com.xunlei.library.asymmetricgridview.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.xunlei.library.asymmetricgridview.model.AsymmetricItem;
import java.util.ArrayList;
import java.util.List;

class RowInfo<T extends AsymmetricItem> implements Parcelable {
    public static final Creator<RowInfo> CREATOR = new Creator<RowInfo>() {
        public RowInfo createFromParcel(Parcel in) {
            return new RowInfo(in);
        }

        public RowInfo[] newArray(int size) {
            return new RowInfo[size];
        }
    };
    private final List<AsymmetricItem> items;
    private final int rowHeight;
    private final float spaceLeft;

    public RowInfo(int rowHeight, List<AsymmetricItem> items, float spaceLeft) {
        this.rowHeight = rowHeight;
        this.items = items;
        this.spaceLeft = spaceLeft;
    }

    public RowInfo(Parcel in) {
        this.rowHeight = in.readInt();
        this.spaceLeft = in.readFloat();
        int totalItems = in.readInt();
        this.items = new ArrayList();
        ClassLoader classLoader = AsymmetricItem.class.getClassLoader();
        for (int i = 0; i < totalItems; i++) {
            this.items.add((AsymmetricItem) in.readParcelable(classLoader));
        }
    }

    public List<AsymmetricItem> getItems() {
        return this.items;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public float getSpaceLeft() {
        return this.spaceLeft;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.rowHeight);
        dest.writeFloat(this.spaceLeft);
        dest.writeInt(this.items.size());
        for (int i = 0; i < this.items.size(); i++) {
            dest.writeParcelable((Parcelable) this.items.get(i), 0);
        }
    }
}
