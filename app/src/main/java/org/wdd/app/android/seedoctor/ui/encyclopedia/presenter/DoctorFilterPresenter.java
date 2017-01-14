package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DoctorFilterActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalFilterGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;

import java.util.List;

/**
 * Created by richard on 12/20/16.
 */

public class DoctorFilterPresenter implements BasePresenter, HospitalFilterGetter.DataCallback {

    private DoctorFilterActivity view;
    private HospitalFilterGetter getter;
    private HttpSession session;

    public DoctorFilterPresenter(DoctorFilterActivity view) {
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
