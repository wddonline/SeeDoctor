package org.wdd.app.android.seedoctor.ui.me.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.me.activity.AboutActivity;
import org.wdd.app.android.seedoctor.ui.me.presenter.MePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private View rootView;
    private TextView cacheDataView;

    private MePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_me, container, false);
            initData();
            initTitle();
            initViews();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void lazyLoad() {
        presenter.getDiskCacheAsync();
    }

    private void initData() {
        presenter = new MePresenter(this);
    }

    private void initTitle() {
        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fragment_me_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        final TextView titleView = (TextView) rootView.findViewById(R.id.fragment_me_title);
        titleView.post(new Runnable() {
            @Override
            public void run() {
                titleView.setPadding(0, 0, Math.round((toolbar.getWidth() - titleView.getWidth()) * 0.5f), 0);
            }
        });

        AppBarLayout barLayout = (AppBarLayout) rootView.findViewById(R.id.fragment_me_bar_layout);
        barLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (-1 * verticalOffset == appBarLayout.getTotalScrollRange()) {
                    titleView.setVisibility(View.VISIBLE);
                } else {
                    titleView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initViews() {
        cacheDataView = (TextView) rootView.findViewById(R.id.fragment_me_clear_cache_data);
        rootView.findViewById(R.id.fragment_me_version_check_click).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_me_clear_cache_click).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_me_about_click).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_me_version_check_click:
                showLoadingDialog(R.string.checking_version);
                presenter.checkLastestVersion();
                break;
            case R.id.fragment_me_clear_cache_click:
                showLoadingDialog(R.string.deleting_cache);
                presenter.cleanDiskCache();
                break;
            case R.id.fragment_me_about_click:
                AboutActivity.show(getContext());
                break;
        }
    }

    public void finishVersionCheck() {
        hideLoadingDialog();
    }
}
