package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;
import org.wdd.app.android.seedoctor.ui.search.activity.HospitalSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.HospitalSearchGetter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class HospitalSearchPresenter implements BasePresenter, HospitalSearchGetter.SearchCallback {

    private HospitalSearchActivity view;
    private HospitalSearchGetter getter;

    public HospitalSearchPresenter(ActivityFragmentAvaliable host, HospitalSearchActivity view) {
        this.view = view;
        getter = new HospitalSearchGetter(host, view, this);
    }

    public void searchHospitalByName(String provinceid, String hospitallevel, String keyword, boolean refresh) {
        getter.getHospitalList(provinceid, hospitallevel, keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Hospital> data, boolean refresh) {
        view.showHospitalDataView(data, refresh);
    }

    @Override
    public void onRequestFailure(String error, boolean refresh) {
        view.handleRequestErrorViews(LoadView.LoadStatus.Request_Failure, refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        view.handleRequestErrorViews(LoadView.LoadStatus.Network_Error, refresh);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
