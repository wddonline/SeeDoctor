package org.wdd.app.android.seedoctor.ui.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

public class NavigationSettingsActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, NavigationSettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private final int REQUEST_CODE = 1;

    private TextView voiceNameView;

    private AppConfManager confManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_settings);
        initData();
        initTitle();
        initViews();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(this);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_navigation_settings_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
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
        voiceNameView = (TextView) findViewById(R.id.activity_navigation_settings_value);
        voiceNameView.setText(getVoceNameFromConf());
    }

    private String getVoceNameFromConf() {
        String[] entries = getResources().getStringArray(R.array.voicer_cloud_entries);
        String[] values = getResources().getStringArray(R.array.voicer_cloud_values);
        String setting = confManager.getNavigationVoiceSetting();
        if (TextUtils.isEmpty(setting)) {
            return entries[0];
        } else {
            for (int i = 0; i < values.length; i++) {
                if (values[i].equals(setting)) {
                    return entries[i];
                }
            }
        }
        return entries[0];
    }

    public void onVoiceSettingClicked(View v) {
        VoiceSettingsActivity.show(this, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE:
                voiceNameView.setText(getVoceNameFromConf());
                break;
        }
    }
}
