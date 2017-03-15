package org.wdd.app.android.seedoctor.ui.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.web.activity.WebActivity;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

public class AboutAppActivity extends BaseActivity {

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {

    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_app_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        getSupportActionBar().setTitle("");
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        TextView versionView = (TextView) findViewById(R.id.activity_about_version_name);
        versionView.setText("V " + AppUtils.getVersionName(this));
    }

    public void onDisclaimerClicked(View view) {
        WebActivity.show(this, "file:///android_asset/html/disclaimer.htm", getString(R.string.disclaimer));
    }

    public void onContactClicked(View view) {
        Uri uri = Uri.parse("mailto:wddonline@163.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, ""); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
        startActivity(Intent.createChooser(intent, getString(R.string.mail_client_choose)));
    }
}
