package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DiseaseDetailPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

public class DiseaseDetailActivity extends BaseActivity {

    public static void show(Context context, int diseaseid, String diseasename) {
        Intent intent = new Intent(context, DiseaseDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("diseaseid", diseaseid);
        intent.putExtra("diseasename", diseasename);
        context.startActivity(intent);
    }

    private LoadView loadView;

    private DiseaseDetailPresenter presenter;
    private int diseaseId;
    private String diseaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        initData();
        initTitle();
        initView();
    }

    private void initData() {
        diseaseId = getIntent().getIntExtra("diseaseid", 0);
        diseaseName = getIntent().getStringExtra("diseasename");

        presenter = new DiseaseDetailPresenter(this);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_disease_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titleView = (TextView) findViewById(R.id.activity_disease_detail_title);
        titleView.setText(diseaseName);
    }

    private void initView() {
        loadView = (LoadView) findViewById(R.id.activity_disease_detail_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDiseaseDetailData(diseaseId);
            }
        });

        presenter.getDiseaseDetailData(diseaseId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showDiseaseDetalViews(DiseaseDetail data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
    }
}
