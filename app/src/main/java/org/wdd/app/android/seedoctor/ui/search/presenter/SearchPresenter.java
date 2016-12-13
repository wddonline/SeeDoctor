package org.wdd.app.android.seedoctor.ui.search.presenter;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.search.activity.SearchActivity;
import org.wdd.app.android.seedoctor.ui.search.data.SearchGetter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class SearchPresenter implements BasePresenter, SearchGetter.SearchCallback {

    private SearchActivity view;
    private SearchGetter getter;

    public SearchPresenter(SearchActivity view) {
        this.view = view;
        getter = new SearchGetter(view, this);
    }

    public void searchHospitalByName(String name, boolean append) {
        getter.getHospitalByName(name, append);
    }

    @Override
    public void onSearchOk(List<Hospital> data) {
        view.showHospitalDataView(data);
    }

    @Override
    public void onNetworkError() {
        view.handleRequestErrorViews(LoadView.LoadStatus.Network_Error);
    }

    @Override
    public void onSearchFailure() {
        view.handleRequestErrorViews(LoadView.LoadStatus.Request_Failure);
    }

    @Override
    public void onSearchNoData() {
        view.handleRequestErrorViews(LoadView.LoadStatus.No_Data);
    }
}
