package org.wdd.app.android.seedoctor.ui.news.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.utils.AppUtils;

/**
 * Created by richard on 3/15/17.
 */

public class NewsFragment extends BaseFragment {

    private ViewGroup mRootView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);
            initData();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initData() {

    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View spaceView = mRootView.getChildAt(0);
            spaceView.setVisibility(View.VISIBLE);
            spaceView.getLayoutParams().height = AppUtils.getStatusHeight(getActivity());
        }
    }

    @Override
    protected void lazyLoad() {

    }
}
