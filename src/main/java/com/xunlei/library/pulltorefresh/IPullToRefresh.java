package com.xunlei.library.pulltorefresh;

import android.view.View;
import android.view.animation.Interpolator;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.State;

public interface IPullToRefresh<T extends View> {
    boolean demo();

    Mode getCurrentMode();

    boolean getFilterTouchEvents();

    ILoadingLayout getLoadingLayoutProxy();

    ILoadingLayout getLoadingLayoutProxy(boolean z, boolean z2);

    Mode getMode();

    T getRefreshableView();

    boolean getShowViewWhileRefreshing();

    State getState();

    boolean isPullToRefreshEnabled();

    boolean isPullToRefreshOverScrollEnabled();

    boolean isRefreshing();

    boolean isScrollingWhileRefreshingEnabled();

    void onRefreshComplete();

    void setFilterTouchEvents(boolean z);

    void setMode(Mode mode);

    void setOnPullEventListener(OnPullEventListener<T> onPullEventListener);

    void setOnRefreshListener(OnRefreshListener2<T> onRefreshListener2);

    void setOnRefreshListener(OnRefreshListener<T> onRefreshListener);

    void setPullToRefreshOverScrollEnabled(boolean z);

    void setRefreshing();

    void setRefreshing(boolean z);

    void setScrollAnimationInterpolator(Interpolator interpolator);

    void setScrollingWhileRefreshingEnabled(boolean z);

    void setShowViewWhileRefreshing(boolean z);
}
