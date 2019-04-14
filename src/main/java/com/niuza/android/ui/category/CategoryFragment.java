package com.niuza.android.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import com.niuza.android.R;
import com.niuza.android.module.entity.Category;
import com.niuza.android.module.logic.IDataResponse;
import com.niuza.android.module.logic.category.CategoryManager;
import java.util.List;
import org.szuwest.lib.BaseFragment;

public class CategoryFragment extends BaseFragment {
    CategoryAdapter mAdapter;
    CategoryManager mDataManager = new CategoryManager();
    GridView mGridView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAdapter = new CategoryAdapter(getActivity());
        refreshData();
    }

    private void refreshData() {
        this.mDataManager.refreshData(new IDataResponse<Category>() {
            public void onResponse(int code, String msg, List<Category> dataList) {
                if (dataList != null) {
                    CategoryFragment.this.mAdapter.setCategories(dataList);
                    CategoryFragment.this.mAdapter.notifyDataSetChanged();
                } else if (code != 0 && CategoryFragment.this.getActivity() != null) {
                    Toast.makeText(CategoryFragment.this.getActivity(), "获取数据失败", 0).show();
                }
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mGridView = (GridView) view.findViewById(R.id.categoryGrid);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.setNumColumns(3);
        this.mGridView.setVerticalSpacing(1);
        this.mGridView.setHorizontalSpacing(1);
        this.mGridView.setStretchMode(2);
        this.mGridView.setOnItemClickListener(this.mAdapter);
    }
}
