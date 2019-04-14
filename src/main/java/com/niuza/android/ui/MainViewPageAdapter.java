package com.niuza.android.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.niuza.android.ui.category.CategoryFragment;
import com.niuza.android.ui.rank.RankingFragment;
import com.niuza.android.ui.recommend.RecommendFragment;

public class MainViewPageAdapter extends FragmentPagerAdapter {
    CategoryFragment mCategoryFragment;
    RankingFragment mRankingFragment;
    RecommendFragment mRecommendFragment;

    public MainViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int i) {
        if (i == 1) {
            this.mCategoryFragment = new CategoryFragment();
            return this.mCategoryFragment;
        } else if (i == 0) {
            this.mRecommendFragment = new RecommendFragment();
            return this.mRecommendFragment;
        } else {
            this.mRankingFragment = new RankingFragment();
            return this.mRankingFragment;
        }
    }

    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "最新推荐";
            case 1:
                return "商品分类";
            case 2:
                return "排行榜";
            default:
                return super.getPageTitle(position);
        }
    }

    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
