package org.wdd.app.android.seedoctor.ui.drugstore.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.drugstore.presenter.NearbyDrugstoreMapPresenter;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyDrugstoreMapFragment extends BaseFragment implements AMap.OnCameraChangeListener,
        AMap.OnMarkerClickListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener,
        AMap.InfoWindowAdapter {

    private View rootView;
    private MapView mapView;

    private NearbyDrugstoreMapPresenter presenter;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private List<Marker> markers;
    private Marker focusMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NearbyDrugstoreMapPresenter(this);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nearby_drugstore_map, container, false);
            initData();
            initViews(savedInstanceState);
        }
        return rootView;
    }

    private void initData() {
        markers = new ArrayList<>();
    }

    private void initViews(Bundle savedInstanceState) {
        mapView = (MapView) rootView.findViewById(R.id.fragment_nearby_drugstore_map_mapview);
        mapView.onCreate(savedInstanceState);

        AMap aMap = mapView.getMap();
        aMap.setMyLocationEnabled(true);
        aMap.setLocationSource(this);
        aMap.setTrafficEnabled(false);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        UiSettings settings = mapView.getMap().getUiSettings();
        settings.setCompassEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        aMap.setOnCameraChangeListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Projection projection = mapView.getMap().getProjection();

        LatLng latLng1 = projection.fromScreenLocation(new Point((int)(mapView.getWidth() * 0.5f), (int)(mapView.getHeight() * 0.5f)));
        LatLonPoint centerPoint = new LatLonPoint(latLng1.latitude, latLng1.longitude);

        LatLng latLng2 = projection.fromScreenLocation(new Point(0, 0));

        int distance = (int) AMapUtils.calculateLineDistance(latLng1, latLng2);
        presenter.searchNearbyHospital(centerPoint, distance);
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

    public void showHospitalMarker(List<PoiItem> data) {
        for (Marker marker : markers) {
            if (focusMarker != null && focusMarker.getPosition().latitude == marker.getPosition().latitude &&
                    focusMarker.getPosition().longitude == marker.getPosition().longitude) {
                continue;
            }
            marker.remove();
        }
        markers.clear();

        LatLonPoint latLonPoint;
        LatLng latLng;
//        Point point;
        for (PoiItem item : data) {
            if (focusMarker != null && focusMarker.getPosition().latitude == item.getLatLonPoint().getLatitude() &&
                    focusMarker.getPosition().longitude == item.getLatLonPoint().getLongitude()) {
                continue;
            }
            latLonPoint = item.getLatLonPoint();
            latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            Marker marker = mapView.getMap().addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(item.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.bubble_drugstore)))
                    .draggable(true));
            marker.setObject(item);

            markers.add(marker);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        focusMarker = marker;
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (focusMarker == null) return;
        focusMarker.hideInfoWindow();
        focusMarker = null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        final PoiItem poiItem = (PoiItem) marker.getObject();
        View view = View.inflate(getContext(), R.layout.layout_hospital_info_window, null);
        TextView nameView = (TextView) view.findViewById(R.id.layout_hospital_window_name);
        nameView.setMaxWidth(DensityUtils.dip2px(getContext(), mapView.getWidth() * 0.7f));
        nameView.setText(poiItem.getTitle());
        TextView levelView = (TextView) view.findViewById(R.id.layout_hospital_window_level);
        if (!TextUtils.isEmpty(poiItem.getTypeDes())) {
            String[] types = poiItem.getTypeDes().split(";");
            levelView.setText(types[types.length - 1]);
        }
        Button goButton = (Button) view.findViewById(R.id.layout_hospital_window_go);
        goButton.setTag(marker.getPosition());
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteLineActivity.show(getContext(), poiItem.getLatLonPoint().getLatitude(),
                        poiItem.getLatLonPoint().getLongitude());
            }
        });
        Button callButton = (Button) view.findViewById(R.id.layout_hospital_window_call);
        callButton.setTag(poiItem.getTel());
        if (TextUtils.isEmpty(poiItem.getTel())) callButton.setEnabled(false);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + poiItem.getTel()));
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void resetHospitalData() {
        lazyLoad();
    }
}
