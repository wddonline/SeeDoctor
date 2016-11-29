package org.wdd.app.android.seedoctor.ui.hospital.presenter;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalListFragment;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyHospitalListPresenter implements BasePresenter, HospitalListDataGetter.SearchCallback {

    private NearbyHospitalListFragment view;
    private HospitalListDataGetter data;

    public NearbyHospitalListPresenter(NearbyHospitalListFragment view) {
        this.view = view;
        data = new HospitalListDataGetter(view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyHospital(boolean isRefshing) {
        data.getNearbyHospitalList(isRefshing);
    }

    @Override
    public void onSearchOk(boolean isRefreshing, List<Hospital> data) {
        view.appendHospitalList(isRefreshing, data);
    }

    @Override
    public void onSearchFailure(boolean isRefreshing) {
        view.handleSearchErrorViews(isRefreshing);
    }
}
