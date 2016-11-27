package org.wdd.app.android.seedoctor.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.umeng.update.UmengUpdateAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.doctor.fragment.DoctorFragment;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.HospitalFragment;
import org.wdd.app.android.seedoctor.ui.self_check.fragment.SelfCheckFragment;

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
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabhost);
        tabHost.getTabWidget().setDividerDrawable(null);

//        TabHost.TabSpec doctorTab = tabHost.newTabSpec("Doctor").setIndicator("医生");
//        tabHost.addTab(doctorTab, DoctorFragment.class, null);

        TabHost.TabSpec hospitalTab = tabHost.newTabSpec("Hostial").setIndicator("医院");
        tabHost.addTab(hospitalTab, HospitalFragment.class, null);

        TabHost.TabSpec drugTab = tabHost.newTabSpec("Drug").setIndicator("药品");
        tabHost.addTab(drugTab, DoctorFragment.class, null);

//        TabHost.TabSpec selfCheckTab = tabHost.newTabSpec("SelfCheck").setIndicator("自查");
//        tabHost.addTab(selfCheckTab, SelfCheckFragment.class, null);
    }
}
