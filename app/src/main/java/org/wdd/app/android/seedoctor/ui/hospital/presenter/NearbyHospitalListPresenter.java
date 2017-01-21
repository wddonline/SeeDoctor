package org.wdd.app.android.seedoctor.ui.hospital.presenter;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyHospitalListPresenter implements BasePresenter, HospitalListDataGetter.SearchCallback {

    private NearbyHospitalFragment view;
    private HospitalListDataGetter data;

    public NearbyHospitalListPresenter(ActivityFragmentAvaliable host, NearbyHospitalFragment view) {
        this.view = view;
        data = new HospitalListDataGetter(host, view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyHospital() {
        data.getNearbyHospitalList();
    }

    public void reloadNearbyHospital() {
        data.reloadNearbyHospitalList();
    }

    @Override
    public void onSearchOk(List<Hospital> data) {
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
