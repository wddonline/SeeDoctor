package org.wdd.app.android.seedoctor.ui.nearby.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.search.activity.NearbySearchActivity;
import org.wdd.app.android.seedoctor.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 3/15/17.
 */

public class NearbyFragment extends BaseFragment {

    private ViewGroup mRootView;
    private Toolbar toolbar;
    private ViewPager mViewPager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_nearby, container, false);
            initTitle();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initTitle() {
        setHasOptionsMenu(true);
        toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_nearby_toolbar);
        MenuItem menuItem = toolbar.getMenu().add(0, 0, 0, getString(R.string.search));
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.mipmap.img_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NearbySearchActivity.show(getContext());
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View spaceView = mRootView.getChildAt(0);
            spaceView.setVisibility(View.VISIBLE);
            spaceView.getLayoutParams().height = AppUtils.getStatusHeight(getActivity());
        }
    }

    private void initViews() {
        mViewPager = (ViewPager) mRootView.findViewById(R.id.fragment_nearby_viewpager);
        mViewPager.setAdapter(new NearbyPagerAdapter(getChildFragmentManager()));

        TabLayout tabs = (TabLayout) mRootView.findViewById(R.id.fragment_nearby_tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    protected void lazyLoad() {

    }

    private class NearbyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { getString(R.string.nearby_hospital), getString(R.string.nearby_drugstore)};

        private List<Fragment> fragments;

        public NearbyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new NearbyHospitalFragment());
            fragments.add(new NearbyDrugstoreFragment());
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
            return TITLES[position];
        }
    }

}
