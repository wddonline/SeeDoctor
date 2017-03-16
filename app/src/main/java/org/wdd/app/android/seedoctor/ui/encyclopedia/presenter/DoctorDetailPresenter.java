package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DoctorDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DoctorDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DoctorDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DoctorDetailPresenter implements BasePresenter, DoctorDetailGetter.DoctorDetailCallback {

    private DoctorDetailActivity view;
    private DoctorDetailGetter getter;

    public DoctorDetailPresenter(ActivityFragmentAvaliable host, DoctorDetailActivity view) {
        this.view = view;
        getter = new DoctorDetailGetter(host, view.getBaseContext(), this);
    }

    public void getDoctorDetailData(String hospitalid) {
        getter.requestDoctorDetailData(hospitalid);
    }

    public void getCollectionStatus(String doctorid) {
        getter.getCollectionStatus(doctorid);
    }

    public void collectDoctor(String doctorid, String doctorname, String photourl) {
        getter.collectDoctor(doctorid, doctorname, photourl);
    }

    public void uncollectDoctor(String departmentid) {
        getter.collectDoctor(departmentid);
    }

    @Override
    public void cancelRequest() {
        getter.cancelRequest();
    }

    @Override
    public void onRequestOk(DoctorDetail data) {
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
        view.setDoctorCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        view.updateDoctorCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        view.updateDoctorUncollectedStatus(success);
    }
}
