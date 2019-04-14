package com.niuza.android.module.logic;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.niuza.android.NZApplication;
import com.niuza.android.module.entity.Product;
import com.niuza.android.module.logic.gson.GsonProvider;
import com.niuza.android.module.logic.gson.ProductResponse;
//import com.xunlei.library.utils.NetHelper;
import java.util.HashMap;
import java.util.List;
import my.base.util.LogUtils;
import org.szuwest.lib.BaseApplication;

public abstract class ProductDataManager extends AbstractDataManager<Product> {
    private HashMap<Long, Product> productHashMap = new HashMap();

    public void refreshData(IDataResponse<Product> response) {
        if (!this.isLoadingData) {
            this.isLoadingData = true;
            this.mStart = 0;
            getData(response);
        }
    }

    public void loadMoreData(IDataResponse<Product> response) {
        if (!this.isLoadingData) {
            this.isLoadingData = true;
            getData(response);
        }
    }

    protected void getData(final IDataResponse<Product> response) {
        String url = getUrl() + "&start=" + this.mStart + "&count=" + 30;
        final String finalUrl = url;
        JsonRequest<String> request = new JsonRequest<String>(0, url, "", new Listener<String>() {
            public void onResponse(String s) {
                ProductResponse productResponse = null;
                try {
                    productResponse = (ProductResponse) GsonProvider.getInstance().getGson().fromJson(s, ProductResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productResponse != null) {
                    if (ProductDataManager.this.mStart == 0) {
                        ProductDataManager.this.dataList.clear();
                        ProductDataManager.this.productHashMap.clear();
                    }
                    List<Product> products = Product.fromProductResponse(productResponse);
                    ProductDataManager productDataManager = ProductDataManager.this;
                    productDataManager.mStart += products.size();
                    if (products.size() < 30) {
                        ProductDataManager.this.hasMore = false;
                    } else {
                        ProductDataManager.this.hasMore = true;
                    }
                    ProductDataManager.this.addProducts(products);
                    ProductDataManager.this.isLoadingData = false;
                    if (response != null) {
                        response.onResponse(productResponse.rtn, "", products);
                    }
                } else if (response != null) {
                    response.onResponse(-1, "error", null);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.w("DataManager", "error:" + volleyError.getMessage());
                ProductDataManager.this.isLoadingData = false;
//                if (ProductDataManager.this.mStart == 0 && ProductDataManager.this.dataList.size() == 0 && !NetHelper.isNetworkAvailable(BaseApplication.getInstance())) {
                if (ProductDataManager.this.mStart == 0 && ProductDataManager.this.dataList.size() == 0) {
                    List<Product> products = ProductDataManager.this.getFromDiskCache(finalUrl);
                    if (products != null) {
                        ProductDataManager.this.dataList.clear();
                        ProductDataManager.this.productHashMap.clear();
                        ProductDataManager.this.addProducts(products);
                        if (response != null) {
                            response.onResponse(0, "", products);
                            return;
                        }
                        return;
                    }
                }
                if (response != null) {
                    response.onResponse(-1, volleyError.getMessage(), null);
                }
            }
        }) {
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                return AbstractDataManager.handleNetResponse(networkResponse);
            }
        };
        if (this.mStart == 0) {
            request.setShouldCache(true);
        }
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NZApplication.getApp().getNetQueue().add(request);
    }

    protected List<Product> getFromDiskCache(String url) {
        if (NZApplication.getApp().getNetQueue().getCache().get(url) != null) {
            try {
                return Product.fromProductResponse((ProductResponse) GsonProvider.getInstance().getGson().fromJson(new String(NZApplication.getApp().getNetQueue().getCache().get(url).data), ProductResponse.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.d("ProductDataManager", "没有缓存数据");
        }
        return null;
    }

    public Product getNextProductFrom(Product product) {
        if (product == null) {
            return null;
        }
        int index = this.dataList.indexOf(product);
        if (index == -1) {
            index = indexOf(product);
        }
        if (index == -1 || index >= this.dataList.size() - 1) {
            return null;
        }
        return (Product) this.dataList.get(index + 1);
    }

    public Product getPreProductFrom(Product product) {
        if (product == null) {
            return null;
        }
        int index = this.dataList.indexOf(product);
        if (index == -1) {
            index = indexOf(product);
        }
        if (index == -1 || index <= 0) {
            return null;
        }
        return (Product) this.dataList.get(index - 1);
    }

    public int indexOf(Product product) {
        if (product == null) {
            return -1;
        }
        for (int i = 0; i < this.dataList.size(); i++) {
            if (((Product) this.dataList.get(i)).id == product.id) {
                return i;
            }
        }
        return -1;
    }

    protected void addProducts(List<Product> productList) {
        if (productList != null) {
            int i = 0;
            while (i < productList.size()) {
                Product p = (Product) productList.get(i);
                if (this.productHashMap.get(Long.valueOf(p.id)) == null) {
                    this.productHashMap.put(Long.valueOf(p.id), p);
                    this.dataList.add(p);
                } else {
                    productList.remove(i);
                    i--;
                }
                i++;
            }
        }
    }
}
