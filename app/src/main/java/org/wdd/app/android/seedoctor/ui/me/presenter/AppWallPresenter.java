package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.AppWallActivity;
import org.wdd.app.android.seedoctor.ui.me.data.AppWallDataGetter;
import org.wdd.app.android.seedoctor.ui.me.model.AppModel;

import java.util.List;

/**
 * Created by richard on 4/14/17.
 */

public class AppWallPresenter implements BasePresenter, AppWallDataGetter.DataCallback {

    private AppWallActivity mView;
    private AppWallDataGetter mGetter;

    public AppWallPresenter(AppWallActivity view) {
        this.mView = view;
        mGetter = new AppWallDataGetter(view, this);
    }

    public void getAppList(ActivityFragmentAvaliable host) {
        mGetter.requestAppListData(host);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelRequest();
    }

    @Override
    public void onRequestOk(List<AppModel> apps) {
        if (apps == null) {
            mView.showNoDataViews();
            return;
        }
        mView.showAppListViews(apps);
    }

    @Override
    public void onRequestError(String error) {
        mView.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
        mView.showNetworkErrorViews();
    }
}
