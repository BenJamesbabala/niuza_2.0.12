package com.niuza.android.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.niuza.android.R;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.SearchDataManager;
import com.niuza.android.ui.common.NABaseActivity;
import com.niuza.android.ui.common.ProductListAdapter;
import com.niuza.android.ui.common.ProductListAdapterCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshListView;
import com.xunlei.library.utils.InputUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.szuwest.utils.ScreenUtils;

public class SearchActivity extends NABaseActivity implements ProductListAdapterCallback, ObservableScrollViewCallbacks {
    private static final String HISTORY_KEY = "HISTORY";
    private static final String SDPF = "SEARCH_HISTORY";
    private ProductListAdapter mAdapter;
    private ImageView mLeftIcon;
    private PullToRefreshListView mListView;
    private TextView mSearchBtn;
    private SearchDataManager mSearchDataManager;
    private AutoCompleteTextView mSearchEdit;

    class SearchAdapter extends ArrayAdapter<String> {
        public SearchAdapter(Context context, int resource) {
            super(context, resource);
        }

        public SearchAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public SearchAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        public SearchAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public SearchAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public SearchAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = super.getView(position, convertView, parent);
            View clearBtn = itemView.findViewById(R.id.clearBtn);
            clearBtn.setTag(getItem(position));
            clearBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String keywords = (String) v.getTag();
                    SearchActivity.this.deleteHistory(keywords);
                    SearchAdapter.this.remove(keywords);
                }
            });
            itemView.setTag(getItem(position));
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String keyword = (String) v.getTag();
                    SearchActivity.this.mSearchEdit.dismissDropDown();
                    SearchActivity.this.doSearch(keyword);
                }
            });
            return itemView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.mSearchDataManager = new SearchDataManager();
        initView();
    }

    private void initView() {
        this.mLeftIcon = (ImageView) findViewById(R.id.leftIcon);
        this.mLeftIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        this.mSearchBtn = (TextView) findViewById(R.id.rightText);
        this.mSearchBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.startSearch();
            }
        });
        this.mSearchEdit = (AutoCompleteTextView) findViewById(R.id.editText);
        this.mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 4 && (event == null || event.getKeyCode() != 66)) {
                    return false;
                }
                SearchActivity.this.startSearch();
                return true;
            }
        });
        this.mListView = (PullToRefreshListView) findViewById(R.id.refreshList);
        this.mAdapter = new ProductListAdapter(this);
        this.mAdapter.setLoadMoreCallback(this);
        this.mListView.setMode(Mode.DISABLED);
        this.mListView.setOnItemClickListener(this.mAdapter);
        this.mListView.setAdapter(this.mAdapter);
        this.mAdapter.setDataManager(this.mSearchDataManager);
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
        initAutoComplete(this.mSearchEdit);
    }

    private void startSearch() {
        String keyword = this.mSearchEdit.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入关键字", 0).show();
            return;
        }
        if (this.mSearchEdit.isPopupShowing()) {
            this.mSearchEdit.dismissDropDown();
        }
        InputUtils.hiddenInput(this, this.mSearchEdit);
        doSearch(keyword);
        saveHistory(keyword);
    }

    private void doSearch(String keyword) {
        if (!this.mSearchDataManager.isLoadingData()) {
            showLoadingDialog("正在搜索...");
            this.mSearchDataManager.searchKeyword(keyword, new IDataResponse<Product>() {
                public void onResponse(int code, String msg, List<Product> dataList) {
                    SearchActivity.this.dismissDialog();
                    if (code == 0) {
                        SearchActivity.this.mAdapter.setProducts(dataList);
                        SearchActivity.this.mAdapter.notifyDataSetChanged();
                        if (dataList == null || dataList.size() == 0) {
                            SearchActivity.this.addEmptyView();
                            SearchActivity.this.mEmptyView.setEmptyText("抱歉，没有搜索到相关商品，换别的关键词试试~");
                            return;
                        }
                        SearchActivity.this.removeEmptyView();
                        return;
                    }
                    Toast.makeText(SearchActivity.this, "搜索失败，请检测网络", 0).show();
                }
            });
        }
    }

    protected void onResume() {
        super.onResume();
        this.mAdapter.notifyDataSetChanged();
    }

    private void initAutoComplete(AutoCompleteTextView auto) {
        String longHistory = getSharedPreferences(SDPF, 0).getString(HISTORY_KEY, "");
        if (!TextUtils.isEmpty(longHistory)) {
            String[] hisArrays = longHistory.split(";");
            List arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(hisArrays));
            ArrayAdapter<String> adapter = new SearchAdapter((Context) this, (int) R.layout.listitem_search_history, (int) R.id.historyText, arrayList);
            if (hisArrays != null && hisArrays.length > 50) {
                String[] newArrays = new String[50];
                System.arraycopy(hisArrays, 0, newArrays, 0, 50);
                arrayList.clear();
                arrayList.addAll(Arrays.asList(newArrays));
                adapter = new SearchAdapter((Context) this, (int) R.layout.listitem_search_history, (int) R.id.historyText, arrayList);
            }
            auto.setAdapter(adapter);
            auto.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    SearchActivity.this.mSearchEdit.dismissDropDown();
                    SearchActivity.this.doSearch((String) SearchActivity.this.mSearchEdit.getAdapter().getItem(position));
                }
            });
            auto.setDropDownHeight(ScreenUtils.dip2px(200.0f));
            auto.setDropDownBackgroundResource(R.drawable.tab_bg);
            auto.setThreshold(1);
            auto.setDropDownVerticalOffset(3);
            auto.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    if (hasFocus && view.getWindowToken() != null) {
                        view.showDropDown();
                    }
                }
            });
        }
    }

    private void saveHistory(String keywords) {
        String text = keywords;
        SharedPreferences sp = getSharedPreferences(SDPF, 0);
        String longHistory = sp.getString(HISTORY_KEY, "nothing");
        if (longHistory.equals("nothing")) {
            sp.edit().putString(HISTORY_KEY, text).commit();
        } else if (!longHistory.contains(text + ";") && !longHistory.equals(text)) {
            StringBuilder sb = new StringBuilder(longHistory);
            sb.insert(0, text + ";");
            sp.edit().putString(HISTORY_KEY, sb.toString()).commit();
        }
    }

    private void deleteHistory(String keywords) {
        SharedPreferences sp = getSharedPreferences(SDPF, 0);
        String longHistory = sp.getString(HISTORY_KEY, "nothing");
        if (longHistory.contains(keywords + ";")) {
            longHistory = longHistory.replace(keywords + ";", "");
        } else if (longHistory.contains(keywords)) {
            longHistory = longHistory.replace(keywords, "");
        }
        sp.edit().putString(HISTORY_KEY, longHistory).commit();
    }

    public boolean hasMoreData() {
        return this.mSearchDataManager.hasMoreData();
    }

    public void loadMoreData() {
        if (!this.mSearchDataManager.isLoadingData() && this.mSearchDataManager.hasMoreData()) {
            this.mSearchDataManager.loadMoreData(new IDataResponse<Product>() {
                public void onResponse(int code, String msg, List<Product> dataList) {
                    if (code == 0) {
                        SearchActivity.this.mAdapter.addProducts(dataList);
                        SearchActivity.this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                    Toast.makeText(SearchActivity.this, "加载数据失败", 0).show();
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
