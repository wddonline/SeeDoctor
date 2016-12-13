package org.wdd.app.android.seedoctor.ui.drugstore.presenter;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.drugstore.data.DrugstoreMapDataGetter;
import org.wdd.app.android.seedoctor.ui.drugstore.fragment.NearbyDrugstoreMapFragment;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalMapDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalMapFragment;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyDrugstoreMapPresenter implements BasePresenter, DrugstoreMapDataGetter.SearchCallback {

    private NearbyDrugstoreMapFragment view;
    private DrugstoreMapDataGetter data;

    public NearbyDrugstoreMapPresenter(NearbyDrugstoreMapFragment view) {
        this.view = view;
        data = new DrugstoreMapDataGetter(view.getContext());
        data.setSearchCallback(this);
    }

    public void searchNearbyHospital(LatLonPoint centerPoint, int radius) {
        data.getNearbyHospitalList(centerPoint, radius);
    }

    @Override
    public void onSearchOk(List<PoiItem> data) {
        view.showHospitalMarker(data);
    }

    @Override
    public void onSearchFailure() {

    }
}
