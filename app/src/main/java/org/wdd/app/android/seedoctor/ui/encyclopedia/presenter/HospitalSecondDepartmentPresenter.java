package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalSecondDepartmentAdtivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.HospitalDepartmentGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalSecondDepartmentPresenter implements BasePresenter, HospitalDepartmentGetter.DepartmentDataCallback {

    private HospitalSecondDepartmentAdtivity view;
    private HospitalDepartmentGetter getter;

    public HospitalSecondDepartmentPresenter(ActivityFragmentAvaliable host, HospitalSecondDepartmentAdtivity view) {
        this.view = view;
        getter = new HospitalDepartmentGetter(host, view);
        getter.setCallback(this);
    }

    public void getDepartmentListData(String hospitalid, String parenthosdepid) {
        getter.requestDepartmentData(hospitalid, parenthosdepid);
    }

    @Override
    public void onRequestOk(List<Department> data) {
        if (data.size() == 0) {
            view.showNoDoctorListResult();
            return;
        }
        view.showDepartmentListData(data);
    }

    @Override
    public void onRequestFailure(String error) {
        view.showRequetErrorView(error);
    }

    @Override
    public void onNetworkError() {
        view.showNetworkErrorView();
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
