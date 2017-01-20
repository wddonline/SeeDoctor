package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DoctorDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DoctorDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DoctorDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DoctorDetailPresenter implements BasePresenter, DoctorDetailGetter.DoctorDetailCallback {

    private DoctorDetailActivity view;
    private DoctorDetailGetter getter;
    private HttpSession session;

    public DoctorDetailPresenter(ActivityFragmentAvaliable holder, DoctorDetailActivity view) {
        this.view = view;
        getter = new DoctorDetailGetter(holder, view.getBaseContext(), this);
    }

    public void getDoctorDetailData(String hospitalid) {
        session = getter.requestDoctorDetailData(hospitalid);
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
    public void onRequestFailure(HttpError error) {
        session = null;
        view.showRequestErrorViews(error.getErrorMsg());
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorViews();
    }
}
