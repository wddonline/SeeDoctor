package org.wdd.app.android.seedoctor.ui.routeline.data;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.utils.AppToaster;

/**
 * Created by richard on 12/1/16.
 */

public class RouteLineDataGetter implements RouteSearch.OnRouteSearchListener {

    private Context context;
    private RouteSearch routeSearch;
    private LocationHelper locationHelper;
    private SearchCallback callback;
    private LatLng start;
    private LatLng dst;

    public RouteLineDataGetter(Context context, LatLng dst) {
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

    public void searchWalkRouteLineData() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude),
                new LatLonPoint(dst.latitude, dst.longitude));
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int rCode) {
        if (rCode == 1000) {
            if (busRouteResult == null || busRouteResult.getPaths() == null || busRouteResult.getPaths().size() == 0) {
                AppToaster.show(R.string.no_routline_data);
            } else {
                if (callback != null) callback.onBusRouteSearched(busRouteResult);
            }
        } else {
            AppToaster.show(R.string.no_routline_data);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
        if (rCode == 1000) {
            if (driveRouteResult == null || driveRouteResult.getPaths() == null || driveRouteResult.getPaths().size() == 0) {
                AppToaster.show(R.string.no_routline_data);
            } else {
                if (callback != null) callback.onDriveRouteSearched(driveRouteResult);
            }
        } else {
            AppToaster.show(R.string.no_routline_data);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int rCode) {
        if (rCode == 1000) {
            if (walkRouteResult == null || walkRouteResult.getPaths() == null || walkRouteResult.getPaths().size() == 0) {
                AppToaster.show(R.string.no_routline_data);
            } else {
                if (callback != null) callback.onWalkRouteSearched(walkRouteResult);
            }
        } else {
            AppToaster.show(R.string.no_routline_data);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int rCode) {

    }

    public void addCallback(SearchCallback callback) {
        this.callback = callback;
    }

    public interface SearchCallback {

        void onBusRouteSearched(BusRouteResult result);

        void onDriveRouteSearched(DriveRouteResult result);

        void onWalkRouteSearched(WalkRouteResult result);

    }
}
