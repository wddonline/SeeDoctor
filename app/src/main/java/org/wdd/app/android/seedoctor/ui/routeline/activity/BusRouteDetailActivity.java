package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.BusRouteDetailAdapter;
import org.wdd.app.android.seedoctor.ui.routeline.model.SchemeBusStep;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;

public class BusRouteDetailActivity extends BaseActivity {

    public static void show(Context context, BusPath path, BusRouteResult result) {
        Intent intent = new Intent(context, BusRouteDetailActivity.class);
        intent.putExtra("bus_path", path);
        intent.putExtra("bus_result", result);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private BusPath busPath;
    private BusRouteResult busRouteResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_detail);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        busPath = intent.getParcelableExtra("bus_path");
        busRouteResult = intent.getParcelableExtra("bus_result");
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_bus_route_detail_toolbar);
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
        recyclerView = (RecyclerView) findViewById(R.id.activity_bus_route_detail_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String dur = AMapUtil.getFriendlyTime((int) busPath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) busPath.getDistance());

        TextView timeDistanceView = (TextView) findViewById(R.id.activity_bus_route_detail_time_distance);
        timeDistanceView.setText(dur + "(" + dis + ")");

        int taxCost = (int) busRouteResult.getTaxiCost();
        TextView taxCostView = (TextView) findViewById(R.id.activity_bus_route_detail_tax_cost);
        taxCostView.setText(String.format(getString(R.string.tax_cost), taxCost));

        List<SchemeBusStep> busStepList = new ArrayList<>();
        List<BusStep> data = busPath.getSteps();
        SchemeBusStep start = new SchemeBusStep(null);
        start.setStart(true);
        busStepList.add(start);
        for (BusStep busStep : data) {
            if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 0) {
                SchemeBusStep walk = new SchemeBusStep(busStep);
                walk.setWalk(true);
                busStepList.add(walk);
            }
            if (busStep.getBusLine() != null) {
                SchemeBusStep bus = new SchemeBusStep(busStep);
                bus.setBus(true);
                busStepList.add(bus);
            }
            if (busStep.getRailway() != null) {
                SchemeBusStep railway = new SchemeBusStep(busStep);
                railway.setRailway(true);
                busStepList.add(railway);
            }

            if (busStep.getTaxi() != null) {
                SchemeBusStep taxi = new SchemeBusStep(busStep);
                taxi.setTaxi(true);
                busStepList.add(taxi);
            }
        }
        SchemeBusStep end = new SchemeBusStep(null);
        end.setEnd(true);
        busStepList.add(end);
        BusRouteDetailAdapter adapter = new BusRouteDetailAdapter(getBaseContext(), busStepList);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }

}
