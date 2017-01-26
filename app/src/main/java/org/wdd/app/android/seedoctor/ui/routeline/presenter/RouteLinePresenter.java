package org.wdd.app.android.seedoctor.ui.routeline.presenter;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkRouteResult;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;
import org.wdd.app.android.seedoctor.ui.routeline.data.RouteLineDataGetter;

/**
 * Created by richard on 12/1/16.
 */

public class RouteLinePresenter implements BasePresenter, RouteLineDataGetter.SearchCallback {

    private RouteLineActivity view;
    private RouteLineDataGetter dataGetter;

    public RouteLinePresenter(ActivityFragmentAvaliable host, RouteLineActivity view, LatLng dst) {
        this.view = view;
        dataGetter = new RouteLineDataGetter(host, view, dst);
        dataGetter.addCallback(this);
    }

    public void searchBusRouteLineData() {
        dataGetter.searchBusRouteLineData();
    }

    public void searchDriveRouteLineData() {
        dataGetter.searchDriveRouteLineData();
    }

    public void searchWalkRouteLineData() {
        dataGetter.searchWalkRouteLineData();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result) {
        view.showBusRouteOnMap(result);
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result) {
        view.showDriveRouteOnMap(result);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result) {
        view.showWalkRouteOnMap(result);
    }

    @Override
    public void onNoRouteFound() {
        view.showNoRouteViews();
    }

}
