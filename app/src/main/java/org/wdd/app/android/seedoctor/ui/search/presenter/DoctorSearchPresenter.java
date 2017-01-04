package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDoctorGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.search.activity.DiseaseSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.activity.DoctorSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.DiseaseSearchGetter;
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

    public DoctorSearchPresenter(DoctorSearchActivity view) {
        this.view = view;
        getter = new DoctorSearchGetter(view, this);
    }

    public void searchDiseaseByName(String provinceid, String hospitallevel, String keyword, boolean refresh) {
        if (session != null) session.cancelRequest();
        session = getter.getDoctorList(provinceid, hospitallevel, keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Doctor> data, boolean refresh) {
        session = null;
        view.showDiseaseDataView(data, refresh);
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
}
