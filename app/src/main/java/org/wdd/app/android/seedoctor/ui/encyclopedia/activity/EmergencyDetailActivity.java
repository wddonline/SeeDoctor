package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.builder.NativeAdsBuilder;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.EmergencyDetailPresenter;
import org.wdd.app.android.seedoctor.utils.Constants;
import org.wdd.app.android.seedoctor.views.NetworkImageView;
import org.wdd.app.android.seedoctor.views.LoadView;

public class EmergencyDetailActivity extends BaseActivity {

    public static void show(Context activity, String emeid, String eme) {
        Intent intent = new Intent(activity, EmergencyDetailActivity.class);
        intent.putExtra("emeid", emeid);
        intent.putExtra("eme", eme);
        activity.startActivity(intent);
    }

    public static void showForResult(Activity activity, String emeid, String eme, int requsetCode) {
        Intent intent = new Intent(activity, EmergencyDetailActivity.class);
        intent.putExtra("emeid", emeid);
        intent.putExtra("eme", eme);
        activity.startActivityForResult(intent, requsetCode);
    }

    private LoadView loadView;
    private Toolbar toolbar;

    private String emeid;
    private String eme;
    private boolean initCollectStatus = false;
    private boolean currentCollectStatus = initCollectStatus;

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
        toolbar = (Toolbar) findViewById(R.id.activity_emergency_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleView = (TextView) findViewById(R.id.activity_emergency_detail_title);
        titleView.setText(eme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_collection_do:
                        presenter.collectEmergency(emeid, eme);
                        return true;
                    case R.id.menu_collection_undo:
                        presenter.uncollectEmergency(emeid);
                        return true;
                }
                return false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        presenter.getCollectionStatus(emeid);
        return true;
    }

    private void backAction() {
        if (currentCollectStatus != initCollectStatus) {
            Intent intent = new Intent();
            intent.putExtra("emeid", emeid);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public void onBackPressed() {
        backAction();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void showEmergencyDetailViews(Emergency data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_emergency_detail_dataview).setVisibility(View.VISIBLE);
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.activity_emergency_detail_img);
        imageView.setImageUrl(data.picurl);

        if (data.more != null && data.more.size() > 0) {
            TextView titleView = (TextView) findViewById(R.id.activity_emergency_detail_head);
            TextView descView = (TextView) findViewById(R.id.activity_emergency_detail_desc);
            titleView.setText(data.more.get(0).title);
            descView.setText(data.more.get(0).content.trim());
        }

        if (SDApplication.getInstance().isAdsOpen()) {
            new NativeAdsBuilder(this, (ViewGroup) findViewById(R.id.activity_emergency_detail_container), Constants.WIKI_EMERGENCY_DETAIL_AD_ID);
        }
    }

    public void showDataGettedFailureViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void setEmergencyCollectionViews(boolean isCollected) {
        initCollectStatus = isCollected;
        currentCollectStatus = isCollected;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(!isCollected);
        menu.findItem(R.id.menu_collection_undo).setVisible(isCollected);
    }

    public void updateEmergencyCollectedStatus(boolean success) {
        currentCollectStatus = true;
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(false);
        menu.findItem(R.id.menu_collection_undo).setVisible(true);
    }

    public void updateEmergencyUncollectedStatus(boolean success) {
        currentCollectStatus = false;
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(true);
        menu.findItem(R.id.menu_collection_undo).setVisible(false);
    }
}
