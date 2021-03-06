package org.wdd.app.android.seedoctor.ui.me.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.me.activity.AboutActivity;
import org.wdd.app.android.seedoctor.ui.me.activity.AppWallActivity;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesActivity;
import org.wdd.app.android.seedoctor.ui.me.activity.NavigationSettingsActivity;
import org.wdd.app.android.seedoctor.ui.me.activity.ProfileEditActivity;
import org.wdd.app.android.seedoctor.ui.me.presenter.MePresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private final int PROFILE_REQUEST_CODE = 1;

    private ViewGroup mRootView;
    private ImageView headerView;
    private TextView nameView;

    private MePresenter presenter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_me, container, false);
            initData();
            initTitle();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    protected void lazyLoad() {
    }

    private void initData() {
        presenter = new MePresenter(host, this);
    }

    private void initTitle() {
        final Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_me_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        final TextView titleView = (TextView) mRootView.findViewById(R.id.fragment_me_title);
        titleView.post(new Runnable() {
            @Override
            public void run() {
                titleView.setPadding(0, 0, Math.round((toolbar.getWidth() - titleView.getWidth()) * 0.5f), 0);
            }
        });

        AppBarLayout barLayout = (AppBarLayout) mRootView.findViewById(R.id.fragment_me_bar_layout);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = AppUtils.getStatusHeight(getActivity());
            toolbar.getLayoutParams().height = toolbar.getLayoutParams().height + statusHeight;
            toolbar.setPadding(toolbar.getPaddingLeft(), statusHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
        }
    }

    private void initViews() {
        headerView = (ImageView) mRootView.findViewById(R.id.fragment_me_headimg);
        nameView = (TextView) mRootView.findViewById(R.id.fragment_me_name);

        mRootView.findViewById(R.id.fragment_me_profile).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_collection_click).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_nav_click).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_version_check_click).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_clear_cache_click).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_appwall_click).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_me_about_click).setOnClickListener(this);

        setHeaderAndName();
    }

    private void setHeaderAndName() {
        AppConfManager confManager = AppConfManager.getInstance(getContext());
        String nickname = confManager.getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            nameView.setText(nickname);
        } else {
            nameView.setText(R.string.waiting_for_a_name);
        }
        String sex = confManager.getSex();
        if (sex.equals("0")) {
            headerView.setImageResource(R.drawable.ic_male_header);
        } else {
            headerView.setImageResource(R.drawable.ic_female_header);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_me_profile:
                ProfileEditActivity.showForResult(getActivity(), ((ViewGroup)v).getChildAt(0), PROFILE_REQUEST_CODE);
                break;
            case R.id.fragment_me_collection_click:
                FavoritesActivity.show(getActivity());
                break;
            case R.id.fragment_me_nav_click:
                NavigationSettingsActivity.show(getActivity());
                break;
            case R.id.fragment_me_version_check_click:
                showLoadingDialog();
                presenter.checkLastestVersion();
                break;
            case R.id.fragment_me_clear_cache_click:
                showLoadingDialog();
                presenter.cleanDiskCache();
                break;
            case R.id.fragment_me_appwall_click:
                AppWallActivity.show(getActivity());
                break;
            case R.id.fragment_me_about_click:
                AboutActivity.show(getActivity());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case PROFILE_REQUEST_CODE:
                setHeaderAndName();
                break;
        }
    }

    public void finishVersionCheck() {
        hideLoadingDialog();
    }

    public void showDiskCleanResult(String result) {
        hideLoadingDialog();
        String hint = getString(R.string.disk_cache_clean_result);
        hint = String.format(hint, result);
        AppToaster.show(hint);
    }
}
