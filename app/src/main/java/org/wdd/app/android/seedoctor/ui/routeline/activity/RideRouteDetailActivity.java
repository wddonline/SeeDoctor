package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RideStep;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.RideRouteDetailAdapter;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.WalkRouteDetailAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;

public class RideRouteDetailActivity extends BaseActivity {

    public static void show(Activity activity, RidePath ridePath, RideRouteResult result) {
        Intent intent = new Intent(activity, RideRouteDetailActivity.class);
        SDApplication.getInstance().putTempData("ride_path", ridePath);
        SDApplication.getInstance().putTempData("ride_result", result);
        activity.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private RidePath ridePath;
    private RideRouteResult rideRouteResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_route_detail);
        initData();
        initViews();
    }

    private void initData() {
        ridePath = (RidePath) SDApplication.getInstance().getTempData("ride_path");
        rideRouteResult = (RideRouteResult) SDApplication.getInstance().getTempData("ride_result");
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_ride_route_detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        initTitle();
        recyclerView = (RecyclerView) findViewById(R.id.activity_ride_route_detail_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        String dur = AMapUtil.getFriendlyTime((int) ridePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) ridePath.getDistance());
        TextView timeDistanceView = (TextView) findViewById(R.id.activity_ride_route_detail_time_distance);
        timeDistanceView.setText(dur + "(" + dis + ")");

        List<RideStep> rideStepList = new ArrayList<>();
        rideStepList.add(new RideStep());
        rideStepList.addAll(ridePath.getSteps());
        rideStepList.add(new RideStep());
        RideRouteDetailAdapter adapter = new RideRouteDetailAdapter(this, rideStepList);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }

}
