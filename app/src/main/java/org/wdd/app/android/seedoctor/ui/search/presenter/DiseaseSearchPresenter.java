package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
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

    private HttpSession session = null;

    public DiseaseSearchPresenter(DiseaseSearchActivity view) {
        this.view = view;
        getter = new DiseaseSearchGetter(view, this);
    }

    public void searchDiseaseByName(String keyword, boolean refresh) {
        if (session != null) session.cancelRequest();
        session = getter.getDiseaseListByName(keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Disease> data, boolean refresh) {
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

    public void destory() {
        if (session != null) session.cancelRequest();
    }
}
