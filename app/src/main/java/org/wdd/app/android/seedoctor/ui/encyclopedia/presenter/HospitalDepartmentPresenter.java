package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDepartmentGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDepartmentFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalDepartmentPresenter implements BasePresenter, HospitalDepartmentGetter.DepartmentDataCallback {

    private HospitalDepartmentFragment view;
    private HospitalDepartmentGetter getter;
    private HttpSession session;

    public HospitalDepartmentPresenter(ActivityFragmentAvaliable host, HospitalDepartmentFragment view) {
        this.view = view;
        getter = new HospitalDepartmentGetter(host, view.getContext());
        getter.setCallback(this);
    }

    public void getDepartmentListData(String hospitalid, String parenthosdepid) {
        session = getter.requestDepartmentData(hospitalid, parenthosdepid);
    }

    @Override
    public void onRequestOk(List<Department> data) {
        session = null;
        if (data.size() == 0) {
            view.showNoDoctorListResult();
            return;
        }
        view.showDepartmentListData(data);
    }

    @Override
    public void onRequestFailure(String error) {
        session = null;
        view.showRequetErrorView(error);
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorView();
    }

    public void destory() {
        if (session != null) session.cancelRequest();
    }
}
