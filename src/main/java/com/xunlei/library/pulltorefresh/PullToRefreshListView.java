package com.xunlei.library.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.niuza.android.R;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.AnimationStyle;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Orientation;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.State;
import com.xunlei.library.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.xunlei.library.pulltorefresh.internal.LoadingLayout;

public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {
    private LoadingLayout mFooterLoadingView;
    private LoadingLayout mHeaderLoadingView;
    private boolean mListViewExtrasEnabled;
    private FrameLayout mLvFooterLoadingFrame;

    protected class InternalListView extends ObservableListView implements EmptyViewMethodAccessor {
        private boolean mAddedLvFooter = false;

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        protected void dispatchDraw(Canvas canvas) {
            try {
                super.dispatchDraw(canvas);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            try {
                return super.dispatchTouchEvent(ev);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void setAdapter(ListAdapter adapter) {
            if (!(PullToRefreshListView.this.mLvFooterLoadingFrame == null || this.mAddedLvFooter)) {
                addFooterView(PullToRefreshListView.this.mLvFooterLoadingFrame, null, false);
                this.mAddedLvFooter = true;
            }
            super.setAdapter(adapter);
        }

        public void setEmptyView(View emptyView) {
            PullToRefreshListView.this.setEmptyView(emptyView);
        }

        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    @TargetApi(9)
    final class InternalListViewSDK9 extends InternalListView {
        public InternalListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
            OverscrollHelper.overScrollBy(PullToRefreshListView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);
            return returnValue;
        }
    }

    public PullToRefreshListView(Context context) {
        super(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    protected void onRefreshing(boolean doScroll) {
        ListAdapter adapter = ((ListView) this.mRefreshableView).getAdapter();
        if (this.mListViewExtrasEnabled && getShowViewWhileRefreshing() && adapter != null) {
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
                    selection = ((ListView) this.mRefreshableView).getCount() - 1;
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
                ((ListView) this.mRefreshableView).setSelection(selection);
                smoothScrollTo(0);
                return;
            }
            return;
        }
        super.onRefreshing(doScroll);
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
                    selection = ((ListView) this.mRefreshableView).getCount() - 1;
                    scrollToHeight = getFooterSize();
                    if (Math.abs(((ListView) this.mRefreshableView).getLastVisiblePosition() - selection) > 1) {
                        scrollLvToEdge = false;
                    }
                    break;
                default:
                    originalLoadingLayout = getHeaderLayout();
                    listViewLoadingLayout = this.mHeaderLoadingView;
                    scrollToHeight = -getHeaderSize();
                    selection = 0;
                    if (Math.abs(((ListView) this.mRefreshableView).getFirstVisiblePosition() - 0) > 1) {
                        scrollLvToEdge = false;
                        break;
                    }
                    break;
            }
            if (listViewLoadingLayout.getVisibility() == 0) {
                originalLoadingLayout.showInvisibleViews();
                listViewLoadingLayout.setVisibility(View.GONE);
                if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
                    ((ListView) this.mRefreshableView).setSelection(selection);
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

    protected ListView createListView(Context context, AttributeSet attrs) {
        if (VERSION.SDK_INT >= 9) {
            return new InternalListViewSDK9(context, attrs);
        }
        return new InternalListView(context, attrs);
    }

    public void setSelection(int position) {
        if (getRefreshableView() instanceof ListView) {
            ((ListView) getRefreshableView()).setSelection(position);
        }
    }

    public void setSelectionFromTop(int position, int y) {
        if (getRefreshableView() instanceof ListView) {
            ((ListView) getRefreshableView()).setSelectionFromTop(position, y);
        }
    }

    public int getLastVisiblePosition() {
        if (getRefreshableView() instanceof ListView) {
            return ((ListView) getRefreshableView()).getLastVisiblePosition();
        }
        return -1;
    }

    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView lv = createListView(context, attrs);
        lv.setId(16908298);
        return lv;
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
            ((ListView) this.mRefreshableView).addHeaderView(frame, null, false);
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
