package org.wdd.app.android.seedoctor.ui.welcome.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;

import org.wdd.app.android.seedoctor.location.LocationFinder;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.welcome.activity.WelcomeActivity;

/**
 * Created by wangdd on 16-11-27.
 */

public class WelcomePresenter implements BasePresenter, LocationFinder.LocationListener {

    private LocationFinder finder;
    private WelcomeActivity activity;

    public WelcomePresenter(WelcomeActivity activity) {
        this.activity = activity;
        finder = new LocationFinder(activity);
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
        activity.jumpToNextActivity();
    }

    @Override
    public void onLocationError(String error) {
        activity.jumpToNextActivity();
    }
}
