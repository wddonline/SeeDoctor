package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.ui.search.activity.DrugSearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.DrugSearchGetter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class DrugSearchPresenter implements BasePresenter, DrugSearchGetter.SearchCallback {

    private DrugSearchActivity view;
    private DrugSearchGetter getter;

    private HttpSession session = null;

    public DrugSearchPresenter(ActivityFragmentAvaliable host, DrugSearchActivity view) {
        this.view = view;
        getter = new DrugSearchGetter(host, view, this);
    }

    public void searchDrugByName(String keyword, boolean refresh) {
        if (session != null) session.cancelRequest();
        session = getter.getDrugListByName(keyword, refresh);
    }

    @Override
    public void onRequestOk(List<Drug> data, boolean refresh) {
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
