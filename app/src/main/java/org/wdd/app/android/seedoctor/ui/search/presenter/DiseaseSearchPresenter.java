package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.search.activity.DiseaseSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.DiseaseSearchGetter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class DiseaseSearchPresenter implements BasePresenter, DiseaseSearchGetter.SearchCallback {

    private DiseaseSearchActivity view;
    private DiseaseSearchGetter getter;

    public DiseaseSearchPresenter(ActivityFragmentAvaliable host, DiseaseSearchActivity view) {
        this.view = view;
        getter = new DiseaseSearchGetter(host, view, this);
    }

    public void searchDiseaseByName(String keyword, boolean refresh) {
        getter.getDiseaseListByName(keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Disease> data, boolean refresh) {
        view.showDiseaseDataView(data, refresh);
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
