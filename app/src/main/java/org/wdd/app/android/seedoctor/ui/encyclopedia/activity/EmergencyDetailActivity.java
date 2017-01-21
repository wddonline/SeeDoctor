package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.EmergencyDetailPresenter;
import org.wdd.app.android.seedoctor.views.HttpImageView;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

public class EmergencyDetailActivity extends BaseActivity {

    public static void show(Context context, String emeid, String eme) {
        Intent intent = new Intent(context, EmergencyDetailActivity.class);
        intent.putExtra("emeid", emeid);
        intent.putExtra("eme", eme);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private LoadView loadView;

    private String emeid;
    private String eme;

    private EmergencyDetailPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        emeid = getIntent().getStringExtra("emeid");
        eme = getIntent().getStringExtra("eme");

        presenter = new EmergencyDetailPresenter(host, this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_emergency_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleView = (TextView) findViewById(R.id.activity_emergency_detail_title);
        titleView.setText(eme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        loadView = (LoadView) findViewById(R.id.activity_emergency_detail_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getEmergencyDetailDataData(emeid);
            }
        });
        presenter.getEmergencyDetailDataData(emeid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showEmergencyDetailViews(Emergency data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_emergency_detail_dataview).setVisibility(View.VISIBLE);
        HttpImageView imageView = (HttpImageView) findViewById(R.id.activity_emergency_detail_img);
        imageView.setImageUrl(data.picurl);

        if (data.more != null && data.more.size() > 0) {
            TextView titleView = (TextView) findViewById(R.id.activity_emergency_detail_head);
            TextView descView = (TextView) findViewById(R.id.activity_emergency_detail_desc);
            titleView.setText(data.more.get(0).title);
            descView.setText(data.more.get(0).content.trim());
        }
    }

    public void showDataGettedFailureViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
