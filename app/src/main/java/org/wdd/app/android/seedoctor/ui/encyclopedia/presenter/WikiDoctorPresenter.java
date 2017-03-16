package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDoctorActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDoctorGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDoctorPresenter implements BasePresenter, WikiDoctorGetter.WikiDoctorDataCallback {

    private WikiDoctorActivity view;
    private WikiDoctorGetter getter;

    public WikiDoctorPresenter(ActivityFragmentAvaliable host, WikiDoctorActivity view) {
        this.view = view;
        getter = new WikiDoctorGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDoctorListData(String provinceid, String hospitallevel, String doclevelid, boolean refresh) {
        getter.requestDoctorList(provinceid, hospitallevel, doclevelid, refresh);
    }

    @Override
    public void onRequestOk(List<Doctor> data, boolean refresh) {
        if (data.size() == 0) {
            view.showNoDoctorListResult(refresh);
            return;
        }
        view.showDoctorListData(data, refresh);
    }

    @Override
    public void onRequestFailure(String error, boolean refresh) {
        view.showRequetErrorView(error, refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        view.showNetworkErrorView(refresh);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
