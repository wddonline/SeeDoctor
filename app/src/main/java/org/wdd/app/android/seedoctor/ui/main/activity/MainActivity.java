package org.wdd.app.android.seedoctor.ui.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.drugstore.fragment.NearbyDrugstoreFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.WikiFragment;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.BmobUtils;
import org.wdd.app.android.seedoctor.views.SDFragmentTabHost;

public class MainActivity extends BaseActivity implements Runnable {

    private final long TIME_LIMIT = 3000;

    private SDFragmentTabHost tabHost;
    private Handler handler = new Handler();

    private int backPressedCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion();
        initViews();
    }

    private void checkVersion() {
        BmobUtils.checkAppVersion(this);
    }

    private void initViews() {
        tabHost = (SDFragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null);

        View tabLayout;
        ImageView tabImgView;
        TextView tabTxtView;

        int[] tabIcons = {R.drawable.icon_hospital, R.drawable.icon_drugstore, R.drawable.icon_wiki,
                R.drawable.icon_me};
        int[] tabTxts = {R.string.hospital, R.string.drug_store, R.string.wiki, R.string.me};
        String[] tabTags = {"doctor", "drugstore", "wiki", "me"};
        Class[] tabClasses = {NearbyHospitalFragment.class, NearbyDrugstoreFragment.class,
                WikiFragment.class, MeFragment.class};

        int tabCount = tabIcons.length;

        for (int i = 0; i < tabCount; i++) {
            tabLayout = getLayoutInflater().inflate(R.layout.layout_main_tab, null, false);
            tabImgView = (ImageView) tabLayout.findViewById(R.id.layout_main_tab_img);
            tabTxtView = (TextView) tabLayout.findViewById(R.id.layout_main_tab_txt);
            tabTxtView.setText(tabTxts[i]);
            tabImgView.setImageResource(tabIcons[i]);
            tabHost.addTab(tabHost.newTabSpec(tabTags[i]).setIndicator(tabLayout), tabClasses[i], null);
        }

        int tabWidth = getResources().getDisplayMetrics().widthPixels / 4;
        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setBackgroundResource(R.drawable.navigation_background);
        for (int i = 0; i < tabCount; i++) {
            tabWidget.getChildTabViewAt(i).getLayoutParams().width = tabWidth;
        }
    }

    @Override
    public void run() {
        backPressedCount = 0;
    }

    @Override
    public void onBackPressed() {
        if (backPressedCount < 1) {
            handler.postDelayed(this, TIME_LIMIT);
            AppToaster.show(R.string.back_to_exit);
            backPressedCount++;
        } else {
            handler.removeCallbacks(this);
            SDApplication.getInstance().exitApp();
        }
    }
}
