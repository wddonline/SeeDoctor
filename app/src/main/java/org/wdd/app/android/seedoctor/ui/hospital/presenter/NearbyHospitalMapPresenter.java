package org.wdd.app.android.seedoctor.ui.hospital.presenter;

import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalMapDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalMapFragment;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyHospitalMapPresenter implements BasePresenter, HospitalMapDataGetter.SearchCallback {

    private NearbyHospitalMapFragment view;
    private HospitalMapDataGetter data;

    public NearbyHospitalMapPresenter(NearbyHospitalMapFragment view) {
        this.view = view;
        data = new HospitalMapDataGetter(view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyHospital() {
        data.getNearbyHospitalList(view.getMapBounds());
    }

    @Override
    public void onSearchOk(List<PoiItem> data) {
    }

    @Override
    public void onSearchFailure() {
    }
}
