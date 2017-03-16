package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDoctorGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDoctorFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalDoctorPresenter implements BasePresenter, WikiDoctorGetter.WikiDoctorDataCallback {

    private HospitalDoctorFragment view;
    private WikiDoctorGetter getter;

    public HospitalDoctorPresenter(ActivityFragmentAvaliable host, HospitalDoctorFragment view) {
        this.view = view;
        getter = new WikiDoctorGetter(host, view.getContext());
        getter.setCallback(this);
    }

    public void getDoctorListData(String hospitalid) {
        getter.requestDoctorList(hospitalid);
    }

    @Override
    public void onRequestOk(List<Doctor> data, boolean refresh) {
        if (data.size() == 0) {
            view.showNoDoctorListResult();
            return;
        }
        view.showDoctorListData(data);
    }

    @Override
    public void onRequestFailure(String error, boolean refresh) {
        view.showRequetErrorView(error);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        view.showNetworkErrorView();
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
