package org.wdd.app.android.seedoctor.ui.welcome.presenter;

import com.amap.api.location.AMapLocation;

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

    public WelcomePresenter(WelcomeActivity view) {
        this.view = view;
        finder = new LocationFinder(view);
        finder.setLocationListener(this);
    }

    public void findLocation() {
        finder.start(true);
    }

    public void destory() {
        if (finder.isLocating()) {
            finder.stop();
        }
    }

    @Override
    public void onLocationGeted(AMapLocation location) {
        view.jumpToNextActivity(true);
    }

    @Override
    public void onLocationError(String error) {
        view.jumpToNextActivity(true);
    }
}
