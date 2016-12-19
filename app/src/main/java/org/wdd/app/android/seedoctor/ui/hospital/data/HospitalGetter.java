package org.wdd.app.android.seedoctor.ui.hospital.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.location.LocationFinder;

/**
 * Created by richard on 12/16/16.
 */

public class HospitalGetter {

    private Context context;
    private LocationFinder finder;

    public HospitalGetter(Context context) {
        this.context = context;
        finder = LocationFinder.getInstance(context);
    }

    public void getCurrentLocation() {
        finder.start();
    }

    public void setLocationListener(LocationFinder.LocationListener listener) {
        finder.addLocationListener(listener);
    }

    public void removeLocationListener(LocationFinder.LocationListener listener) {
        finder.removeLocationListener(listener);
    }

    public void onDestory() {
        if (finder == null) return;
        finder.stop();
    }
}
