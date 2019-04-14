package com.niuza.android.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.niuza.android.R;
import com.niuza.android.module.entity.Category;
import java.util.List;
import org.szuwest.utils.ScreenUtils;

public class CategoryAdapter extends BaseAdapter implements OnItemClickListener {
    private int columnWidth = ((ScreenUtils.getScreenWidth() / 3) - 1);
    private List<Category> mCategories;
    Context mContext;

    public CategoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setCategories(List<Category> list) {
        this.mCategories = list;
    }

    public int getCount() {
        if (this.mCategories != null) {
            return this.mCategories.size();
        }
        return 0;
    }

    public Category getItem(int position) {
        return (Category) this.mCategories.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listitem_category, null);
            convertView.setLayoutParams(new LayoutParams(this.columnWidth, this.columnWidth));
        }
        Category category = getItem(position);
        ((TextView) convertView.findViewById(R.id.tv_categoryName)).setText(category.categoryName);
        ((ImageView) convertView.findViewById(R.id.imageIcon)).setImageResource(category.iconResId);
        return convertView;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Category category = getItem(position);
        if (category.categoryId != 0) {
            CategoryProductActivity.goToCategoryProduct(this.mContext, category);
        }
    }
}
