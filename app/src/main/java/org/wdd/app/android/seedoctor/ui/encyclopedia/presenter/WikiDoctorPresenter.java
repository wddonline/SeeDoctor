package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
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
    private HttpSession session;

    public WikiDoctorPresenter(WikiDoctorActivity view) {
        this.view = view;
        getter = new WikiDoctorGetter(view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDoctorListData(String provinceid, String hospitallevel, boolean refresh) {
        session = getter.requestDoctorList(provinceid, hospitallevel, refresh);
    }

    @Override
    public void onRequestOk(List<Doctor> data, boolean refresh) {
        session = null;
        if (data.size() == 0) {
            view.showNoDrugListResult(refresh);
            return;
        }
        view.showDrugListData(data, refresh);
    }

    @Override
    public void onRequestFailure(HttpError error, boolean refresh) {
        session = null;
        view.showRequetErrorView(error.getErrorMsg(), refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        session = null;
        view.showNetworkErrorView(refresh);
    }

    public void destory() {
        if (session != null) session.cancelRequest();
    }
}
