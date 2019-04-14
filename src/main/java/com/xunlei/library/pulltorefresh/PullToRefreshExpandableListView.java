package com.xunlei.library.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.niuza.android.R;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.AnimationStyle;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Orientation;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.State;
import com.xunlei.library.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.xunlei.library.pulltorefresh.internal.LoadingLayout;

public class PullToRefreshExpandableListView extends PullToRefreshAdapterViewBase<ExpandableListView> {
    private LoadingLayout mFooterLoadingView;
    private LoadingLayout mHeaderLoadingView;
    private boolean mListViewExtrasEnabled;
    private FrameLayout mLvFooterLoadingFrame;

    class InternalExpandableListView extends ExpandableListView implements EmptyViewMethodAccessor {
        public InternalExpandableListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setEmptyView(View emptyView) {
            PullToRefreshExpandableListView.this.setEmptyView(emptyView);
        }

        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        protected void onDetachedFromWindow() {
            try {
                super.onDetachedFromWindow();
            } catch (IllegalArgumentException e) {
            }
        }

        public void smoothScrollToPosition(int position, int boundPosition) {
        }
    }

    @TargetApi(9)
    final class InternalExpandableListViewSDK9 extends InternalExpandableListView {
        public InternalExpandableListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
            OverscrollHelper.overScrollBy(PullToRefreshExpandableListView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);
            return returnValue;
        }
    }

    public PullToRefreshExpandableListView(Context context) {
        super(context);
    }

    public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshExpandableListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshExpandableListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    protected ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
        ExpandableListView lv;
        if (VERSION.SDK_INT >= 9) {
            lv = new InternalExpandableListViewSDK9(context, attrs);
        } else {
            lv = new InternalExpandableListView(context, attrs);
        }
        lv.setId(16908298);
        return lv;
    }

    protected void onRefreshing(boolean doScroll) {
        ExpandableListAdapter adapter = ((ExpandableListView) this.mRefreshableView).getExpandableListAdapter();
        if (!this.mListViewExtrasEnabled || !getShowViewWhileRefreshing() || adapter == null || adapter.isEmpty()) {
            super.onRefreshing(doScroll);
            return;
        }
        LoadingLayout origLoadingView;
        LoadingLayout listViewLoadingView;
        LoadingLayout oppositeListViewLoadingView;
        int selection;
        int scrollToY;
        super.onRefreshing(false);
        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                origLoadingView = getFooterLayout();
                listViewLoadingView = this.mFooterLoadingView;
                oppositeListViewLoadingView = this.mHeaderLoadingView;
                selection = ((ExpandableListView) this.mRefreshableView).getCount() - 1;
                scrollToY = getScrollY() - getFooterSize();
                break;
            default:
                origLoadingView = getHeaderLayout();
                listViewLoadingView = this.mHeaderLoadingView;
                oppositeListViewLoadingView = this.mFooterLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderSize();
                break;
        }
        origLoadingView.reset();
        origLoadingView.hideAllViews();
        oppositeListViewLoadingView.setVisibility(View.GONE);
        listViewLoadingView.setVisibility(View.VISIBLE);
        listViewLoadingView.refreshing();
        if (doScroll) {
            disableLoadingLayoutVisibilityChanges();
            setHeaderScroll(scrollToY);
            ((ExpandableListView) this.mRefreshableView).setSelection(selection);
            smoothScrollTo(0);
        }
    }

    protected void onReset() {
        boolean scrollLvToEdge = true;
        if (this.mListViewExtrasEnabled) {
            LoadingLayout originalLoadingLayout;
            LoadingLayout listViewLoadingLayout;
            int selection;
            int scrollToHeight;
            switch (getCurrentMode()) {
                case MANUAL_REFRESH_ONLY:
                case PULL_FROM_END:
                    originalLoadingLayout = getFooterLayout();
                    listViewLoadingLayout = this.mFooterLoadingView;
                    selection = ((ExpandableListView) this.mRefreshableView).getCount() - 1;
                    scrollToHeight = getFooterSize();
                    if (Math.abs(((ExpandableListView) this.mRefreshableView).getLastVisiblePosition() - selection) > 1) {
                        scrollLvToEdge = false;
                    }
                    break;
                default:
                    originalLoadingLayout = getHeaderLayout();
                    listViewLoadingLayout = this.mHeaderLoadingView;
                    scrollToHeight = -getHeaderSize();
                    selection = 0;
                    if (Math.abs(((ExpandableListView) this.mRefreshableView).getFirstVisiblePosition() - 0) > 1) {
                        scrollLvToEdge = false;
                        break;
                    }
                    break;
            }
            if (listViewLoadingLayout.getVisibility() == 0) {
                originalLoadingLayout.showInvisibleViews();
                listViewLoadingLayout.setVisibility(View.GONE);
                if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
                    ((ExpandableListView) this.mRefreshableView).setSelection(selection);
                    setHeaderScroll(scrollToHeight);
                }
            }
            super.onReset();
            return;
        }
        super.onReset();
    }

    protected LoadingLayoutProxy createLoadingLayoutProxy(boolean includeStart, boolean includeEnd) {
        LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart, includeEnd);
        if (this.mListViewExtrasEnabled) {
            Mode mode = getMode();
            if (includeStart && mode.showHeaderLoadingLayout()) {
                proxy.addLayout(this.mHeaderLoadingView);
            }
            if (includeEnd && mode.showFooterLoadingLayout()) {
                proxy.addLayout(this.mFooterLoadingView);
            }
        }
        return proxy;
    }

    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);
        this.mListViewExtrasEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrListViewExtrasEnabled, true);
        if (this.mListViewExtrasEnabled) {
            LayoutParams lp = new LayoutParams(-1, -2, 1);
            FrameLayout frame = new FrameLayout(getContext());
            this.mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
            this.mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(this.mHeaderLoadingView, lp);
            ((ExpandableListView) this.mRefreshableView).addHeaderView(frame, null, false);
            this.mLvFooterLoadingFrame = new FrameLayout(getContext());
            this.mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
            this.mFooterLoadingView.setVisibility(View.GONE);
            this.mLvFooterLoadingFrame.addView(this.mFooterLoadingView, lp);
            if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled)) {
                setScrollingWhileRefreshingEnabled(true);
            }
        }
    }
}
