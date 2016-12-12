package org.wdd.app.android.seedoctor.ui.main.activity;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.drugstore.fragment.NearbyDrugstoreFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.WikiFragment;
import org.wdd.app.android.seedoctor.ui.hospital.fragment.NearbyHospitalFragment;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;

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

        View tabLayout;
        ImageView tabImgView;
        TextView tabTxtView;

        int[] tabIcons = {R.drawable.icon_hospital, R.drawable.icon_drugstore, R.drawable.icon_wiki,
                R.drawable.icon_me};
        int[] tabTxts = {R.string.hospital, R.string.drug_store, R.string.wiki, R.string.me};
        String[] tabTags = {"hospital", "drugstore", "wiki", "me"};
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
}
