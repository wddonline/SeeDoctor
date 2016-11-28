package org.wdd.app.android.seedoctor.ui.hospital.presenter;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.HospitalFragment;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class HospitalPresenter implements BasePresenter, HospitalDataGetter.SearchCallback {

    private HospitalFragment view;
    private HospitalDataGetter data;

    public HospitalPresenter(HospitalFragment view) {
        this.view = view;
        data = new HospitalDataGetter(view.getContext());
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
