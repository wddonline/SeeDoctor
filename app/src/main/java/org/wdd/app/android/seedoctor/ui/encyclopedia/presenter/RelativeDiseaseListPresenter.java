package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.RelativeDiseaseListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.RelativeDiseaseListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class RelativeDiseaseListPresenter implements BasePresenter, RelativeDiseaseListGetter.RelativeDiseaseDataCallback {

    private RelativeDiseaseListActivity view;
    private RelativeDiseaseListGetter getter;
    private HttpSession session;

    public RelativeDiseaseListPresenter(ActivityFragmentAvaliable host, RelativeDiseaseListActivity view) {
        this.view = view;
        getter = new RelativeDiseaseListGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDiseaseListData(String drugid, String departmentid, boolean refresh) {
        session = getter.requestDiseaseList(drugid, departmentid, refresh);
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
    public void onRequestFailure(String error, boolean refresh) {
        session = null;
        view.showRequetErrorView(error, refresh);
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
