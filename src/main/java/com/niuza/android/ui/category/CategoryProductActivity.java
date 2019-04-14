package com.niuza.android.ui.category;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.niuza.android.R;
import com.niuza.android.module.entity.Category;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.category.CategoryDataManager;
import com.niuza.android.ui.common.NABaseActivity;
import com.niuza.android.ui.common.ProductListAdapter;
import com.niuza.android.ui.common.ProductListAdapterCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xunlei.library.pulltorefresh.PullToRefreshBase;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xunlei.library.pulltorefresh.PullToRefreshListView;
import java.util.List;

public class CategoryProductActivity extends NABaseActivity implements ProductListAdapterCallback, ObservableScrollViewCallbacks {
    private ProductListAdapter mAdapter;
    private CategoryDataManager mDataManager;
    private PullToRefreshListView mListView;

    public static void goToCategoryProduct(Context context, Category category) {
        Intent i = new Intent(context, CategoryProductActivity.class);
        i.putExtra("categoryId", category.categoryId);
        i.putExtra("categoryName", category.categoryName);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryproduct);
        String categoryName = getIntent().getStringExtra("categoryName");
        this.mDataManager = new CategoryDataManager(getIntent().getIntExtra("categoryId", 0));
        this.mAdapter = new ProductListAdapter(this);
        this.mAdapter.setDataManager(this.mDataManager);
        this.mAdapter.setLoadMoreCallback(this);
        ActionBar actionBar = getActionBar();
        if (!(actionBar == null || categoryName == null)) {
            actionBar.setTitle(categoryName);
        }
        initView();
        showLoadingDialog("正在加载数据...");
        refreshData();
    }

    private void initView() {
        this.mListView = (PullToRefreshListView) findViewById(R.id.refreshList);
        this.mListView.setMode(Mode.PULL_FROM_START);
        this.mListView.setOnItemClickListener(this.mAdapter);
        this.mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (!CategoryProductActivity.this.mDataManager.isLoadingData()) {
                    CategoryProductActivity.this.refreshData();
                }
            }
        });
        this.mListView.setAdapter(this.mAdapter);
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
        ((ObservableListView) this.mListView.getRefreshableView()).setScrollViewCallbacks(this);
    }

    protected void onResume() {
        super.onResume();
        this.mAdapter.notifyDataSetChanged();
    }

    private void refreshData() {
        this.mDataManager.refreshData(new IDataResponse<Product>() {
            public void onResponse(int code, String msg, List<Product> dataList) {
                CategoryProductActivity.this.dismissDialog();
                CategoryProductActivity.this.mListView.onRefreshComplete();
                if (code != 0 || dataList == null) {
                    if (dataList == null || dataList.size() == 0) {
                        CategoryProductActivity.this.addEmptyView();
                        CategoryProductActivity.this.mEmptyView.setEmptyText("加载商品数据失败，请检查网络~");
                    }
                    Toast.makeText(CategoryProductActivity.this, "获取数据失败", 0).show();
                    return;
                }
                CategoryProductActivity.this.mAdapter.setProducts(dataList);
                CategoryProductActivity.this.mAdapter.notifyDataSetChanged();
                CategoryProductActivity.this.removeEmptyView();
            }
        });
    }

    public boolean hasMoreData() {
        return this.mDataManager.hasMoreData();
    }

    public void loadMoreData() {
        if (!this.mDataManager.isLoadingData() && hasMoreData()) {
            this.mDataManager.loadMoreData(new IDataResponse<Product>() {
                public void onResponse(int code, String msg, List<Product> dataList) {
                    if (code != 0 || dataList == null) {
                        Toast.makeText(CategoryProductActivity.this, "加载数据失败", 0).show();
                        return;
                    }
                    CategoryProductActivity.this.mAdapter.addProducts(dataList);
                    CategoryProductActivity.this.mAdapter.notifyDataSetChanged();
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
        if (scrollState == ScrollState.UP) {
            hideToTopBtn();
        } else if (scrollState != ScrollState.DOWN) {
        } else {
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
