package com.niuza.android.module.logic;

import java.util.List;

public interface DataManagerInterface<T> {
    List<T> getDataList();

    String getUrl();

    boolean hasMoreData();

    boolean isLoadingData();

    void loadMoreData(IDataResponse<T> iDataResponse);

    void refreshData(IDataResponse<T> iDataResponse);
}
