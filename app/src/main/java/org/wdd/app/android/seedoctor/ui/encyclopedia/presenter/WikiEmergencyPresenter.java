package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiEmergencyActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiEmergencyGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class WikiEmergencyPresenter implements WikiEmergencyGetter.EmergencyCallback {

    private WikiEmergencyActivity view;
    private WikiEmergencyGetter getter;
    private HttpSession session;

    public WikiEmergencyPresenter(ActivityFragmentAvaliable host, WikiEmergencyActivity view) {
        this.view = view;
        getter = new WikiEmergencyGetter(host, view.getBaseContext());
        getter.setEmergencyCallback(this);
    }

    public void getEmergencyListData() {
        session = getter.requestEmergencyData();
    }

    public void destory() {
        if (session != null) {
            session.cancelRequest();
        }
    }

    @Override
    public void onDataGetted(List<Emergency> data) {
        session = null;
        if (data.size() == 0) {
            view.showNoDataViews();
            return;
        }
        view.showEmergencyListViews(data);
    }

    @Override
    public void onFailure(HttpError error) {
        session = null;
        view.showDataGettedFailureViews(error.getErrorMsg());
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorViews();
    }
}
