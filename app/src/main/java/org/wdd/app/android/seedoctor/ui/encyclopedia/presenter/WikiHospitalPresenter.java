package org.wdd.app.android.seedoctor.ui.encyclopedia.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiHospitalActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiHospitalGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiHospitalPresenter implements BasePresenter, WikiHospitalGetter.WikiHospitalDataCallback {

    private WikiHospitalActivity view;
    private WikiHospitalGetter getter;

    public WikiHospitalPresenter(ActivityFragmentAvaliable host, WikiHospitalActivity view) {
        this.view = view;
        getter = new WikiHospitalGetter(host, view.getBaseContext());
        getter.setCallback(this);
    }

    public void getHospitalListData(String provinceid, String hospitallevel, boolean refresh) {
        getter.requestHospitalList(provinceid, hospitallevel, refresh);
    }

    @Override
    public void onRequestOk(List<Hospital> data, boolean refresh) {
        if (data.size() == 0) {
            view.showNoHospitalListResult(refresh);
            return;
        }
        view.showHospitalListData(data, refresh);
    }

    @Override
    public void onRequestFailure(String error, boolean refresh) {
        view.showRequetErrorView(error, refresh);
    }

    @Override
    public void onNetworkError(boolean refresh) {
        view.showNetworkErrorView(refresh);
    }

    public void cancelRequest() {
        getter.cancelRequest();
    }
}
