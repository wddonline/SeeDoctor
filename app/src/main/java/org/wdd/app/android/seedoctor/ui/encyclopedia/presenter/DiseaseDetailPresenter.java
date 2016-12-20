package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DiseaseDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DiseaseDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;

/**
 * Created by richard on 12/20/16.
 */

public class DiseaseDetailPresenter implements BasePresenter, DiseaseDetailGetter.SearchCallback {

    private DiseaseDetailActivity view;
    private DiseaseDetailGetter getter;
    private HttpSession session;

    public DiseaseDetailPresenter(DiseaseDetailActivity view) {
        this.view = view;
        getter = new DiseaseDetailGetter(view.getBaseContext(), this);
    }

    public void getDiseaseDetailData(int diseaseId) {
        session = getter.requestDiseaseDetailData(diseaseId);
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
