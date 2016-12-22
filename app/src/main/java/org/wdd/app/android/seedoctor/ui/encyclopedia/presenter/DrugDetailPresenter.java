package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DrugDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DiseaseDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DrugDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DrugDetailPresenter implements BasePresenter, DrugDetailGetter.DrugDetailCallback {

    private DrugDetailActivity view;
    private DrugDetailGetter getter;
    private HttpSession session;

    public DrugDetailPresenter(DrugDetailActivity view) {
        this.view = view;
        getter = new DrugDetailGetter(view.getBaseContext(), this);
    }

    public void getDrugDetailData(int drugId) {
        session = getter.requestDrugDetailData(drugId);
    }

    public void destory() {
        if (session == null) return;
        session.cancelRequest();
    }

    @Override
    public void onRequestOk(DrugDetail data) {
        session = null;
        view.showDrugDetalViews(data);
    }

    @Override
    public void onRequestFailure(HttpError error) {
        session = null;
        view.showRequestErrorViews(error.getErrorMsg());
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorViews();
    }
}
