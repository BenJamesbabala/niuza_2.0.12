package my.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PageListView extends ListView implements OnScrollListener {
    public PageListView(Context context) {
        super(context);
    }

    public PageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(ListAdapter adapter) {
        setOnScrollListener(this);
        super.setAdapter(adapter);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
}
