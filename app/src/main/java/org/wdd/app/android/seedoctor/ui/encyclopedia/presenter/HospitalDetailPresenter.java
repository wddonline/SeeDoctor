package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DrugDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DrugDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalDetailPresenter implements BasePresenter, HospitalDetailGetter.HospitalDetailCallback {

    private HospitalDetailActivity view;
    private HospitalDetailGetter getter;
    private HttpSession session;

    public HospitalDetailPresenter(HospitalDetailActivity view) {
        this.view = view;
        getter = new HospitalDetailGetter(view.getBaseContext(), this);
    }

    public void getHospitalDetailData(String hospitalid) {
        session = getter.requestHospitalDetailData(hospitalid);
    }

    public void destory() {
        if (session == null) return;
        session.cancelRequest();
    }

    @Override
    public void onRequestOk(HospitalDetail data) {
        session = null;
        view.showHospitalDetalViews(data);
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