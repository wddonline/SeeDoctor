package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DepartmentDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.DepartmentDetailGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class DepartmentDetailPresenter implements DepartmentDetailGetter.DepartmentDetailCallback {

    private DepartmentDetailActivity view;
    private DepartmentDetailGetter getter;
    private HttpSession departmentSession;
    private HttpSession diseaseSession;

    public DepartmentDetailPresenter(ActivityFragmentAvaliable holder, DepartmentDetailActivity view) {
        this.view = view;
        getter = new DepartmentDetailGetter(holder, view.getBaseContext());
        getter.setDepartmentDetailCallback(this);
    }

    public void getDepartmentDetailData(String departmentid) {
        departmentSession = getter.requestDepartmentData(departmentid);
    }

    public void getDiseaseListByDepartmentId(String departmentid) {
        diseaseSession = getter.requestDiseaseData(departmentid);
    }

    public void destory() {
        if (departmentSession != null) {
            departmentSession.cancelRequest();
        }
        if (diseaseSession != null) {
            diseaseSession.cancelRequest();
        }
    }

    @Override
    public void onDataGetted(DepartmentDetailGetter.Type type, Object data) {
        switch (type) {
            case Department:
                departmentSession = null;
                view.showDepartmentDetailViews((Department) data);
                break;
            case Disease:
                diseaseSession = null;
                view.showDiseaseListViews((List<Disease>) data);
                break;
        }
    }

    @Override
    public void onFailure(DepartmentDetailGetter.Type type, HttpError error) {
        switch (type) {
            case Department:
                departmentSession = null;
                view.showDataGettedFailureViews(error.getErrorMsg());
                break;
            case Disease:
                diseaseSession = null;
                break;
        }
    }

    @Override
    public void onNetworkError(DepartmentDetailGetter.Type type) {
        switch (type) {
            case Department:
                departmentSession = null;
                view.showNetworkErrorViews();
                break;
            case Disease:
                diseaseSession = null;
                break;
        }
    }
}
