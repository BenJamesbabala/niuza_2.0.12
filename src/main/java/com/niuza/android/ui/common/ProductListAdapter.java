package com.niuza.android.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.niuza.android.R;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.logic.ProductDataManager;
import com.niuza.android.ui.detail.DetailActivity;
import com.niuza.android.utils.ImageLoaderUtil;
import com.niuza.android.utils.TimeUtil;
import java.util.List;

public class ProductListAdapter extends BaseAdapter implements OnItemClickListener {
    private ProductListAdapterCallback loadMoreCallback;
    private View loadingMoreView;
    private Context mContext;
    private ProductDataManager mDataManager;
    private List<Product> mProducts;

    static class ViewHolder {
        TextView hotTextView;
        ImageView imageView;
        TextView subTitleTextView;
        TextView titleTextView;

        ViewHolder() {
        }
    }

    public void setDataManager(ProductDataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public List<Product> getProducts() {
        return this.mProducts;
    }

    public void setProducts(List<Product> products) {
        this.mProducts = products;
    }

    public void setLoadMoreCallback(ProductListAdapterCallback loadMoreCallback) {
        this.loadMoreCallback = loadMoreCallback;
    }

    public ProductListAdapter(Context context) {
        this.mContext = context;
    }

    public void addProducts(List<Product> productList) {
        if (productList != null) {
            if (this.mProducts != null) {
                this.mProducts.addAll(productList);
            } else {
                this.mProducts = productList;
            }
        }
    }

    public int getCount() {
        if (this.mProducts == null || this.mProducts.size() <= 0) {
            return 0;
        }
        return this.mProducts.size() + 1;
    }

    public Product getItem(int position) {
        if (position <= this.mProducts.size() - 1) {
            return (Product) this.mProducts.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            return getLoadingMoreView(position, convertView, parent);
        }
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_product, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.productImage);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleText);
            viewHolder.subTitleTextView = (TextView) convertView.findViewById(R.id.subTitleText);
            viewHolder.hotTextView = (TextView) convertView.findViewById(R.id.hotText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product product = (Product) this.mProducts.get(position);
        ImageLoaderUtil.showImageWithUrl(product.imgUrl, viewHolder.imageView);
        viewHolder.titleTextView.setText(product.title);
        viewHolder.subTitleTextView.setText(TimeUtil.getTimeString(product.hotTime));
        viewHolder.hotTextView.setText("人气：" + product.hot + "°C");
        if (position == (getCount() - 6)) {
            if (this.loadMoreCallback != null && this.loadMoreCallback.hasMoreData()) {
                this.loadMoreCallback.loadMoreData();
            }
        }
        return convertView;
    }

    private View getLoadingMoreView(int position, View convertView, ViewGroup parent) {
        if (this.loadingMoreView == null) {
            this.loadingMoreView = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_loading, null);
        }
        ProgressBar progressBar = (ProgressBar) this.loadingMoreView.findViewById(R.id.loadingProgressBar);
        TextView loadingText = (TextView) this.loadingMoreView.findViewById(R.id.loadingText);
        if (this.loadMoreCallback == null || !this.loadMoreCallback.hasMoreData()) {
            progressBar.setVisibility(View.GONE);
            loadingText.setText("没有更多了~");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            loadingText.setText("加载中...");
        }
        return this.loadingMoreView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (position > 0) {
            position--;
        }
        Product product = getItem(position);
        if (product != null) {
            DetailActivity.goToDetail(this.mContext, product, this.mDataManager);
        }
    }
}
