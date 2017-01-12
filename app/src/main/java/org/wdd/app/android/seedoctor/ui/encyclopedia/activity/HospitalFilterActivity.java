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
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalFilterPresenter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.view.WheelPickerMenu;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class HospitalFilterActivity extends BaseActivity {

    public static void show(Activity activity, int code) {
        Intent intent = new Intent(activity, HospitalFilterActivity.class);
        activity.startActivityForResult(intent, code);
    }

    private View dataView;
    private LoadView loadView;
    private WheelPickerMenu provinceMenu;
    private WheelPickerMenu levelMenu;
    private TextView provinceView;
    private TextView levelView;

    private HospitalFilterPresenter presenter;
    private AppConfManager confManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_filter);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        confManager = new AppConfManager(this);
        presenter = new HospitalFilterPresenter(this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_hospital_filter_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        dataView = findViewById(R.id.activity_hospital_filter_dataview);
        provinceView = (TextView) findViewById(R.id.activity_hospital_filter_area);
        levelView = (TextView) findViewById(R.id.activity_hospital_filter_level);

        loadView = (LoadView) findViewById(R.id.activity_hospital_filter_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getProvinceData();
            }
        });

        presenter.getProvinceData();
    }

    public void onProvinceClicked(View v) {
        provinceMenu.show();
    }

    public void onHospitalLevelClicked(View v) {
        levelMenu.show();
    }

    public void showProvinceViews(final List<Province> data) {
        dataView.setVisibility(View.VISIBLE);
        loadView.setStatus(LoadView.LoadStatus.Normal);
        provinceMenu = new WheelPickerMenu(this);
        String provinceid = confManager.getWikiHospitalProvinceId();
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
                    confManager.saveWikiHospitalProvinceId(null);
                } else {
                    Province province = data.get(position - 1);
                    provinceView.setText(province.name);
                    provinceView.setTag(province.provinceid);
                    confManager.saveWikiHospitalProvinceId(province.provinceid);
                }

            }
        });

        levelMenu = new WheelPickerMenu(this);
        final String[] arr = getResources().getStringArray(R.array.filter_hospital_level);
        List<String> levels = new ArrayList<>();
        for (String l : arr) {
            levels.add(l);
        }
        initPosition = 0;
        String levelid = confManager.getWikiHospitalLevelId();
        if (!TextUtils.isEmpty(levelid)) {
            initPosition = Integer.parseInt(levelid);
        }
        levelMenu.setData(levels);
        levelMenu.setInitPosition(initPosition);
        levelView.setText(arr[initPosition]);
        levelMenu.setProvinceSelectedListener(new WheelPickerMenu.ProvinceSelectedListener() {

            @Override
            public void onProvinceSelected(int position) {
                if (position == 0) {
                    levelView.setText("不限");
                    levelView.setTag(null);
                    confManager.saveWikiHospitalLevelId(null);
                } else {
                    levelView.setText(arr[position]);
                    levelView.setTag(position + "");
                    confManager.saveWikiHospitalLevelId(position + "");
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
