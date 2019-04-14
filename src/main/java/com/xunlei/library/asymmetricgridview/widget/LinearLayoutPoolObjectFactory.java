package com.xunlei.library.asymmetricgridview.widget;

import android.content.Context;

public class LinearLayoutPoolObjectFactory implements PoolObjectFactory<IcsLinearLayout> {
    private final Context context;

    public LinearLayoutPoolObjectFactory(Context context) {
        this.context = context;
    }

    public IcsLinearLayout createObject() {
        return new IcsLinearLayout(this.context, null);
    }
}
