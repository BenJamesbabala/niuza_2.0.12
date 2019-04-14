package com.xunlei.library.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.niuza.android.R;

public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    private static final CharSequence EMPTY_TITLE = "";
    private OnPageChangeListener mListener;
    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private final OnClickListener mTabClickListener;
    private final IcsLinearLayout mTabLayout;
    private OnTabReselectedListener mTabReselectedListener;
    private Runnable mTabSelector;
    private ViewPager mViewPager;
    private boolean userCustomSelectStyle;

    public interface OnTabReselectedListener {
        void onTabReselected(int i);
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.vpiTabPageIndicatorStyle);
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (TabPageIndicator.this.mMaxTabWidth > 0 && getMeasuredWidth() > TabPageIndicator.this.mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(TabPageIndicator.this.mMaxTabWidth, 1073741824), heightMeasureSpec);
            }
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTabClickListener = new OnClickListener() {
            public void onClick(View view) {
                TabView tabView = (TabView) view;
                int oldSelected = TabPageIndicator.this.mViewPager.getCurrentItem();
                int newSelected = tabView.getIndex();
                TabPageIndicator.this.mViewPager.setCurrentItem(newSelected);
                if (oldSelected == newSelected && TabPageIndicator.this.mTabReselectedListener != null) {
                    TabPageIndicator.this.mTabReselectedListener.onTabReselected(newSelected);
                }
            }
        };
        this.mMaxTabWidth = -1;
        this.userCustomSelectStyle = false;
        setHorizontalScrollBarEnabled(false);
        this.mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        addView(this.mTabLayout, new LayoutParams(-2, -1));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        this.mTabReselectedListener = listener;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded = widthMode == 1073741824;
        setFillViewport(lockedExpanded);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount <= 1 || !(widthMode == 1073741824 || widthMode == ExploreByTouchHelper.INVALID_ID)) {
            this.mMaxTabWidth = -1;
        } else if (childCount > 2) {
            this.mMaxTabWidth = (int) (((float) MeasureSpec.getSize(widthMeasureSpec)) * 0.4f);
        } else {
            this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
        }
        int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();
        if (lockedExpanded && oldWidth != newWidth) {
            setCurrentItem(this.mSelectedTabIndex);
        }
    }

    private void animateToTab(int position) {
        final View tabView = this.mTabLayout.getChildAt(position);
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
        this.mTabSelector = new Runnable() {
            public void run() {
                TabPageIndicator.this.smoothScrollTo(tabView.getLeft() - ((TabPageIndicator.this.getWidth() - tabView.getWidth()) / 2), 0);
                TabPageIndicator.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            post(this.mTabSelector);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setGravity(17);
        tabView.setTextSize(2, 16.0f);
        tabView.setPadding(dip2px(getContext(), 7.0f), 0, dip2px(getContext(), 7.0f), 0);
        tabView.setTextColor(getResources().getColor(R.color.calendar_indicator_selector));
        tabView.setFocusable(true);
        tabView.setOnClickListener(this.mTabClickListener);
        tabView.setText(text);
        tabView.setSingleLine();
        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }
        this.mTabLayout.addView(tabView, new LinearLayout.LayoutParams(-2, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void onPageScrollStateChanged(int arg0) {
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(arg0);
        }
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (this.mListener != null) {
            this.mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (this.mListener != null) {
            this.mListener.onPageSelected(arg0);
        }
    }

    public void setViewPager(ViewPager view) {
        if (this.mViewPager != view) {
            if (this.mViewPager != null) {
                this.mViewPager.setOnPageChangeListener(null);
            }
            if (view.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.mViewPager = view;
            view.setOnPageChangeListener(this);
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        this.mTabLayout.removeAllViews();
        PagerAdapter adapter = this.mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (this.mSelectedTabIndex > count) {
            this.mSelectedTabIndex = count - 1;
        }
        setCurrentItem(this.mSelectedTabIndex);
        requestLayout();
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setUserCustomSelectStyle(boolean userCustomSelectStyle) {
        this.userCustomSelectStyle = userCustomSelectStyle;
    }

    public void setCurrentItem(int item) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mSelectedTabIndex = item;
        this.mViewPager.setCurrentItem(item);
        int tabCount = this.mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            boolean isSelected;
            TabView child = (TabView) this.mTabLayout.getChildAt(i);
            if (i == item) {
                isSelected = true;
            } else {
                isSelected = false;
            }
            child.setSelected(isSelected);
            if (this.userCustomSelectStyle) {
                if (isSelected) {
                    child.setTextSize(2, 20.0f);
                    child.setPadding(dip2px(getContext(), 10.0f), 0, dip2px(getContext(), 10.0f), 0);
                    child.setTextColor(-26368);
                    child.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.viewpagerindicator_triangle);
                } else {
                    child.setTextSize(2, 16.0f);
                    child.setPadding(dip2px(getContext(), 7.0f), 0, dip2px(getContext(), 7.0f), 0);
                    child.setTextColor(-6710887);
                    child.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
            this.mTabLayout.requestLayout();
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
