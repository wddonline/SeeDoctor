package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

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

    public DiseaseDetailPresenter(ActivityFragmentAvaliable host, DiseaseDetailActivity view) {
        this.view = view;
        getter = new DiseaseDetailGetter(host, view.getBaseContext(), this);
    }

    public void getDiseaseDetailData(String diseaseId) {
        getter.requestDiseaseDetailData(diseaseId);
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

    @Override
    public void cancelRequest() {
        getter.cancelRequest();
    }

    @Override
    public void onRequestOk(DiseaseDetail data) {
        view.showDiseaseDetalViews(data);
    }

    @Override
    public void onRequestFailure(String error) {
        view.showRequestErrorViews(error);
    }

    @Override
    public void onNetworkError() {
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
