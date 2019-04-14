package com.xunlei.library.asymmetricgridview.widget;

import android.view.View;

public interface PoolObjectFactory<T extends View> {
    T createObject();
}
