package org.wdd.app.android.seedoctor.ui.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

public class AboutDeveloperActivity extends BaseActivity {

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, AboutDeveloperActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        initTitles();
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_developer_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        getSupportActionBar().setTitle("");
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
