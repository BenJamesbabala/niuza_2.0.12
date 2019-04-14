package com.xunlei.library.asymmetricgridview.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.niuza.android.R;
import com.xunlei.library.asymmetricgridview.AsymmetricGridViewAdapterContract;
import com.xunlei.library.asymmetricgridview.AsyncTaskCompat;
import com.xunlei.library.asymmetricgridview.Utils;
import com.xunlei.library.asymmetricgridview.model.AsymmetricItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AsymmetricGridViewAdapter<T extends AsymmetricItem> extends BaseAdapter implements OnClickListener, OnLongClickListener, AsymmetricGridViewAdapterContract {
    private static final String TAG = "AGA";
    private ProcessRowsTask asyncTask;
    protected final Context context;
    protected final List<AsymmetricItem> items;
    private Map<Integer, RowInfo<AsymmetricItem>> itemsPerRow = new HashMap();
    private final ViewPool<IcsLinearLayout> linearLayoutPool;
    protected final AsymmetricGridView listView;
    private final ViewPool<View> viewPool = new ViewPool();

    class ProcessRowsTask extends AsyncTaskCompat<List<AsymmetricItem>, Void, List<RowInfo<AsymmetricItem>>> {
        ProcessRowsTask() {
        }

        protected final List<RowInfo<AsymmetricItem>> doInBackground(List<AsymmetricItem>... params) {
            return calculateItemsPerRow(0, params[0]);
        }

        protected void onPostExecute(List<RowInfo<AsymmetricItem>> rows) {
            for (RowInfo<AsymmetricItem> row : rows) {
                AsymmetricGridViewAdapter.this.itemsPerRow.put(Integer.valueOf(AsymmetricGridViewAdapter.this.getRowCount()), row);
            }
            if (AsymmetricGridViewAdapter.this.listView.isDebugging()) {
                for (Entry<Integer, RowInfo<AsymmetricItem>> e : AsymmetricGridViewAdapter.this.itemsPerRow.entrySet()) {
                    Log.d(AsymmetricGridViewAdapter.TAG, "row: " + e.getKey() + ", items: " + ((RowInfo) e.getValue()).getItems().size());
                }
            }
            AsymmetricGridViewAdapter.this.notifyDataSetChanged();
        }

        private List<RowInfo<AsymmetricItem>> calculateItemsPerRow(int currentRow, List<AsymmetricItem> itemsToAdd) {
            List<RowInfo<AsymmetricItem>> rows = new ArrayList();
            while (!itemsToAdd.isEmpty()) {
                RowInfo<AsymmetricItem> stuffThatFit = AsymmetricGridViewAdapter.this.calculateItemsForRow(itemsToAdd);
                List<AsymmetricItem> itemsThatFit = stuffThatFit.getItems();
                if (itemsThatFit.isEmpty()) {
                    break;
                }
                for (AsymmetricItem anItemsThatFit : itemsThatFit) {
                    itemsToAdd.remove(anItemsThatFit);
                }
                rows.add(stuffThatFit);
                currentRow++;
            }
            return rows;
        }
    }

    public abstract View getActualView(int i, View view, ViewGroup viewGroup);

    public AsymmetricGridViewAdapter(Context context, AsymmetricGridView listView, List<AsymmetricItem> items) {
        this.linearLayoutPool = new ViewPool(new LinearLayoutPoolObjectFactory(context));
        this.items = items;
        this.context = context;
        this.listView = listView;
    }

    protected int getRowHeight(AsymmetricItem item) {
        return getRowHeight(item.getRowSpan());
    }

    public AsymmetricItem getItem(int position) {
        return (AsymmetricItem) this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    protected int getRowHeight(int rowSpan) {
        return ((rowSpan - 1) * this.listView.getDividerHeight()) + (this.listView.getColumnWidth() * rowSpan);
    }

    protected int getRowWidth(AsymmetricItem item) {
        return getRowWidth(item.getColumnSpan());
    }

    protected int getRowWidth(int columnSpan) {
        return Math.min(((columnSpan - 1) * this.listView.getRequestedHorizontalSpacing()) + (this.listView.getColumnWidth() * columnSpan), Utils.getScreenWidth(this.context));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.listView.isDebugging()) {
            Log.d(TAG, "getView(" + String.valueOf(position) + ")");
        }
        LinearLayout layout = findOrInitializeLayout(convertView);
        RowInfo<AsymmetricItem> rowInfo = (RowInfo) this.itemsPerRow.get(Integer.valueOf(position));
        List<AsymmetricItem> rowItems = new ArrayList();
        rowItems.addAll(rowInfo.getItems());
        int columnIndex = 0;
        int currentIndex = 0;
        int spaceLeftInColumn = rowInfo.getRowHeight();
        while (!rowItems.isEmpty() && columnIndex < this.listView.getNumColumns()) {
            AsymmetricItem currentItem = (AsymmetricItem) rowItems.get(currentIndex);
            if (spaceLeftInColumn != 0) {
                if (spaceLeftInColumn < currentItem.getRowSpan()) {
                    if (currentIndex >= rowItems.size() - 1) {
                        break;
                    }
                    currentIndex++;
                } else {
                    rowItems.remove(currentItem);
                    int index = this.items.indexOf(currentItem);
                    LinearLayout childLayout = findOrInitializeChildLayout(layout, columnIndex);
                    View v = getActualView(index, this.viewPool.get(), parent);
                    v.setTag(currentItem);
                    v.setOnClickListener(this);
                    v.setOnLongClickListener(this);
                    spaceLeftInColumn -= currentItem.getRowSpan();
                    currentIndex = 0;
                    v.setLayoutParams(new LayoutParams(getRowWidth(currentItem), getRowHeight(currentItem)));
                    childLayout.addView(v);
                }
            } else {
                columnIndex++;
                currentIndex = 0;
                spaceLeftInColumn = rowInfo.getRowHeight();
            }
        }
        if (this.listView.isDebugging() && position % 20 == 0) {
            Log.d(TAG, this.linearLayoutPool.getStats("LinearLayout"));
            Log.d(TAG, this.viewPool.getStats("Views"));
        }
        return layout;
    }

    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt("totalItems", this.items.size());
        for (int i = 0; i < this.items.size(); i++) {
            bundle.putParcelable("item_" + i, (Parcelable) this.items.get(i));
        }
        return bundle;
    }

    public void restoreState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
            int totalItems = bundle.getInt("totalItems");
            List<AsymmetricItem> tmpItems = new ArrayList();
            for (int i = 0; i < totalItems; i++) {
                tmpItems.add((AsymmetricItem) bundle.getParcelable("item_" + i));
            }
            setItems(tmpItems);
        }
    }

    private IcsLinearLayout findOrInitializeLayout(View convertView) {
        IcsLinearLayout layout;
        if (convertView == null || !(convertView instanceof IcsLinearLayout)) {
            layout = new IcsLinearLayout(this.context, null);
            if (this.listView.isDebugging()) {
                layout.setBackgroundColor(Color.parseColor("#83F27B"));
            }
            layout.setShowDividers(2);
            layout.setDividerDrawable(this.context.getResources().getDrawable(R.drawable.asymmetricgridview_item_divider_horizontal));
            layout.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
        } else {
            layout = (IcsLinearLayout) convertView;
        }
        for (int j = 0; j < layout.getChildCount(); j++) {
            IcsLinearLayout tempChild = (IcsLinearLayout) layout.getChildAt(j);
            this.linearLayoutPool.put(tempChild);
            for (int k = 0; k < tempChild.getChildCount(); k++) {
                this.viewPool.put(tempChild.getChildAt(k));
            }
            tempChild.removeAllViews();
        }
        layout.removeAllViews();
        return layout;
    }

    private IcsLinearLayout findOrInitializeChildLayout(LinearLayout parentLayout, int childIndex) {
        IcsLinearLayout childLayout = (IcsLinearLayout) parentLayout.getChildAt(childIndex);
        if (childLayout == null) {
            childLayout = (IcsLinearLayout) this.linearLayoutPool.get();
            childLayout.setOrientation(1);
            if (this.listView.isDebugging()) {
                childLayout.setBackgroundColor(Color.parseColor("#837BF2"));
            }
            childLayout.setShowDividers(2);
            childLayout.setDividerDrawable(this.context.getResources().getDrawable(R.drawable.asymmetricgridview_item_divider_vertical));
            childLayout.setLayoutParams(new AbsListView.LayoutParams(-2, -1));
            parentLayout.addView(childLayout);
        }
        return childLayout;
    }

    public void setItems(List<AsymmetricItem> newItems) {
        this.linearLayoutPool.clear();
        this.viewPool.clear();
        this.items.clear();
        this.items.addAll(newItems);
        recalculateItemsPerRow();
        notifyDataSetChanged();
    }

    public void appendItems(List<AsymmetricItem> newItems) {
        this.items.addAll(newItems);
        RowInfo<AsymmetricItem> rowInfo = null;
        int lastRow = getRowCount() - 1;
        if (lastRow >= 0) {
            rowInfo = (RowInfo) this.itemsPerRow.get(Integer.valueOf(lastRow));
        }
        if (rowInfo != null) {
            float spaceLeftInLastRow = rowInfo.getSpaceLeft();
            if (this.listView.isDebugging()) {
                Log.d(TAG, "Space left in last row: " + spaceLeftInLastRow);
            }
            if (spaceLeftInLastRow > 0.0f) {
                for (AsymmetricItem i : rowInfo.getItems()) {
                    newItems.add(0, i);
                }
                RowInfo<AsymmetricItem> stuffThatFit = calculateItemsForRow(newItems);
                List<AsymmetricItem> itemsThatFit = stuffThatFit.getItems();
                if (!itemsThatFit.isEmpty()) {
                    for (AsymmetricItem anItemsThatFit : itemsThatFit) {
                        newItems.remove(anItemsThatFit);
                    }
                    this.itemsPerRow.put(Integer.valueOf(lastRow), stuffThatFit);
                    notifyDataSetChanged();
                }
            }
        }
        this.asyncTask = new ProcessRowsTask();
        this.asyncTask.executeSerially(newItems);
    }

    public void onClick(View v) {
        this.listView.fireOnItemClick(this.items.indexOf((AsymmetricItem) v.getTag()), v);
    }

    public boolean onLongClick(View v) {
        return this.listView.fireOnItemLongClick(this.items.indexOf((AsymmetricItem) v.getTag()), v);
    }

    public int getCount() {
        return getRowCount();
    }

    public int getRowCount() {
        return this.itemsPerRow.size();
    }

    public void recalculateItemsPerRow() {
        if (this.asyncTask != null) {
            this.asyncTask.cancel(true);
        }
        this.linearLayoutPool.clear();
        this.viewPool.clear();
        this.itemsPerRow.clear();
        ArrayList itemsToAdd = new ArrayList();
        itemsToAdd.addAll(this.items);
        this.asyncTask = new ProcessRowsTask();
        this.asyncTask.executeSerially(itemsToAdd);
    }

    private RowInfo<AsymmetricItem> calculateItemsForRow(List<AsymmetricItem> items) {
        return calculateItemsForRow(items, (float) this.listView.getNumColumns());
    }

    private RowInfo<AsymmetricItem> calculateItemsForRow(List<AsymmetricItem> items, float initialSpaceLeft) {
        List<AsymmetricItem> itemsThatFit = new ArrayList();
        int rowHeight = 1;
        float areaLeft = initialSpaceLeft;
        int currentItem = 0;
        while (areaLeft > 0.0f && currentItem < items.size()) {
            int currentItem2 = currentItem + 1;
            AsymmetricItem item = (AsymmetricItem) items.get(currentItem);
            float itemArea = (float) (item.getRowSpan() * item.getColumnSpan());
            if (this.listView.isDebugging()) {
                Log.d(TAG, String.format("item %s in row with height %s consumes %s area", new Object[]{item, Integer.valueOf(rowHeight), Float.valueOf(itemArea)}));
            }
            if (rowHeight < item.getRowSpan()) {
                itemsThatFit.clear();
                rowHeight = item.getRowSpan();
                currentItem2 = 0;
                areaLeft = initialSpaceLeft * ((float) item.getRowSpan());
            } else if (areaLeft >= itemArea) {
                areaLeft -= itemArea;
                itemsThatFit.add(item);
            } else if (!this.listView.isAllowReordering()) {
                break;
            }
            currentItem = currentItem2;
        }
        return new RowInfo(rowHeight, itemsThatFit, areaLeft);
    }
}
