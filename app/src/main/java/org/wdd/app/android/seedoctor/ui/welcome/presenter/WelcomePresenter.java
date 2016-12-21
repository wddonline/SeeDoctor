package org.wdd.app.android.seedoctor.ui.welcome.presenter;

import com.amap.api.location.AMapLocation;

import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.location.LocationFinder;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.welcome.activity.WelcomeActivity;

/**
 * Created by wangdd on 16-11-27.
 */

public class WelcomePresenter implements BasePresenter, LocationFinder.LocationListener {

    private LocationFinder finder;
    private WelcomeActivity view;

    private long startTime;

    public WelcomePresenter(WelcomeActivity view) {
        this.view = view;
        finder = LocationFinder.getInstance(view.getBaseContext());
        finder.addLocationListener(this);
    }

    public void findLocation() {
        startTime = System.currentTimeMillis();
        finder.start();
    }

    public void destory() {
        finder.removeLocationListener(this);
    }

    @Override
    public void onLocationGeted(LatLong location) {
        finder.removeLocationListener(this);
        long endTime = System.currentTimeMillis();
        boolean immediately = true;
        if (endTime - startTime < 3000) {
            immediately = false;
        }
        view.jumpToNextActivity(immediately);
    }

    @Override
    public void onLocationError(String error) {
        view.jumpToNextActivity(true);
    }

    public void exitApp() {
        finder.stop();
    }
}
