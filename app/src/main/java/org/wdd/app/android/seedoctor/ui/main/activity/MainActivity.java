package org.wdd.app.android.seedoctor.ui.main.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.WikiFragment;
import org.wdd.app.android.seedoctor.ui.me.fragment.MeFragment;
import org.wdd.app.android.seedoctor.ui.nearby.fragment.NearbyFragment;
import org.wdd.app.android.seedoctor.ui.news.fragment.NewsHomeFragment;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.BmobUtils;
import org.wdd.app.android.seedoctor.views.XFragmentTabHost;

public class MainActivity extends BaseActivity implements Runnable {

    private final long TIME_LIMIT = 3000;

    private XFragmentTabHost tabHost;
    private Handler handler = new Handler();

    private int backPressedCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        MobclickAgent.openActivityDurationTrack(false);
        checkVersion();
        initViews();
    }

    private void checkVersion() {
        BmobUtils.autoUpdateApp(this);
    }

    private void initViews() {
        tabHost = (XFragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.getTabWidget().setDividerDrawable(null);

        View tabLayout;
        ImageView tabImgView;
        TextView tabTxtView;

        int[] tabIcons = {R.drawable.icon_nearby, R.drawable.icon_news, R.drawable.icon_wiki, R.drawable.icon_me};
        int[] tabTxts = {R.string.nearby, R.string.news, R.string.wiki, R.string.me};
        String[] tabTags = {"nearby", "news", "wiki", "me"};
        Class[] tabClasses = {NearbyFragment.class, NewsHomeFragment.class, WikiFragment.class, MeFragment.class};

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
