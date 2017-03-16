package org.wdd.app.android.seedoctor.ui.nearby.presenter;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.nearby.data.DrugstoreMapDataGetter;
import org.wdd.app.android.seedoctor.ui.nearby.activity.NearbyDrugstoreMapActivity;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyDrugstoreMapPresenter implements BasePresenter, DrugstoreMapDataGetter.SearchCallback {

    private NearbyDrugstoreMapActivity view;
    private DrugstoreMapDataGetter data;

    public NearbyDrugstoreMapPresenter(ActivityFragmentAvaliable host, NearbyDrugstoreMapActivity view) {
        this.view = view;
        data = new DrugstoreMapDataGetter(host, view);
        data.setSearchCallback(this);
    }

    public void searchNearbyHospital(LatLonPoint centerPoint, int radius) {
        data.getNearbyHospitalList(centerPoint, radius);
    }

    @Override
    public void cancelRequest() {
        
    }

    @Override
    public void onSearchOk(List<PoiItem> data) {
        view.showHospitalMarker(data);
    }

    @Override
    public void onSearchFailure() {

    }
}
