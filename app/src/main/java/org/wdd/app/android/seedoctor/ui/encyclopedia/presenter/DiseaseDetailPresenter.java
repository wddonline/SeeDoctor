package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DiseaseDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DiseaseDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DiseaseDetailPresenter implements BasePresenter, DiseaseDetailGetter.DiseaseDetailCallback {

    private DiseaseDetailActivity view;
    private DiseaseDetailGetter getter;
    private HttpSession session;

    public DiseaseDetailPresenter(ActivityFragmentAvaliable host, DiseaseDetailActivity view) {
        this.view = view;
        getter = new DiseaseDetailGetter(host, view.getBaseContext(), this);
    }

    public void getDiseaseDetailData(String diseaseId) {
        session = getter.requestDiseaseDetailData(diseaseId);
    }

    public void getCollectionStatus(String departmentid) {
        getter.getCollectionStatus(departmentid);
    }

    public void collectDisease(String departmentid, String departmentname) {
        getter.collectDisease(departmentid, departmentname);
    }

    public void uncollectDisease(String departmentid) {
        getter.uncollectDisease(departmentid);
    }

    public void destory() {
        if (session == null) return;
        session.cancelRequest();
    }

    @Override
    public void onRequestOk(DiseaseDetail data) {
        session = null;
        view.showDiseaseDetalViews(data);
    }

    @Override
    public void onRequestFailure(String error) {
        session = null;
        view.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorViews();
    }

    @Override
    public void onCollectionStatusGetted(boolean isCollected) {
        view.setDiseaseCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        view.updateDiseaseCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        view.updateDiseaseUncollectedStatus(success);
    }
}
