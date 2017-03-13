package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDepartmentActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDepartmentGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class WikiDepartmentPresenter implements WikiDepartmentGetter.DepartmentCallback {

    private WikiDepartmentActivity view;
    private WikiDepartmentGetter getter;
    private HttpSession session;

    public WikiDepartmentPresenter(ActivityFragmentAvaliable host, WikiDepartmentActivity view) {
        this.view = view;
        getter = new WikiDepartmentGetter(host, view.getBaseContext());
        getter.setDepartmentCallback(this);
    }

    public void getDepartmentListData() {
        session = getter.requestDepartmentData();
    }

    public void destory() {
        if (session != null) {
            session.cancelRequest();
        }
    }

    @Override
    public void onDataGetted(List<Department> data) {
        session = null;
        if (data.size() == 0) {
            view.showNoDataViews();
            return;
        }
        view.showEmergencyListViews(data);
    }

    @Override
    public void onFailure(String error) {
        session = null;
        view.showDataGettedFailureViews(error);
    }

    @Override
    public void onNetworkError() {
        session = null;
        view.showNetworkErrorViews();
    }
}
