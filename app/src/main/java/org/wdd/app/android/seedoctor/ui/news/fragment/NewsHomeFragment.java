package org.wdd.app.android.seedoctor.ui.news.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 3/15/17.
 */

public class NewsHomeFragment extends BaseFragment {

    private ViewGroup mRootView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_news_home, container, false);
            initData();
            initTitles();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initTitles() {

    }

    private void initData() {

    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View spaceView = mRootView.getChildAt(0);
            spaceView.setVisibility(View.VISIBLE);
            spaceView.getLayoutParams().height = AppUtils.getStatusHeight(getActivity());
        }

        ViewPager viewPager = (ViewPager) mRootView.findViewById(R.id.fragment_news_home_viewpager);
        viewPager.setAdapter(new NewsAdapter(getChildFragmentManager()));

        TabLayout tabStrip = (TabLayout) mRootView.findViewById(R.id.fragment_news_home_tabs);
        tabStrip.setupWithViewPager(viewPager);
    }

    @Override
    protected void lazyLoad() {

    }

    private class NewsAdapter extends FragmentStatePagerAdapter {

        private String[] titles;
        private List<Fragment> fragments;

        public NewsAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.news_channel);
            fragments = new ArrayList<>();
            fragments.add(NewsFragment.newInstance(null));
            fragments.add(NewsFragment.newInstance("6"));
            fragments.add(NewsFragment.newInstance("3"));
            fragments.add(NewsFragment.newInstance("12"));
            fragments.add(NewsFragment.newInstance("9"));
            fragments.add(NewsFragment.newInstance("15"));
            fragments.add(NewsFragment.newInstance("18"));
            fragments.add(NewsFragment.newInstance("27"));
            fragments.add(NewsFragment.newInstance("21"));
            fragments.add(NewsFragment.newInstance("24"));
            fragments.add(NewsFragment.newInstance("30"));
            fragments.add(NewsFragment.newInstance("33"));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
