package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDiseaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDiseaseGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDiseasePresenter implements BasePresenter, WikiDiseaseGetter.WikiDiseaseDataCallback {

    private WikiDiseaseActivity view;
    private WikiDiseaseGetter getter;
    private HttpSession session;

    public WikiDiseasePresenter(ActivityFragmentAvaliable host, WikiDiseaseActivity view) {
        this.view = view;
        getter = new WikiDiseaseGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDiseaseListData(boolean refresh) {
        session = getter.requestDiseaseList(refresh);
    }

    @Override
    public void onRequestOk(List<Disease> data, boolean refresh) {
        session = null;
        if (data.size() == 0) {
            view.showNoDiseaseListResult(refresh);
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
