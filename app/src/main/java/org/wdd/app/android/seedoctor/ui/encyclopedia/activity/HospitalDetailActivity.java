package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDepartmentFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDescFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.HospitalDoctorFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalDetailPresenter;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.views.ActionSheet;
import org.wdd.app.android.seedoctor.views.LoadView;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

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

    public static void showForResult(Activity activity, int position, String hospitalid, String hospitalname, int requsetCode) {
        Intent intent = new Intent(activity, HospitalDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("hospitalid", hospitalid);
        intent.putExtra("hospitalname", hospitalname);
        activity.startActivityForResult(intent, requsetCode);
    }

    private ViewPager viewPager;
    private Toolbar toolbar;
    private LoadView loadView;
    private View briefView;
    private TabLayout tabLayout;
    private TextView phoneView;

    private HospitalAdapter adapter;
    private HospitalDetailPresenter presenter;
    private String hospitalid;
    private String hospitalname;
    private HospitalDetail detail;
    private List<String> phones;

    private int position;
    private boolean initCollectStatus = false;
    private boolean currentCollectStatus = initCollectStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        MobclickAgent.openActivityDurationTrack(false);

        presenter = new HospitalDetailPresenter(host, this);
        position = getIntent().getIntExtra("position" , -1);
        hospitalid = getIntent().getStringExtra("hospitalid");
        hospitalname = getIntent().getStringExtra("hospitalname");
    }

    private void initTitles() {
        toolbar = (Toolbar) findViewById(R.id.activity_hospital_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        final TextView titleView = (TextView) findViewById(R.id.activity_hospital_detail_title);
        titleView.setText(hospitalname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                finish();
            }
        });

        titleView.post(new Runnable() {
            @Override
            public void run() {
                titleView.setPadding(0, 0, Math.round((toolbar.getWidth() - titleView.getWidth()) * 0.5f), 0);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_collection_do:
                        showLoadingDialog();
                        presenter.collectHospital(hospitalid, hospitalname, detail.picurl);
                        return true;
                    case R.id.menu_collection_undo:
                        showLoadingDialog();
                        presenter.uncollectHospital(hospitalid);
                        return true;
                }
                return false;
            }
        });
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.activity_hospital_detail_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.activity_hospital_detail_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        loadView = (LoadView) findViewById(R.id.activity_hospital_detail_loadview);
        briefView = findViewById(R.id.activity_hospital_detail_brief);
        phoneView = (TextView) findViewById(R.id.activity_hospital_detail_phone);
        findViewById(R.id.activity_hospital_detail_phone_click).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppUtils.clipText(getBaseContext(), phoneView.getText().toString());
                AppToaster.show(R.string.hospital_phone_cliped);
                return true;
            }
        });

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getHospitalDetailData(hospitalid);
            }
        });

        presenter.getHospitalDetailData(hospitalid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    private void backAction() {
        if (currentCollectStatus != initCollectStatus) {
            Intent intent = new Intent();
            intent.putExtra("position", position);
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

    public void showHospitalDetalViews(HospitalDetail detail) {
        this.detail = detail;
        presenter.getCollectionStatus(hospitalid);

        loadView.setStatus(LoadView.LoadStatus.Normal);
        viewPager.setVisibility(View.VISIBLE);
        briefView.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.activity_hospital_detail_image);
        imageView.setImageUrl(detail.picurl);

        TextView nameView = (TextView) findViewById(R.id.activity_hospital_detail_name);
        nameView.setText(detail.hospitalname);

        TextView levelName = (TextView) findViewById(R.id.activity_hospital_detail_level);
        levelName.setText(detail.levelname);

        ViewGroup starView = (ViewGroup) findViewById(R.id.activity_hospital_detail_star);
        int star = Math.round(detail.score / 20f);
        for (int i = 0; i < star; i++) {
            starView.getChildAt(i).setVisibility(View.VISIBLE);
        }

        phoneView.setText(detail.phone);

        TextView addressView = (TextView) findViewById(R.id.activity_hospital_detail_addr);
        addressView.setText(detail.address);

        List<Fragment> fragments = new ArrayList<>();
        Bundle extras;

        HospitalDescFragment descFragment = new HospitalDescFragment();
        extras = new Bundle();
        extras.putString(HospitalDescFragment.KEY_DESC, detail.introduction);
        descFragment.setArguments(extras);
        fragments.add(descFragment);

        HospitalDoctorFragment doctorFragment = new HospitalDoctorFragment();
        extras = new Bundle();
        extras.putString(HospitalDoctorFragment.KEY_ID, detail.hospitalid);
        doctorFragment.setArguments(extras);
        fragments.add(doctorFragment);

        HospitalDepartmentFragment departmentFragment = new HospitalDepartmentFragment();
        extras = new Bundle();
        extras.putString(HospitalDepartmentFragment.KEY_ID, detail.hospitalid);
        departmentFragment.setArguments(extras);
        fragments.add(departmentFragment);

        adapter = new HospitalAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    public void onPhoneClicked(View v) {
        if (phones == null) {
            phones = extractPhones(detail.phone);
        }
        if (phones.size() == 0) return;
        ActionSheet.Builder builder = ActionSheet.createBuilder(this, getSupportFragmentManager());
        builder.setCancelableOnTouchOutside(true);
        builder.setCancelButtonTitle(R.string.cancel);
        builder.setOtherButtonTitles(phones);
        builder.setListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

            }

            @Override
            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phones.get(index)));
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void onAddressClicked(View v) {
        RouteLineActivity.show(this, detail.getLat(), detail.getLng());
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void setHospitalCollectionViews(boolean isCollected) {
        initCollectStatus = isCollected;
        currentCollectStatus = isCollected;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(!isCollected);
        menu.findItem(R.id.menu_collection_undo).setVisible(isCollected);
    }

    public void updateHospitalCollectedStatus(boolean success) {
        currentCollectStatus = true;
        hideLoadingDialog();
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(false);
        menu.findItem(R.id.menu_collection_undo).setVisible(true);
    }

    public void updateHospitalUncollectedStatus(boolean success) {
        currentCollectStatus = false;
        hideLoadingDialog();
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(true);
        menu.findItem(R.id.menu_collection_undo).setVisible(false);
    }

    /**
     * 从字符串中提取电话
     * @param text
     * @return
     */
    private List<String> extractPhones(String text) {
        List<String> result = new ArrayList<String>();
        char ch;
        text = text.trim();
        StringBuffer buff = null;
        for (int i = 0; i < text.length(); i++) {
            ch = text.charAt(i);
            if ((ch >= '0' && ch <= '9') || ch == '-') {
                if (buff == null) {
                    buff = new StringBuffer();
                }
                buff.append(ch);
                if (i == text.length() - 1) {
                    result.add(buff.toString());
                }
            } else {
                if (buff != null) {
                    result.add(buff.toString());
                    buff = null;
                }
            }
        }
        return result;
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
