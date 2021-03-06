package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalDetailPresenter implements BasePresenter, HospitalDetailGetter.HospitalDetailCallback {

    private HospitalDetailActivity view;
    private HospitalDetailGetter getter;

    public HospitalDetailPresenter(ActivityFragmentAvaliable host, HospitalDetailActivity view) {
        this.view = view;
        getter = new HospitalDetailGetter(host, view.getBaseContext(), this);
    }

    public void getHospitalDetailData(String hospitalid) {
        getter.requestHospitalDetailData(hospitalid);
    }

    public void getCollectionStatus(String hospitalid) {
        getter.getCollectionStatus(hospitalid);
    }

    public void collectHospital(String hospitalid, String hospitalname, String picurl) {
        getter.collectHospital(hospitalid, hospitalname, picurl);
    }

    public void uncollectHospital(String hospitalid) {
        getter.collectHospital(hospitalid);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }

    @Override
    public void onRequestOk(HospitalDetail data) {
        view.showHospitalDetalViews(data);
    }

    @Override
    public void onRequestFailure(String error) {
        view.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
        view.showNetworkErrorViews();
    }

    @Override
    public void onCollectionStatusGetted(boolean isCollected) {
        view.setHospitalCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        view.updateHospitalCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        view.updateHospitalUncollectedStatus(success);
    }

}
