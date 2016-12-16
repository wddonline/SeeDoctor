package org.wdd.app.android.seedoctor.ui.hospital.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.location.LocationFinder;

/**
 * Created by richard on 12/16/16.
 */

public class HospitalGetter {

    private Context context;
    private LocationFinder.LocationListener listener;

    public HospitalGetter(Context context, LocationFinder.LocationListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void getCurrentLocation() {
        LocationFinder finder = new LocationFinder(context);
        finder.setLocationListener(listener);
        finder.start(true);
    }
}
