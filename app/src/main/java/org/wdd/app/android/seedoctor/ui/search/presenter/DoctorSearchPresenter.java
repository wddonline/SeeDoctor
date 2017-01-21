package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.search.activity.DoctorSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.DoctorSearchGetter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class DoctorSearchPresenter implements BasePresenter, DoctorSearchGetter.SearchCallback {

    private DoctorSearchActivity view;
    private DoctorSearchGetter getter;

    private HttpSession session = null;

    public DoctorSearchPresenter(ActivityFragmentAvaliable host, DoctorSearchActivity view) {
        this.view = view;
        getter = new DoctorSearchGetter(host, view, this);
    }

    public void searchDoctorByName(String provinceid, String hospitallevel, String keyword, boolean refresh) {
        if (session != null) session.cancelRequest();
        session = getter.getDoctorList(provinceid, hospitallevel, keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Doctor> data, boolean refresh) {
        session = null;
        view.showDoctorDataView(data, refresh);
    }

    @Override
    public void onRequestFailure(HttpError error, boolean refresh) {
        session = null;
        view.handleRequestErrorViews(LoadView.LoadStatus.Request_Failure, refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        session = null;
        view.handleRequestErrorViews(LoadView.LoadStatus.Network_Error, refresh);
    }

    public void destory() {
        if (session != null) session.cancelRequest();
    }
}
