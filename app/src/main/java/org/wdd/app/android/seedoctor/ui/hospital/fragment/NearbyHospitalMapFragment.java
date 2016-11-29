package org.wdd.app.android.seedoctor.ui.hospital.fragment;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.hospital.presenter.NearbyHospitalMapPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyHospitalMapFragment extends BaseFragment implements AMap.OnCameraChangeListener {

    private View rootView;
    private MapView mapView;

    private NearbyHospitalMapPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NearbyHospitalMapPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nearby_hospital_map, container, false);
            initViews(savedInstanceState);
        }
        return rootView;
    }

    private void initViews(Bundle savedInstanceState) {
        mapView = (MapView) rootView.findViewById(R.id.fragment_nearby_hospital_map_mapview);
        mapView.onCreate(savedInstanceState);
        AMap aMap = mapView.getMap();
        aMap.setOnCameraChangeListener(this);
        mapView.getMap().setMyLocationEnabled(true);
        aMap.setTrafficEnabled(false);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }

    @Override
    protected void lazyLoad() {
        mapView.setVisibility(View.VISIBLE);
        LocationHelper.Location location = LocationHelper.getInstance(getContext()).getLocation();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(location.latitude, location.longitude),//新的中心点坐标
                15, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        ));
        mapView.getMap().animateCamera(cameraUpdate);
    }

    public List<LatLonPoint> getMapBounds() {
        List<LatLonPoint> points = new ArrayList<>();
        Projection projection = mapView.getMap().getProjection();
        LatLng latLong = new LatLng(mapView.getMap().getMyLocation().getLatitude(), mapView.getMap().getMyLocation().getLatitude());
        //获取中心点坐标
        Point pointCenter = projection.toScreenLocation(latLong);
        Log.e("#####", pointCenter.x + "," + pointCenter.y);
        Log.e("#####", mapView.getWidth() + "," + mapView.getHeight());
        Log.e("#####", mapView.getMeasuredWidth() + "," + mapView.getMeasuredHeight());
        //根据中心点坐标计算四个角的坐标
        Point pointLeftTop = new Point();
        //将四个角的坐标转换为四个角的经纬度

        return points;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        presenter.searchNearbyHospital();
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
