package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.data.AppDiskCacheManeger;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;
import org.wdd.app.android.seedoctor.utils.BmobUtils;

/**
 * Created by richard on 1/20/17.
 */

public class MePresenter implements BasePresenter, AppDiskCacheManeger.AppDiskCacheCallback {

    private MeFragment view;
    private AppDiskCacheManeger cacheManeger;

    public MePresenter(ActivityFragmentAvaliable host, MeFragment view) {
        this.view = view;
        cacheManeger = new AppDiskCacheManeger(host, view.getContext(), this);
    }

    public void checkLastestVersion() {
        BmobUtils.mannelUpdateApp(view.getContext());
    }


    public void cleanDiskCache() {
        cacheManeger.cleanDiskCache();
    }

    @Override
    public void onDiskCacheCleaned(String result) {
        view.showDiskCleanResult(result);
    }
}
