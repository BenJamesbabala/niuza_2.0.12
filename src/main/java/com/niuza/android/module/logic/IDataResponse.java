package com.niuza.android.module.logic;

import java.util.List;

public interface IDataResponse<T> {
    void onResponse(int i, String str, List<T> list);
}
