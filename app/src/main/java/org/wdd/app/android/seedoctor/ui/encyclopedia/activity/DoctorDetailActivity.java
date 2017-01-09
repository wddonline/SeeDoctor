package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DoctorDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DoctorDetailPresenter;
import org.wdd.app.android.seedoctor.views.HttpImageView;
import org.wdd.app.android.seedoctor.views.LoadView;

public class DoctorDetailActivity extends BaseActivity {

    public static void show(Context context, String doctorid, String doctorname) {
        Intent intent = new Intent(context, DoctorDetailActivity.class);
        intent.putExtra("doctorid", doctorid);
        intent.putExtra("doctorname", doctorname);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private LoadView loadView;

    private DoctorDetailPresenter presenter;
    private String doctorid;
    private String doctorname;
    private DoctorDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        presenter = new DoctorDetailPresenter(this);
        doctorid = getIntent().getStringExtra("doctorid");
        doctorname = getIntent().getStringExtra("doctorname");
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_doctor_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleView = (TextView) findViewById(R.id.activity_doctor_detail_title);
        titleView.setText(doctorname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {

        loadView = (LoadView) findViewById(R.id.activity_doctor_detail_loadview);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDoctorDetailData(doctorid);
            }
        });

        presenter.getDoctorDetailData(doctorid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void onHospitalClicked(View v) {
        HospitalDetailActivity.show(this, detail.hospitalid, detail.hospitalname);
    }

    public void showHospitalDetalViews(DoctorDetail detail) {
        this.detail = detail;

        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_doctor_detail_hospital_data).setVisibility(View.VISIBLE);

        HttpImageView imageView = (HttpImageView) findViewById(R.id.activity_doctor_detail_image);
        imageView.setImageUrl(detail.photourl);
        TextView nameView = (TextView) findViewById(R.id.activity_doctor_detail_name);
        nameView.setText(detail.doctorname);
        TextView deptView = (TextView) findViewById(R.id.activity_doctor_detail_dept);
        deptView.setText(detail.departmentname);
        TextView levelView = (TextView) findViewById(R.id.activity_doctor_detail_level);
        levelView.setText(detail.doclevelname);
        TextView hospitalNameView = (TextView) findViewById(R.id.activity_doctor_detail_hospital_name);
        hospitalNameView.setText(detail.hospitalname);
        TextView majorView = (TextView) findViewById(R.id.activity_doctor_detail_major);
        majorView.setText(detail.feature);
        TextView briefView = (TextView) findViewById(R.id.activity_doctor_detail_short_brief);
        briefView.setText(detail.introduction);
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
