package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDrugListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDrugListPresenter implements BasePresenter, WikiDrugListGetter.WikiDrugDataCallback {

    private WikiDrugListActivity view;
    private WikiDrugListGetter getter;

    public WikiDrugListPresenter(ActivityFragmentAvaliable host, WikiDrugListActivity view) {
        this.view = view;
        getter = new WikiDrugListGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDrugListData(int catid, boolean refresh) {
        getter.requestDrugList(catid, refresh);
    }

    @Override
    public void onRequestOk(List<Drug> data, boolean refresh) {
        if (data.size() == 0) {
            view.showNoDrugListResult(refresh);
            return;
        }
        view.showDrugListData(data, refresh);
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
