package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalFilterActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalFilterGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;

import java.util.List;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalFilterPresenter implements BasePresenter, HospitalFilterGetter.DataCallback {

    private HospitalFilterActivity view;
    private HospitalFilterGetter getter;
    private HttpSession session;

    public HospitalFilterPresenter(HospitalFilterActivity view) {
        this.view = view;
        getter = new HospitalFilterGetter(view.getBaseContext(), this);
    }

    public void getProvinceData() {
        session = getter.requestHospitalDetailData();
    }

    public void destory() {
        if (session == null) return;
        session.cancelRequest();
    }

    @Override
    public void onRequestOk(List<Province> data) {
        session = null;
        view.showProvinceViews(data);
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
