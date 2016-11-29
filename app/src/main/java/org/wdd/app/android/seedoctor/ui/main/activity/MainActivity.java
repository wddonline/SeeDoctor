package org.wdd.app.android.seedoctor.ui.main.activity;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.umeng.update.UmengUpdateAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.doctor.fragment.DoctorFragment;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        UmengUpdateAgent.update(this);//Umeng更新检测
    }

    private void initViews() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null);

        TabHost.TabSpec hospitalTab = tabHost.newTabSpec("Hostial").setIndicator("医院");
        tabHost.addTab(hospitalTab, NearbyHospitalFragment.class, null);

        TabHost.TabSpec drugTab = tabHost.newTabSpec("Drug").setIndicator("药品");
        tabHost.addTab(drugTab, DoctorFragment.class, null);
    }
}
