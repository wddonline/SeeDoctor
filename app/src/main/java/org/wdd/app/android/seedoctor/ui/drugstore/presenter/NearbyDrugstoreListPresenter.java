package org.wdd.app.android.seedoctor.ui.drugstore.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.drugstore.data.DrugstoreListDataGetter;
import org.wdd.app.android.seedoctor.ui.drugstore.fragment.NearbyDrugstoreFragment;
import org.wdd.app.android.seedoctor.ui.drugstore.model.Drugstore;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 12/13/16.
 */

public class NearbyDrugstoreListPresenter implements BasePresenter, DrugstoreListDataGetter.SearchCallback {

    private NearbyDrugstoreFragment view;
    private DrugstoreListDataGetter data;

    public NearbyDrugstoreListPresenter(ActivityFragmentAvaliable holder, NearbyDrugstoreFragment view) {
        this.view = view;
        data = new DrugstoreListDataGetter(holder, view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyDrugstores() {
        data.getNearbyHospitalList();
    }

    public void reloadNearbyHospital() {
        data.reloadNearbyHospitalList();
    }

    @Override
    public void onSearchOk(List<Drugstore> data) {
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
