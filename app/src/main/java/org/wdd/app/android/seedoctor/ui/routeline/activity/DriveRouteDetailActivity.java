package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.content.Context;
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
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.DriveRouteDetailAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

public class DriveRouteDetailActivity extends BaseActivity {

    public static void show(Context context, DrivePath drivePath, DriveRouteResult driveRouteResult) {
        Intent intent = new Intent(context, DriveRouteDetailActivity.class);
        intent.putExtra("drive_path", drivePath);
        intent.putExtra("drive_result", driveRouteResult);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private DrivePath drivePath;
    private DriveRouteResult driveRouteResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_route_detail);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        drivePath = intent.getParcelableExtra("drive_path");
        driveRouteResult = intent.getParcelableExtra("drive_result");
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
        recyclerView.setHasFixedSize(true);

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
