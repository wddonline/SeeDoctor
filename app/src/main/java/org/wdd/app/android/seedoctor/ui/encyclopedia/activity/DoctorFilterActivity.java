package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DoctorFilterPresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.view.WheelPickerMenu;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class DoctorFilterActivity extends BaseActivity {

    public static void show(Activity activity, int code) {
        Intent intent = new Intent(activity, DoctorFilterActivity.class);
        activity.startActivityForResult(intent, code);
    }

    private View dataView;
    private LoadView loadView;
    private WheelPickerMenu provinceMenu;
    private WheelPickerMenu hospitalLevelMenu;
    private WheelPickerMenu doctorLevelMenu;
    private TextView provinceView;
    private TextView hospitalLevelView;
    private TextView doctorLevelView;

    private DoctorFilterPresenter presenter;
    private AppConfManager confManager;

    private String initProvinceid;
    private String initHospitalLevelid;
    private String initDoctorLevelid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_filter);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(this);
        initProvinceid = confManager.getWikiDoctorProvinceId();
        initHospitalLevelid = confManager.getWikiDoctorLevelId();
        initDoctorLevelid = confManager.getWikiDoctorJobLevelId();
        presenter = new DoctorFilterPresenter(this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_doctor_filter_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beforeFinish();
                finish();
            }
        });
    }

    private void initViews() {
        dataView = findViewById(R.id.activity_doctor_filter_dataview);
        provinceView = (TextView) findViewById(R.id.activity_doctor_filter_area);
        hospitalLevelView = (TextView) findViewById(R.id.activity_doctor_filter_hospital_level);
        doctorLevelView = (TextView) findViewById(R.id.activity_doctor_filter_doctor_level);

        provinceView.setTag(confManager.getWikiDoctorProvinceId());
        hospitalLevelView.setTag(confManager.getWikiDoctorLevelId());
        doctorLevelView.setTag(confManager.getWikiDoctorJobLevelId());

        loadView = (LoadView) findViewById(R.id.activity_doctor_filter_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getProvinceData();
            }
        });

        presenter.getProvinceData();
    }

    @Override
    public void onBackPressed() {
        beforeFinish();
        super.onBackPressed();
    }

    private void beforeFinish() {
        String currentProvinceid = (String) provinceView.getTag();
        String currentHospitalLevelid = (String) hospitalLevelView.getTag();
        String currentDoctorLevelid = (String) doctorLevelView.getTag();
        if (!initProvinceid.equals(currentProvinceid) || !initHospitalLevelid.equals(currentHospitalLevelid)
                || !initDoctorLevelid.equals(currentDoctorLevelid)) {
            setResult(RESULT_OK);
        }
    }

    public void onProvinceClicked(View v) {
        provinceMenu.show();
    }

    public void onHospitalLevelClicked(View v) {
        hospitalLevelMenu.show();
    }

    public void onDoctorLevelClicked(View v) {
        doctorLevelMenu.show();
    }

    public void showProvinceViews(final List<Province> data) {
        dataView.setVisibility(View.VISIBLE);
        loadView.setStatus(LoadView.LoadStatus.Normal);
        provinceMenu = new WheelPickerMenu(this);
        String provinceid = confManager.getWikiDoctorProvinceId();
        int initPosition = 0;
        List<String> provinces = new ArrayList<>();
        provinces.add("不限");
        Province p;
        for (int i = 0; i < data.size(); i++) {
            p = data.get(i);
            provinces.add(p.name);
            if (p.provinceid.equals(provinceid)) {
                initPosition = i + 1;
            }
        }
        provinceMenu.setData(provinces);
        provinceMenu.setInitPosition(initPosition);
        provinceView.setText(provinces.get(initPosition));
        provinceMenu.setProvinceSelectedListener(new WheelPickerMenu.ProvinceSelectedListener() {

            @Override
            public void onProvinceSelected(int position) {
                if (position == 0) {
                    provinceView.setText("不限");
                    provinceView.setTag(null);
                    confManager.saveWikiDoctorProvinceId(null);
                    confManager.saveWikiDoctorProvinceName(null);
                } else {
                    Province province = data.get(position - 1);
                    provinceView.setText(province.name);
                    provinceView.setTag(province.provinceid);
                    confManager.saveWikiDoctorProvinceId(province.provinceid);
                    confManager.saveWikiDoctorProvinceName(province.name);
                }

            }
        });

        hospitalLevelMenu = new WheelPickerMenu(this);
        final String[] hospitalArr = getResources().getStringArray(R.array.filter_hospital_level);
        List<String> levels = new ArrayList<>();
        for (String l : hospitalArr) {
            levels.add(l);
        }
        initPosition = 0;
        String levelid = confManager.getWikiDoctorLevelId();
        if (!TextUtils.isEmpty(levelid)) {
            initPosition = Integer.parseInt(levelid) - 1;
        }
        hospitalLevelMenu.setData(levels);
        hospitalLevelMenu.setInitPosition(initPosition);
        hospitalLevelView.setText(hospitalArr[initPosition]);
        hospitalLevelMenu.setProvinceSelectedListener(new WheelPickerMenu.ProvinceSelectedListener() {

            @Override
            public void onProvinceSelected(int position) {
                if (position == 0) {
                    hospitalLevelView.setText("不限");
                    hospitalLevelView.setTag(null);
                    confManager.saveWikiDoctorLevelId(null);
                    confManager.saveWikiDoctorLevelName(null);
                } else {
                    hospitalLevelView.setText(hospitalArr[position]);
                    hospitalLevelView.setTag(position + "");
                    confManager.saveWikiDoctorLevelId((position + 1) + "");
                    confManager.saveWikiDoctorLevelName(position == 0 ? null : hospitalArr[position]);
                }

            }
        });

        doctorLevelMenu = new WheelPickerMenu(this);
        final String[] doctorArr = getResources().getStringArray(R.array.filter_doctor_level);
        List<String> doctorLevels = new ArrayList<>();
        for (String l : doctorArr) {
            doctorLevels.add(l);
        }
        initPosition = 0;
        String doctorLevelid = confManager.getWikiDoctorJobLevelId();
        if (!TextUtils.isEmpty(doctorLevelid)) {
            initPosition = Integer.parseInt(doctorLevelid);
        }
        doctorLevelMenu.setData(doctorLevels);
        doctorLevelMenu.setInitPosition(initPosition);
        doctorLevelView.setText(doctorArr[initPosition]);
        doctorLevelMenu.setProvinceSelectedListener(new WheelPickerMenu.ProvinceSelectedListener() {

            @Override
            public void onProvinceSelected(int position) {
                if (position == 0) {
                    doctorLevelView.setText("不限");
                    doctorLevelView.setTag(null);
                    confManager.saveWikiDoctorJobLevelId(null);
                    confManager.saveWikiDoctorJobLevelName(null);
                } else {
                    doctorLevelView.setText(doctorArr[position]);
                    doctorLevelView.setTag(position + "");
                    confManager.saveWikiDoctorJobLevelId(position + "");
                    confManager.saveWikiDoctorJobLevelName(doctorArr[position]);
                }

            }
        });
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
