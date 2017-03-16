package org.wdd.app.android.seedoctor.ui.nearby.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.nearby.data.DrugstoreListDataGetter;
import org.wdd.app.android.seedoctor.ui.nearby.fragment.NearbyDrugstoreFragment;
import org.wdd.app.android.seedoctor.ui.nearby.model.Mark;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/13/16.
 */

public class NearbyDrugstoreListPresenter implements BasePresenter, DrugstoreListDataGetter.SearchCallback {

    private NearbyDrugstoreFragment view;
    private DrugstoreListDataGetter data;

    public NearbyDrugstoreListPresenter(ActivityFragmentAvaliable host, NearbyDrugstoreFragment view) {
        this.view = view;
        data = new DrugstoreListDataGetter(host, view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyDrugstores() {
        data.getNearbyHospitalList();
    }

    public void reloadNearbyHospital() {
        data.reloadNearbyHospitalList();
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onSearchOk(List<Mark> data) {
        view.appendHospitalList(data);
    }

    @Override
    public void onSearchFailure() {
        view.handleSearchDataErrorViews(LoadView.LoadStatus.Request_Failure);
    }

    @Override
    public void onSearchNoData() {
        view.handleSearchDataErrorViews(LoadView.LoadStatus.No_Data);
    }

    @Override
    public void onNetworkError() {
        view.handleSearchDataErrorViews(LoadView.LoadStatus.Network_Error);
    }
}
