package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.RelativeDiseaseListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.RelativeDiseaseListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDrugListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class RelativeDiseaseListPresenter implements BasePresenter, RelativeDiseaseListGetter.RelativeDiseaseDataCallback {

    private RelativeDiseaseListActivity view;
    private RelativeDiseaseListGetter getter;
    private HttpSession session;

    public RelativeDiseaseListPresenter(RelativeDiseaseListActivity view) {
        this.view = view;
        getter = new RelativeDiseaseListGetter(view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDiseaseListData(String drugid, boolean refresh) {
        session = getter.requestDiseaseList(drugid, refresh);
    }

    @Override
    public void onRequestOk(List<Disease> data, boolean refresh) {
        session = null;
        if (data.size() == 0) {
            view.showNoDrugListResult(refresh);
            return;
        }
        view.showDiseaseListData(data, refresh);
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
