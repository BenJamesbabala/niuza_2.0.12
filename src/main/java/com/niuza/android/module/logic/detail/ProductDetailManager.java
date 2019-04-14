package com.niuza.android.module.logic.detail;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.niuza.android.NZApplication;
import com.niuza.android.module.UrlConstance;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.entity.ProductDetail;
import com.niuza.android.module.logic.AbstractDataManager;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.gson.GsonProvider;
import com.niuza.android.module.logic.gson.ProductDetailResponse;
import com.niuza.android.module.logic.gson.ProductResponse.ProductItem;
//import com.xunlei.library.utils.XLLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import my.base.util.LogUtils;

public class ProductDetailManager extends AbstractDataManager<ProductDetail> {
    private static ProductDetailManager instance;
    private HashMap<Long, ProductDetail> productDetailList = new HashMap();

    public interface FeedbackListener {
        void onFeedbackResult(int i, String str);
    }

    public static ProductDetailManager getInstance() {
        if (instance == null) {
            synchronized (ProductDetailManager.class) {
                if (instance == null) {
                    instance = new ProductDetailManager();
                }
            }
        }
        return instance;
    }

    public void requestDetail(Product product, IProductDetailResponse response) {
        if (this.productDetailList.get(Long.valueOf(product.id)) == null) {
            getDetail(product, response);
        } else {
            response.onResponse(0, "", (ProductDetail) this.productDetailList.get(Long.valueOf(product.id)));
        }
    }

    private void getDetail(final Product product, final IProductDetailResponse response) {
        NZApplication.getApp().getNetQueue().add(new JsonRequest<String>(0, getUrl() + "&id=" + product.id, "", new Listener<String>() {
            public void onResponse(String s) {
//                XLLog.d(ProductDetailManager.class.getSimpleName(), "content=" + s);
                ProductDetailResponse productDetailResponse = null;
                try {
                    productDetailResponse = (ProductDetailResponse) GsonProvider.getInstance().getGson().fromJson(s, ProductDetailResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productDetailResponse == null) {
                    if (response != null) {
                        response.onResponse(-1, "error", null);
                    }
                } else if (response != null) {
                    ProductDetail productDetail = new ProductDetail(productDetailResponse.product);
                    if (productDetailResponse.product.others != null) {
                        List<Product> otherProducts = new ArrayList(productDetailResponse.product.others.size());
                        for (ProductItem item : productDetailResponse.product.others) {
                            otherProducts.add(new Product(item));
                        }
                        productDetail.others = otherProducts;
                    }
                    ProductDetailManager.this.productDetailList.put(Long.valueOf(product.id), productDetail);
                    response.onResponse(productDetailResponse.rtn, "", productDetail);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.w("DataManager", "error:" + volleyError.getMessage());
                if (response != null) {
                    response.onResponse(-1, volleyError.getMessage(), null);
                }
            }
        }) {
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                return AbstractDataManager.handleNetResponse(networkResponse);
            }
        });
    }

    public String getUrl() {
        return UrlConstance.getProductDetailUrl();
    }

    public void refreshData(IDataResponse<ProductDetail> iDataResponse) {
    }

    public void loadMoreData(IDataResponse<ProductDetail> iDataResponse) {
    }

    public void feedback(ProductDetail product, final FeedbackListener listener) {
        NZApplication.getApp().getNetQueue().add(new JsonRequest<String>(0, UrlConstance.getFeedbackUrl() + "&id=" + product.id, "", new Listener<String>() {
            public void onResponse(String s) {
//                XLLog.d(ProductDetailManager.class.getSimpleName(), "content=" + s);
                if (listener != null) {
                    listener.onFeedbackResult(0, "");
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.w("DataManager", "error:" + volleyError.getMessage());
                if (listener != null) {
                    listener.onFeedbackResult(-1, volleyError.getMessage());
                }
            }
        }) {
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                return AbstractDataManager.handleNetResponse(networkResponse);
            }
        });
    }
}
