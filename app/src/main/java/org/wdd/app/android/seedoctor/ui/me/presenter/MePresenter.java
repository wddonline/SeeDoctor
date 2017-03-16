package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.data.AppDiskCacheManeger;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.BmobUtils;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

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
        BmobUtils.mannelUpdateApp(view.getContext(), new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (updateStatus == UpdateStatus.Yes) {//版本有更新

                }else if(updateStatus == UpdateStatus.No){
                    AppToaster.show(R.string.bmob_update_lastest_already);
                }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                    AppToaster.show(R.string.bmob_update_field_error);
                }else if(updateStatus==UpdateStatus.IGNORED){
                    AppToaster.show(R.string.bmob_update_version_ignore);
                }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
                    AppToaster.show(R.string.bmob_update_apk_size_error);
                }else if(updateStatus==UpdateStatus.TimeOut){
                    AppToaster.show(R.string.bmob_update_error);
                }
                view.finishVersionCheck();
            }
        });
    }

    @Override
    public void cancelRequest() {

    }

    public void cleanDiskCache() {
        cacheManeger.cleanDiskCache();
    }

    @Override
    public void onDiskCacheCleaned(String result) {
        view.showDiskCleanResult(result);
    }
}
