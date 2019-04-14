package com.xunlei.library.asymmetricgridview;

import android.os.Parcelable;

public interface AsymmetricGridViewAdapterContract {
    void recalculateItemsPerRow();

    void restoreState(Parcelable parcelable);

    Parcelable saveState();
}
