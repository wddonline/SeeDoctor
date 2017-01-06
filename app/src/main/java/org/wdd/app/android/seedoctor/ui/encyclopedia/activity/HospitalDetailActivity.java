package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalDetailPresenter;

public class HospitalDetailActivity extends BaseActivity {

    public static void show(Context context, String hospitalid, String hospitalname) {
        Intent intent = new Intent(context, HospitalDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("hospitalid", hospitalid);
        intent.putExtra("hospitalname", hospitalname);
        context.startActivity(intent);
    }

    private HospitalDetailPresenter presenter;
    private String hospitalid;
    private String hospitalname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        hospitalid = getIntent().getStringExtra("hospitalid");
        hospitalname = getIntent().getStringExtra("hospitalname");
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_hospital_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleView = (TextView) findViewById(R.id.activity_department_detail_title);
        titleView.setText(hospitalname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {

        presenter.getHospitalDetailData(hospitalid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showHospitalDetalViews(HospitalDetail data) {

    }

    public void showRequestErrorViews(String errorMsg) {

    }

    public void showNetworkErrorViews() {

    }
}
