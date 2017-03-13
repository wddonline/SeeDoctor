package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugCategoryActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDrugCategoryGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;

import java.util.List;
import java.util.Map;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDrugCategoryPresenter implements BasePresenter, WikiDrugCategoryGetter.WikiDrugCategoryDataCallback {

    private WikiDrugCategoryActivity view;
    private WikiDrugCategoryGetter getter;
    private HttpSession session;

    public WikiDrugCategoryPresenter(ActivityFragmentAvaliable host, WikiDrugCategoryActivity view) {
        this.view = view;
        getter = new WikiDrugCategoryGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getDrugCategoryListData() {
        session = getter.requestDrugCategoryList();
    }

    @Override
    public void onRequestOk(Map<String, Map<String, List<DrugCategory>>> data) {
        session = null;
        if (data.size() == 0) {
            view.showNoDiseaseListResult();
            return;
        }
        view.showDiseaseListData(data);
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
