package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.navigation.activity.NavigationActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.BusRouteLineAdapter;
import org.wdd.app.android.seedoctor.ui.routeline.presenter.RouteLinePresenter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;
import org.wdd.app.android.seedoctor.utils.DensityUtils;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;

/**
 * Created by richard on 11/30/16.
 */

public class RouteLineActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        BusRouteLineAdapter.OnItemClickedListener {

    public static void show(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context, RouteLineActivity.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("lon", longitude);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private enum Status {
        NORMAL,
        BUS_MODE
    }

    private Toolbar toolbar;
    private TextView titleView;
    private MapView mapView;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private View bottomLayout;
    private TextView timeDistanceView;
    private TextView taxtView;
    private View bottomBar;

    private AMap aMap;
    private RouteLinePresenter presenter;
    private Status status = Status.NORMAL;
    private BusPath selectedBusPath;
    private BusRouteResult busRouteResult;
    private BusRouteOverlay busRouteOverlay;

    private double lat;
    private double lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_line);
        initData();
        initViews(savedInstanceState);
    }

    private void initData() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        presenter = new RouteLinePresenter(this, new LatLng(lat, lon));
    }

    private void initViews(Bundle savedInstanceState) {
        initTitle();

        mapView = (MapView) findViewById(R.id.activity_route_line_mapview);
        radioGroup = (RadioGroup) findViewById(R.id.activity_route_line_traffic);
        recyclerView = (RecyclerView) findViewById(R.id.activity_route_line_recyclerview);
        bottomLayout = findViewById(R.id.activity_route_line_bottom_layout);
        timeDistanceView = (TextView) findViewById(R.id.activity_route_line_time_distance);
        taxtView = (TextView) findViewById(R.id.activity_route_line_time_tax_cost);
        bottomBar = findViewById(R.id.activity_route_line_bottom_layout);

        aMap = mapView.getMap();
        mapView.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        decoration.setLeftOffset(DensityUtils.dip2px(this, 16));
        recyclerView.addItemDecoration(decoration);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(lat, lon),//新的中心点坐标
                15, //新的缩放级别
                0, //俯仰角0°~45°（垂直与地图时为0）
                0  ////偏航角 0~360° (正北方为0)
        ));
        mapView.getMap().animateCamera(cameraUpdate);
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_route_line_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {
                    case NORMAL:
                        finish();
                        break;
                    default:
                        status = Status.NORMAL;
                        setStatusViews();
                        break;
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LocationHelper locationHelper = LocationHelper.getInstance(getBaseContext());
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.activity_root_line_drive:
                        NavigationActivity.show(getBaseContext(), NavigationActivity.NAVI_DRIVE, locationHelper.getLatitude(),
                                locationHelper.getLongitude(), lat, lon);
                        break;
                    case R.id.activity_root_line_walk:
                        NavigationActivity.show(getBaseContext(), NavigationActivity.NAVI_WALK, locationHelper.getLatitude(),
                                locationHelper.getLongitude(), lat, lon);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        titleView = (TextView) findViewById(R.id.activity_route_line_title);
    }

    private void setStatusViews() {
        switch (status) {
            case NORMAL:
                mapView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                titleView.setVisibility(View.GONE);
                bottomBar.setVisibility(View.GONE);
                break;
            case BUS_MODE:
                mapView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                titleView.setVisibility(View.VISIBLE);
                if (selectedBusPath == null) return;
                titleView.setText(AMapUtil.getBusPathTitle(selectedBusPath));
                bottomBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_line, menu);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.activity_root_line_drive);
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_root_line_bus:
                toolbar.getMenu().getItem(0).setVisible(false);
                presenter.searchBusRouteLineData();
                break;
            case R.id.activity_root_line_drive:
                toolbar.getMenu().getItem(0).setVisible(true);
                presenter.searchDriveRouteLineData();
                break;
            case R.id.activity_root_line_walk:
                toolbar.getMenu().getItem(0).setVisible(true);
                presenter.searchWalkRouteLineData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (status) {
            case NORMAL:
                super.onBackPressed();
                break;
            default:
                status = Status.NORMAL;
                setStatusViews();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void showBusRouteOnMap(BusRouteResult result) {
        busRouteResult = result;
        recyclerView.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        aMap.clear();
        BusRouteLineAdapter adapter = new BusRouteLineAdapter(this, result.getPaths());
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);
    }

    public void showDriveRouteOnMap(final DriveRouteResult result) {
        recyclerView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        aMap.clear();
        final DrivePath drivePath = result.getPaths().get(0);
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                this, aMap, drivePath,
                result.getStartPos(),
                result.getTargetPos(), null);
        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();

        int dis = (int) drivePath.getDistance();
        int dur = (int) drivePath.getDuration();
        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
        timeDistanceView.setText(des);
        taxtView.setVisibility(View.VISIBLE);
        int taxiCost = (int) result.getTaxiCost();
        taxtView.setText(String.format(getString(R.string.tax_cost), taxiCost));

        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriveRouteDetailActivity.show(RouteLineActivity.this, drivePath, result);
            }
        });
    }

    public void showWalkRouteOnMap(final WalkRouteResult result) {
        recyclerView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        aMap.clear();
        final WalkPath walkPath = result.getPaths().get(0);
        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                this, aMap, walkPath,
                result.getStartPos(),
                result.getTargetPos());
        walkRouteOverlay.removeFromMap();
        walkRouteOverlay.addToMap();
        walkRouteOverlay.zoomToSpan();

        int dis = (int) walkPath.getDistance();
        int dur = (int) walkPath.getDuration();
        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
        timeDistanceView.setText(des);
        taxtView.setVisibility(View.GONE);
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalkRouteDetailActivity.show(RouteLineActivity.this, walkPath, result);
            }
        });
    }

    @Override
    public void onItemClicked(int position, BusPath path) {
        status = Status.BUS_MODE;
        selectedBusPath = path;
        setStatusViews();
        aMap.clear();
        if (busRouteResult == null) return;
        if (busRouteOverlay != null) busRouteOverlay.removeFromMap();
        busRouteOverlay = new BusRouteOverlay(this, mapView.getMap(), path, busRouteResult.getStartPos(),
                busRouteResult.getTargetPos());
        busRouteOverlay.addToMap();
        busRouteOverlay.zoomToSpan();

        int dis = (int) path.getDistance();
        int dur = (int) path.getDuration();
        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
        timeDistanceView.setText(des);
        taxtView.setVisibility(View.VISIBLE);
        int taxiCost = (int) busRouteResult.getTaxiCost();
        taxtView.setText(String.format(getString(R.string.tax_cost), taxiCost));
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusRouteDetailActivity.show(RouteLineActivity.this, selectedBusPath, busRouteResult);
            }
        });
    }

}

