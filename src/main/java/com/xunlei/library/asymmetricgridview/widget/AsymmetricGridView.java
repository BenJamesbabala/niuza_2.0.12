package com.xunlei.library.asymmetricgridview.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;
import com.xunlei.library.asymmetricgridview.AsymmetricGridViewAdapterContract;
import com.xunlei.library.asymmetricgridview.Utils;

public class AsymmetricGridView extends ListView {
    private static final int DEFAULT_COLUMN_COUNT = 2;
    private static final String TAG = "AsymmetricGridView";
    protected boolean allowReordering;
    protected boolean debugging;
    protected AsymmetricGridViewAdapterContract gridAdapter;
    protected int numColumns = 2;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected int requestedColumnCount;
    protected int requestedColumnWidth;
    protected int requestedHorizontalSpacing;

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable adapterState;
        boolean allowReordering;
        boolean debugging;
        int defaultPadding;
        ClassLoader loader;
        int numColumns;
        int requestedColumnCount;
        int requestedColumnWidth;
        int requestedHorizontalSpacing;
        int requestedVerticalSpacing;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            boolean z;
            boolean z2 = true;

            this.numColumns = in.readInt();
            this.requestedColumnWidth = in.readInt();
            this.requestedColumnCount = in.readInt();
            this.requestedVerticalSpacing = in.readInt();
            this.requestedHorizontalSpacing = in.readInt();
            this.defaultPadding = in.readInt();
            if (in.readByte() == (byte) 1) {
                z = true;
            } else {
                z = false;
            }
            this.debugging = z;
            if (in.readByte() != (byte) 1) {
                z2 = false;
            }
            this.allowReordering = z2;
            this.adapterState = in.readParcelable(this.loader);
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            int i2 = 1;
            super.writeToParcel(dest, flags);
            dest.writeInt(this.numColumns);
            dest.writeInt(this.requestedColumnWidth);
            dest.writeInt(this.requestedColumnCount);
            dest.writeInt(this.requestedVerticalSpacing);
            dest.writeInt(this.requestedHorizontalSpacing);
            dest.writeInt(this.defaultPadding);
            if (this.debugging) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeByte((byte) i);
            if (!this.allowReordering) {
                i2 = 0;
            }
            dest.writeByte((byte) i2);
            dest.writeParcelable(this.adapterState, flags);
        }
    }

    public AsymmetricGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.requestedHorizontalSpacing = Utils.dpToPx(context, 5.0f);
        ViewTreeObserver vto = getViewTreeObserver();
        if (vto != null) {
            vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    AsymmetricGridView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    AsymmetricGridView.this.determineColumns();
                    if (AsymmetricGridView.this.gridAdapter != null) {
                        AsymmetricGridView.this.gridAdapter.recalculateItemsPerRow();
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    protected void fireOnItemClick(int position, View v) {
        if (this.onItemClickListener != null) {
            this.onItemClickListener.onItemClick(this, v, position, (long) v.getId());
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    protected boolean fireOnItemLongClick(int position, View v) {
        if (this.onItemLongClickListener != null) {
            if (this.onItemLongClickListener.onItemLongClick(this, v, position, (long) v.getId())) {
                return true;
            }
        }
        return false;
    }

    public void setAdapter(ListAdapter adapter) {
        ListAdapter innerAdapter = adapter;
        if (innerAdapter instanceof WrapperListAdapter) {
            innerAdapter = ((WrapperListAdapter) innerAdapter).getWrappedAdapter();
            if (!(innerAdapter instanceof AsymmetricGridViewAdapterContract)) {
                throw new UnsupportedOperationException("Wrapped adapter must implement AsymmetricGridViewAdapterContract");
            }
        } else if (!(innerAdapter instanceof AsymmetricGridViewAdapterContract)) {
            throw new UnsupportedOperationException("Adapter must implement AsymmetricGridViewAdapterContract");
        }
        this.gridAdapter = (AsymmetricGridViewAdapterContract) innerAdapter;
        super.setAdapter(innerAdapter);
        this.gridAdapter.recalculateItemsPerRow();
    }

    public void setRequestedColumnWidth(int width) {
        this.requestedColumnWidth = width;
    }

    public void setRequestedColumnCount(int requestedColumnCount) {
        this.requestedColumnCount = requestedColumnCount;
    }

    public int getRequestedHorizontalSpacing() {
        return this.requestedHorizontalSpacing;
    }

    public void setRequestedHorizontalSpacing(int spacing) {
        this.requestedHorizontalSpacing = spacing;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        determineColumns();
    }

    public int determineColumns() {
        int numColumns;
        int availableSpace = getAvailableSpace();
        if (this.requestedColumnWidth > 0) {
            numColumns = (this.requestedHorizontalSpacing + availableSpace) / (this.requestedColumnWidth + this.requestedHorizontalSpacing);
        } else if (this.requestedColumnCount > 0) {
            numColumns = this.requestedColumnCount;
        } else {
            numColumns = 2;
        }
        if (numColumns <= 0) {
            numColumns = 1;
        }
        this.numColumns = numColumns;
        return numColumns;
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.allowReordering = this.allowReordering;
        ss.debugging = this.debugging;
        ss.numColumns = this.numColumns;
        ss.requestedColumnCount = this.requestedColumnCount;
        ss.requestedColumnWidth = this.requestedColumnWidth;
        ss.requestedHorizontalSpacing = this.requestedHorizontalSpacing;
        if (this.gridAdapter != null) {
            ss.adapterState = this.gridAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.allowReordering = ss.allowReordering;
            this.debugging = ss.debugging;
            this.numColumns = ss.numColumns;
            this.requestedColumnCount = ss.requestedColumnCount;
            this.requestedColumnWidth = ss.requestedColumnWidth;
            this.requestedHorizontalSpacing = ss.requestedHorizontalSpacing;
            if (this.gridAdapter != null) {
                this.gridAdapter.restoreState(ss.adapterState);
            }
            setSelectionFromTop(20, 0);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public int getNumColumns() {
        return this.numColumns;
    }

    public int getColumnWidth() {
        return (getAvailableSpace() - ((this.numColumns - 1) * this.requestedHorizontalSpacing)) / this.numColumns;
    }

    public int getAvailableSpace() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public boolean isAllowReordering() {
        return this.allowReordering;
    }

    public void setAllowReordering(boolean allowReordering) {
        this.allowReordering = allowReordering;
        if (this.gridAdapter != null) {
            this.gridAdapter.recalculateItemsPerRow();
        }
    }

    public boolean isDebugging() {
        return this.debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }
}
