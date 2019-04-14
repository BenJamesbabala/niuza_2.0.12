package com.niuza.android.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.niuza.android.AppMode;
import com.niuza.android.R;
import com.niuza.android.ui.search.SearchActivity;
import org.szuwest.lib.BaseActivity;

public class MainActivity extends BaseActivity {
    public static final int TAB_CATEGORY = 1;
    public static final int TAB_RANK = 2;
    public static final int TAB_RECOMMAND = 0;
    private MainViewPageAdapter mPageAdapter;
    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        public void onPageScrolled(int i, float v, int i1) {
        }

        public void onPageSelected(int i) {
            if (i == 1) {
                MainActivity.this.getActionBar().show();
            } else if (i == 0) {
                if (MainActivity.this.mPageAdapter.mRecommendFragment != null) {
                    MainActivity.this.mPageAdapter.mRecommendFragment.setObservableScrollEnable(true);
                }
                if (MainActivity.this.mPageAdapter.mRankingFragment != null) {
                    MainActivity.this.mPageAdapter.mRankingFragment.setObservableScrollEnable(false);
                }
            } else {
                if (MainActivity.this.mPageAdapter.mRecommendFragment != null) {
                    MainActivity.this.mPageAdapter.mRecommendFragment.setObservableScrollEnable(false);
                }
                if (MainActivity.this.mPageAdapter.mRankingFragment != null) {
                    MainActivity.this.mPageAdapter.mRankingFragment.setObservableScrollEnable(true);
                }
            }
        }

        public void onPageScrollStateChanged(int i) {
        }
    };
    private ViewPager mPager;
    private PagerSlidingTabStrip mTab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setLogo(AppMode.getAboutLogoResId());
            actionBar.setTitle("");
        }
        initUI();
    }

    private void initUI() {
        this.mTab = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        this.mPager = (ViewPager) findViewById(R.id.viewpager);
        this.mPager.setOffscreenPageLimit(2);
        this.mPageAdapter = new MainViewPageAdapter(getSupportFragmentManager());
        this.mPager.setOnPageChangeListener(this.mPageChangeListener);
        this.mPager.setAdapter(this.mPageAdapter);
        this.mTab.setViewPager(this.mPager);
        this.mPager.setCurrentItem(0);
        this.mPager.postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.mPager.setCurrentItem(0);
                if (MainActivity.this.mPageAdapter.mRecommendFragment != null) {
                    MainActivity.this.mPageAdapter.mRecommendFragment.setObservableScrollEnable(true);
                }
            }
        }, 50);
        this.mTab.setOnPageChangeListener(this.mPageChangeListener);
//        this.mTab.setUserCustomSelectStyle(true);
        this.mTab.setIndicatorColor(getResources().getColor(R.color.orange));
        this.mTab.setUnderlineColor(getResources().getColor(R.color.orange));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        }
        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
