package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.EmergencyDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.EmergencyDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;


/**
 * Created by richard on 1/3/17.
 */

public class EmergencyDetailPresenter implements EmergencyDetailGetter.EmergencyCallback {

    private EmergencyDetailActivity view;
    private EmergencyDetailGetter getter;
    private HttpSession session;

    public EmergencyDetailPresenter(ActivityFragmentAvaliable host, EmergencyDetailActivity view) {
        this.view = view;
        getter = new EmergencyDetailGetter(host, view.getBaseContext());
        getter.setEmergencyCallback(this);
    }

    public void getEmergencyDetailDataData(String emeid) {
        session = getter.requestEmergencyData(emeid);
    }

    public void getCollectionStatus(String emeid) {
        getter.getCollectionStatus(emeid);
    }

    public void collectEmergency(String emeid, String eme) {
        getter.collectEmergency(emeid, eme);
    }

    public void uncollectEmergency(String emeid) {
        getter.collectEmergency(emeid);
    }

    public void destory() {
        if (session != null) {
            session.cancelRequest();
        }
    }

    @Override
    public void onDataGetted(Emergency data) {
        session = null;
        view.showEmergencyDetailViews(data);
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

    @Override
    public void onCollectionStatusGetted(boolean isCollected) {
        view.setEmergencyCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        view.updateEmergencyCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        view.updateEmergencyUncollectedStatus(success);
    }

}
