package org.wdd.app.android.seedoctor.ui.hospital.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.hospital.activity.HospitalSearchActivity;
import org.wdd.app.android.seedoctor.views.SDViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyHospitalFragment extends BaseFragment implements SDViewPager.OnPageChangeListener {

    private enum Mode {
        List, Map
    }

    private View rootView;
    private SDViewPager viewPager;
    private Toolbar toolbar;

    private Mode mode = Mode.List;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_neary_hospital, container, false);
            initViews();
        }
        return rootView;
    }

    private void initViews() {
        initTitle();
        viewPager = (SDViewPager) rootView.findViewById(R.id.fragment_nearby_hospital_viewpager);
        viewPager.setScrollable(false);
        HospitalAdapter adapter = new HospitalAdapter();
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);
    }

    private void initTitle() {
        setHasOptionsMenu(true);
        toolbar = (Toolbar) rootView.findViewById(R.id.fragment_nearby_hospital_toolbar);
        toolbar.setNavigationIcon(R.mipmap.img_search);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HospitalSearchActivity.show(getContext());
            }
        });
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_map:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_item_list:
                        viewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby_hospital_list, menu);
        switch (mode) {
            case Map:
                menu.findItem(R.id.menu_item_list).setVisible(true);
                menu.findItem(R.id.menu_item_map).setVisible(false);
                break;
            case List:
                menu.findItem(R.id.menu_item_list).setVisible(false);
                menu.findItem(R.id.menu_item_map).setVisible(true);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mode = Mode.List;
                toolbar.getMenu().getItem(0).setVisible(true);
                toolbar.getMenu().getItem(1).setVisible(false);
                break;
            case 1:
                mode = Mode.Map;
                toolbar.getMenu().getItem(0).setVisible(false);
                toolbar.getMenu().getItem(1).setVisible(true);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class HospitalAdapter extends FragmentPagerAdapter {

        public HospitalAdapter() {
            super(getFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new NearbyHospitalListFragment();
                    break;
                case 1:
                    fragment = new NearbyHospitalMapFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
