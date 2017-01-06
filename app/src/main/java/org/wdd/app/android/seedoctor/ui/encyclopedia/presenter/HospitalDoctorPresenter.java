package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDoctorGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDoctorFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalDoctorPresenter implements BasePresenter, HospitalDoctorGetter.HospitalDoctorDataCallback {

    private HospitalDoctorFragment view;
    private HospitalDoctorGetter getter;
    private HttpSession session;

    public HospitalDoctorPresenter(HospitalDoctorFragment view) {
        this.view = view;
        getter = new HospitalDoctorGetter(view.getContext());
        getter.setCallback(this);
    }

    public void getDoctorListData(String hospitalid) {
        session = getter.requestDoctorList(hospitalid);
    }

    @Override
    public void onRequestOk(List<Doctor> data) {
        session = null;
        if (data.size() == 0) {
            view.showNoDoctorListResult();
            return;
        }
        view.showDoctorListData(data);
    }

    @Override
    public void onRequestFailure(HttpError error) {
        session = null;
        view.showRequetErrorView(error.getErrorMsg());
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorView();
    }

    public void destory() {
        if (session != null) session.cancelRequest();
    }
}
