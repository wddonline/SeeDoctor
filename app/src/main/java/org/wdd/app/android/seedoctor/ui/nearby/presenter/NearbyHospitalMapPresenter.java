package org.wdd.app.android.seedoctor.ui.nearby.presenter;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.nearby.data.HospitalMapDataGetter;
import org.wdd.app.android.seedoctor.ui.nearby.activity.NearbyHospitalMapActivity;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyHospitalMapPresenter implements BasePresenter, HospitalMapDataGetter.SearchCallback {

    private NearbyHospitalMapActivity view;
    private HospitalMapDataGetter data;

    public NearbyHospitalMapPresenter(ActivityFragmentAvaliable host, NearbyHospitalMapActivity view) {
        this.view = view;
        data = new HospitalMapDataGetter(host, view);
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
