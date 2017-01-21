package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.RelativeDrugListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.RelativeDrugListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class RelativeDrugListPresenter implements BasePresenter, RelativeDrugListGetter.RelativeDrugDataCallback {

    private RelativeDrugListActivity view;
    private RelativeDrugListGetter getter;
    private HttpSession session;

    public RelativeDrugListPresenter(ActivityFragmentAvaliable host, RelativeDrugListActivity view) {
        this.view = view;
        getter = new RelativeDrugListGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDrugListData(String diseaseid, boolean refresh) {
        session = getter.requestDrugList(diseaseid, refresh);
    }

    @Override
    public void onRequestOk(List<Drug> data, boolean refresh) {
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
