package org.wdd.app.android.seedoctor.ui.hospital.presenter;

import com.amap.api.location.AMapLocation;

import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.location.LocationFinder;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalGetter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalListFragment;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyHospitalPresenter implements BasePresenter, LocationFinder.LocationListener {

    private NearbyHospitalFragment view;
    private HospitalGetter getter;

    public NearbyHospitalPresenter(NearbyHospitalFragment view) {
        this.view = view;
        getter = new HospitalGetter(view.getContext());
    }

    public void getCurrentLocation() {
        getter.setLocationListener(this);
        getter.getCurrentLocation();
    }

    @Override
    public void onLocationGeted(LatLong latLong) {
        getter.removeLocationListener(this);
        view.reloadHospitalData();
    }

    @Override
    public void onLocationError(String error) {
        view.getCurrentLocationFailure(error);
    }

    public void destory() {
        getter.onDestory();
    }
}
