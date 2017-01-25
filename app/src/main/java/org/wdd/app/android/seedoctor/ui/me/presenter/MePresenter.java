package org.wdd.app.android.seedoctor.ui.me.presenter;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.data.AppDiskCacheManeger;
import org.wdd.app.android.seedoctor.ui.me.data.MeDataGetter;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;
import org.wdd.app.android.seedoctor.utils.AppToaster;

/**
 * Created by richard on 1/20/17.
 */

public class MePresenter implements BasePresenter, MeDataGetter.DataCallbck, AppDiskCacheManeger.AppDiskCacheCallback {

    private MeFragment view;
    private MeDataGetter getter;
    private AppDiskCacheManeger cacheManeger;

    public MePresenter(ActivityFragmentAvaliable host, MeFragment view) {
        this.view = view;
        getter = new MeDataGetter(host, view.getActivity(), this);
        cacheManeger = new AppDiskCacheManeger(host, view.getContext(), this);
    }

    public void checkLastestVersion() {
        getter.checkVersion();
    }

    @Override
    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
        switch (updateStatus) {
            case UpdateStatus.Yes: // has update
                UmengUpdateAgent.showUpdateDialog(view.getContext(), updateInfo);
                break;
            case UpdateStatus.No: // has no update
                AppToaster.show(R.string.lastest_version_already);
                break;
            case UpdateStatus.NoneWifi: // none wifi
                AppToaster.show(R.string.wifi_update_only);
                break;
            case UpdateStatus.Timeout: // time out
                AppToaster.show(R.string.query_version_timeout);
                break;
        }
        view.finishVersionCheck();
    }

    public void cleanDiskCache() {
        cacheManeger.cleanDiskCache();
    }

    @Override
    public void onDiskCacheCleaned(String result) {
        view.showDiskCleanResult(result);
    }
}
