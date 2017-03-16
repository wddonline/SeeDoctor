package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalFilterActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalFilterGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;

import java.util.List;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalFilterPresenter implements BasePresenter, HospitalFilterGetter.DataCallback {

    private HospitalFilterActivity view;
    private HospitalFilterGetter getter;

    public HospitalFilterPresenter(ActivityFragmentAvaliable host, HospitalFilterActivity view) {
        this.view = view;
        getter = new HospitalFilterGetter(host, view.getBaseContext(), this);
    }

    public void getProvinceData() {
        getter.requestHospitalDetailData();
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }

    @Override
    public void onRequestOk(List<Province> data) {
        view.showProvinceViews(data);
    }

    @Override
    public void onRequestFailure(String error) {
        view.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
        view.showNetworkErrorViews();
    }
}
