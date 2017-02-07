package org.wdd.app.android.seedoctor.ui.routeline.data;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

/**
 * Created by richard on 12/1/16.
 */

public class RouteLineDataGetter implements RouteSearch.OnRouteSearchListener {

    private Context context;
    private RouteSearch routeSearch;
    private LocationHelper locationHelper;
    private SearchCallback callback;
    private ActivityFragmentAvaliable host;
    private LatLng start;
    private LatLng dst;

    public RouteLineDataGetter(ActivityFragmentAvaliable host, Context context, LatLng dst) {
        this.host = host;
        this.context = context;
        this.dst = dst;
        locationHelper = LocationHelper.getInstance(context);
        start = new LatLng(locationHelper.getLatitude(), locationHelper.getLongitude());
        routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);
    }

    public void searchBusRouteLineData() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude),
                new LatLonPoint(dst.latitude, dst.longitude));
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk,
                locationHelper.getCity_code(), 0);
        routeSearch.calculateBusRouteAsyn(query);
    }

    public void searchDriveRouteLineData() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude),
                new LatLonPoint(dst.latitude, dst.longitude));
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    public void searchRideRouteLineData() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude),
                new LatLonPoint(dst.latitude, dst.longitude));
        RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RIDING_DEFAULT);
        routeSearch.calculateRideRouteAsyn(query);
    }

    public void searchWalkRouteLineData() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude),
                new LatLonPoint(dst.latitude, dst.longitude));
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int rCode) {
        if (!host.isAvaliable()) return;
        if (callback == null) return;
        if (rCode == 1000) {
            if (busRouteResult == null || busRouteResult.getPaths() == null || busRouteResult.getPaths().size() == 0) {
                callback.onNoRouteFound();
            } else {
                callback.onBusRouteSearched(busRouteResult);
            }
        } else {
            callback.onNoRouteFound();
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
        if (!host.isAvaliable()) return;
        if (callback == null) return;
        if (rCode == 1000) {
            if (driveRouteResult == null || driveRouteResult.getPaths() == null || driveRouteResult.getPaths().size() == 0) {
                callback.onNoRouteFound();
            } else {
                callback.onDriveRouteSearched(driveRouteResult);
            }
        } else {
            callback.onNoRouteFound();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int rCode) {
        if (!host.isAvaliable()) return;
        if (callback == null) return;
        if (rCode == 1000) {
            if (rideRouteResult == null || rideRouteResult.getPaths() == null || rideRouteResult.getPaths().size() == 0) {
                callback.onNoRouteFound();
            } else {
                callback.onRideRouteSearched(rideRouteResult);
            }
        } else {
            callback.onNoRouteFound();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int rCode) {
        if (!host.isAvaliable()) return;
        if (callback == null) return;
        if (rCode == 1000) {
            if (walkRouteResult == null || walkRouteResult.getPaths() == null || walkRouteResult.getPaths().size() == 0) {
                callback.onNoRouteFound();
            } else {
                if (callback != null) callback.onWalkRouteSearched(walkRouteResult);
            }
        } else {
            callback.onNoRouteFound();
        }
    }

    public void addCallback(SearchCallback callback) {
        this.callback = callback;
    }

    public interface SearchCallback {

        void onBusRouteSearched(BusRouteResult result);

        void onDriveRouteSearched(DriveRouteResult result);

        void onRideRouteSearched(RideRouteResult result);

        void onWalkRouteSearched(WalkRouteResult result);

        void onNoRouteFound();

    }
}
