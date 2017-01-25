package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.DriveRouteDetailAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

public class DriveRouteDetailActivity extends BaseActivity {

    public static void show(Activity activity, DrivePath drivePath, DriveRouteResult driveRouteResult) {
        Intent intent = new Intent(activity, DriveRouteDetailActivity.class);
        SDApplication.getInstance().putTempData("drive_path", drivePath);
        SDApplication.getInstance().putTempData("drive_result", driveRouteResult);
        activity.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private DrivePath drivePath;
    private DriveRouteResult driveRouteResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_route_detail);
        initData();
        initViews();
    }

    private void initData() {
        drivePath = (DrivePath) SDApplication.getInstance().getTempData("drive_path");
        driveRouteResult = (DriveRouteResult) SDApplication.getInstance().getTempData("drive_result");
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_drive_route_detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
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
        recyclerView = (RecyclerView) findViewById(R.id.activity_drive_route_detail_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String dur = AMapUtil.getFriendlyTime((int) drivePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) drivePath.getDistance());

        TextView timeDistanceView = (TextView) findViewById(R.id.activity_drive_route_detail_time_distance);
        timeDistanceView.setText(dur + "(" + dis + ")");

        int taxCost = (int) driveRouteResult.getTaxiCost();
        TextView taxCostView = (TextView) findViewById(R.id.activity_drive_route_detail_tax_cost);
        taxCostView.setText(String.format(getString(R.string.tax_cost), taxCost));

        List<DriveStep> data = drivePath.getSteps();
        data.add(0, new DriveStep());
        data.add(data.size(), new DriveStep());
        DriveRouteDetailAdapter adapter = new DriveRouteDetailAdapter(this, data);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }
}
