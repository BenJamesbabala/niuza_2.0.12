package com.niuza.android.module.logic.detail;

import com.niuza.android.module.entity.ProductDetail;

public interface IProductDetailResponse {
    void onResponse(int i, String str, ProductDetail productDetail);
}
