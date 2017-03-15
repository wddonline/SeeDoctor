package org.wdd.app.android.seedoctor.ui.routeline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.routeline.adapter.WalkRouteDetailAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdd on 16-12-3.
 */

public class WalkRouteDetailActivity extends BaseActivity {

    public static void show(Activity activity, WalkPath walkPath, WalkRouteResult result) {
        Intent intent = new Intent(activity, WalkRouteDetailActivity.class);
        SDApplication.getInstance().putTempData("walk_path", walkPath);
        SDApplication.getInstance().putTempData("walk_result", result);
        activity.startActivity(intent);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private WalkPath walkPath;
    private WalkRouteResult walkRouteResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_route_detail);
        initData();
        initTitle();
        initViews();
    }

    private void initData() {
        walkPath = (WalkPath) SDApplication.getInstance().getTempData("walk_path");
        walkRouteResult = (WalkRouteResult) SDApplication.getInstance().getTempData("walk_result");
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_walk_route_detail_toolbar);
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
        recyclerView = (RecyclerView) findViewById(R.id.activity_walk_route_detail_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        String dur = AMapUtil.getFriendlyTime((int) walkPath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) walkPath.getDistance());
        TextView timeDistanceView = (TextView) findViewById(R.id.activity_walk_route_detail_time_distance);
        timeDistanceView.setText(dur + "(" + dis + ")");

        List<WalkStep> walkStepList = new ArrayList<>();
        walkStepList.add(new WalkStep());
        walkStepList.addAll(walkPath.getSteps());
        walkStepList.add(new WalkStep());
        WalkRouteDetailAdapter adapter = new WalkRouteDetailAdapter(this, walkStepList);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }
}
