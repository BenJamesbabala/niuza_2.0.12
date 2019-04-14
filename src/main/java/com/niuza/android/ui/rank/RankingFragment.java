package com.niuza.android.ui.rank;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.niuza.android.R;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.RankDataManager;
import com.niuza.android.ui.common.NZBaseFragment;
import com.niuza.android.ui.common.ProductListAdapter;
import com.niuza.android.ui.common.ProductListAdapterCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xunlei.library.pulltorefresh.PullToRefreshBase;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xunlei.library.pulltorefresh.PullToRefreshListView;
import java.util.List;

public class RankingFragment extends NZBaseFragment implements ProductListAdapterCallback, ObservableScrollViewCallbacks {
    private ProductListAdapter mAdapter;
    private RankDataManager mDataManager = RankDataManager.getInstance();
    private PullToRefreshListView mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAdapter = new ProductListAdapter(getActivity());
        this.mAdapter.setLoadMoreCallback(this);
        this.mAdapter.setProducts(this.mDataManager.getDataList());
        this.mAdapter.setDataManager(this.mDataManager);
        refreshData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mListView = (PullToRefreshListView) view.findViewById(R.id.refreshList);
        this.mListView.setMode(Mode.PULL_FROM_START);
        this.mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                RankingFragment.this.refreshData();
            }
        });
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this.mAdapter);
        ((ListView) this.mListView.getRefreshableView()).setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 2) {
                    ImageLoader.getInstance().pause();
                } else {
                    ImageLoader.getInstance().resume();
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void setObservableScrollEnable(boolean enable) {
        if (this.mListView != null) {
            ObservableScrollViewCallbacks observableScrollViewCallbacks = this;
            ObservableListView listView = (ObservableListView) this.mListView.getRefreshableView();
            if (!enable) {
                observableScrollViewCallbacks = null;
            }
            listView.setScrollViewCallbacks(observableScrollViewCallbacks);
        }
    }

    public void onResume() {
        super.onResume();
        this.mAdapter.notifyDataSetChanged();
    }

    public boolean hasMoreData() {
        return this.mDataManager.hasMoreData();
    }

    public void loadMoreData() {
        if (this.mDataManager.hasMoreData() && !this.mDataManager.isLoadingData()) {
            this.mDataManager.loadMoreData(new IDataResponse<Product>() {
                public void onResponse(int code, String msg, List<Product> dataList) {
                    if (code == 0 && dataList != null) {
                        RankingFragment.this.mAdapter.addProducts(dataList);
                    }
                    RankingFragment.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void refreshData() {
        if (!this.mDataManager.isLoadingData()) {
            this.mDataManager.refreshData(new IDataResponse<Product>() {
                public void onResponse(int code, String msg, List<Product> dataList) {
                    if (code != 0 || dataList == null) {
                        if (RankingFragment.this.getActivity() != null) {
                            Toast.makeText(RankingFragment.this.getActivity(), "获取数据失败", 0).show();
                        }
                        if (RankingFragment.this.mDataManager.getDataList().size() == 0) {
                            RankingFragment.this.addEmptyView();
                            if (RankingFragment.this.mEmptyView != null) {
                                RankingFragment.this.mEmptyView.setEmptyText("加载数据失败，请检查网络");
                            }
                        }
                    } else {
                        RankingFragment.this.mAdapter.setProducts(dataList);
                        RankingFragment.this.removeEmptyView();
                    }
                    if (RankingFragment.this.mListView != null) {
                        RankingFragment.this.mListView.onRefreshComplete();
                    }
                    RankingFragment.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    public void onDownMotionEvent() {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        int lastP = this.mListView.getLastVisiblePosition();
        ActionBar ab = getActivity().getActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
            hideToTopBtn();
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
            if (lastP > 20) {
                showToTopBtn();
            } else {
                hideToTopBtn();
            }
        }
    }

    public void scrollToTop() {
        this.mListView.setSelection(0);
    }
}
