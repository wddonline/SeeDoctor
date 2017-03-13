package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
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
    private HttpSession session;

    public DoctorDetailPresenter(ActivityFragmentAvaliable host, DoctorDetailActivity view) {
        this.view = view;
        getter = new DoctorDetailGetter(host, view.getBaseContext(), this);
    }

    public void getDoctorDetailData(String hospitalid) {
        session = getter.requestDoctorDetailData(hospitalid);
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

    public void destory() {
        if (session == null) return;
        session.cancelRequest();
    }

    @Override
    public void onRequestOk(DoctorDetail data) {
        session = null;
        view.showHospitalDetalViews(data);
    }

    @Override
    public void onRequestFailure(String error) {
        session = null;
        view.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
        session = null;
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
