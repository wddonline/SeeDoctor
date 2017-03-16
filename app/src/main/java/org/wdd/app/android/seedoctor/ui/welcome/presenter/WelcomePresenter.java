package org.wdd.app.android.seedoctor.ui.welcome.presenter;

import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.location.LocationFinder;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.welcome.activity.WelcomeActivity;

/**
 * Created by wangdd on 16-11-27.
 */

public class WelcomePresenter implements BasePresenter, LocationFinder.LocationListener {

    private LocationFinder finder;
    private WelcomeActivity view;
    private ActivityFragmentAvaliable host;

    public WelcomePresenter(ActivityFragmentAvaliable host, WelcomeActivity view) {
        this.host = host;
        this.view = view;
        finder = LocationFinder.getInstance(view.getBaseContext());
        finder.addLocationListener(this);
    }

    public void findLocation() {
        finder.start();
    }

    @Override
    public void cancelRequest() {

    }

    public void destory() {
        finder.removeLocationListener(this);
    }

    @Override
    public void onLocationGeted(LatLong location) {
        if (!host.isAvaliable()) return;
        finder.removeLocationListener(this);
        view.showSplashAds();
    }

    @Override
    public void onLocationError(String error) {
        if (!host.isAvaliable()) return;
        view.showSplashAds();
    }

    public void exitApp() {
        finder.stop();
    }
}
