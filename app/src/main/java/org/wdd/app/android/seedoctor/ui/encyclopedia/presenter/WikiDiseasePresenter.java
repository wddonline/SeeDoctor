package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

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

    public WikiDiseasePresenter(ActivityFragmentAvaliable host, WikiDiseaseActivity view) {
        this.view = view;
        getter = new WikiDiseaseGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDiseaseListData(boolean refresh) {
        getter.requestDiseaseList(refresh);
    }

    @Override
    public void onRequestOk(List<Disease> data, boolean refresh) {
        if (data.size() == 0) {
            view.showNoDiseaseListResult(refresh);
            return;
        }
        view.showDiseaseListData(data, refresh);
    }

    @Override
    public void onRequestFailure(String error, boolean refresh) {
        view.showRequetErrorView(error, refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        view.showNetworkErrorView(refresh);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
