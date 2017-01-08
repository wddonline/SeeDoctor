package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDepartmentFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDescFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDoctorFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalDetailPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class HospitalDetailActivity extends BaseActivity {

    public static void show(Context context, String hospitalid, String hospitalname) {
        Intent intent = new Intent(context, HospitalDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("hospitalid", hospitalid);
        intent.putExtra("hospitalname", hospitalname);
        context.startActivity(intent);
    }

    private ViewPager viewPager;
    private LoadView loadView;
    private View briefView;

    private HospitalAdapter adapter;
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
        presenter = new HospitalDetailPresenter(this);
        hospitalid = getIntent().getStringExtra("hospitalid");
        hospitalname = getIntent().getStringExtra("hospitalname");
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_hospital_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleView = (TextView) findViewById(R.id.activity_hospital_detail_title);
        titleView.setText(hospitalname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.activity_hospital_detail_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_hospital_detail_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        loadView = (LoadView) findViewById(R.id.activity_hospital_detail_loadview);
        briefView = findViewById(R.id.activity_hospital_detail_brief);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getHospitalDetailData(hospitalid);
            }
        });

        presenter.getHospitalDetailData(hospitalid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showHospitalDetalViews(HospitalDetail data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        viewPager.setVisibility(View.VISIBLE);
        briefView.setVisibility(View.VISIBLE);

        List<Fragment> fragments = new ArrayList<>();
        Bundle extras;

        HospitalDescFragment descFragment = new HospitalDescFragment();
        extras = new Bundle();
        extras.putString(HospitalDescFragment.KEY_DESC, data.introduction);
        descFragment.setArguments(extras);
        fragments.add(descFragment);

        HospitalDoctorFragment doctorFragment = new HospitalDoctorFragment();
        extras = new Bundle();
        extras.putString(HospitalDoctorFragment.KEY_ID, data.hospitalid);
        doctorFragment.setArguments(extras);
        fragments.add(doctorFragment);

        HospitalDepartmentFragment departmentFragment = new HospitalDepartmentFragment();
        extras = new Bundle();
        extras.putString(HospitalDepartmentFragment.KEY_ID, data.hospitalid);
        departmentFragment.setArguments(extras);
        fragments.add(departmentFragment);

        adapter = new HospitalAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    private class HospitalAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"医院简介", "医生", "科室"};

        private List<Fragment> fragments;

        public HospitalAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
