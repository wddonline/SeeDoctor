package org.wdd.app.android.seedoctor.ui.drugstore.presenter;

import com.amap.api.location.AMapLocation;

import org.wdd.app.android.seedoctor.location.LocationFinder;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.drugstore.data.DrugstoreGetter;
import org.wdd.app.android.seedoctor.ui.drugstore.fragment.NearbyDrugstoreFragment;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalGetter;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;

/**
 * Created by richard on 11/28/16.
 */

public class NearbyDrugstorePresenter implements BasePresenter, LocationFinder.LocationListener {

    private NearbyDrugstoreFragment view;
    private DrugstoreGetter getter;

    public NearbyDrugstorePresenter(NearbyDrugstoreFragment view) {
        this.view = view;
        getter = new DrugstoreGetter(view.getContext());
    }

    public void getCurrentLocation() {
        getter.setLocationListener(this);
        getter.getCurrentLocation();
    }

    @Override
    public void onLocationGeted(AMapLocation location) {
        getter.removeLocationListener(this);
        view.reloadHospitalData();
    }

    @Override
    public void onLocationError(String error) {
        view.getCurrentLocationFailure(error);
    }
}
