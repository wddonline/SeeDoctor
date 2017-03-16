package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DrugDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DrugDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DrugDetailPresenter implements BasePresenter, DrugDetailGetter.DrugDetailCallback {

    private DrugDetailActivity view;
    private DrugDetailGetter getter;

    public DrugDetailPresenter(ActivityFragmentAvaliable host, DrugDetailActivity view) {
        this.view = view;
        getter = new DrugDetailGetter(host, view.getBaseContext(), this);
    }

    public void getDrugDetailData(String drugId) {
        getter.requestDrugDetailData(drugId);
    }

    public void getCollectionStatus(String drugid) {
        getter.getCollectionStatus(drugid);
    }

    public void collectDrug(String drugid, String drugname) {
        getter.collectDrug(drugid, drugname);
    }

    public void uncollectDrug(String drugid) {
        getter.uncollectDrug(drugid);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }

    @Override
    public void onRequestOk(DrugDetail data) {
        view.showDrugDetalViews(data);
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
        view.setDrugCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        view.updateDrugCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        view.updateDrugUncollectedStatus(success);
    }

}
