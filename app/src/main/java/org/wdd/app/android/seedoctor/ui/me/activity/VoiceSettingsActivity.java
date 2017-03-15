package org.wdd.app.android.seedoctor.ui.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.me.adapter.VoiceSettingsAdapter;
import org.wdd.app.android.seedoctor.ui.me.model.SettingModel;
import org.wdd.app.android.seedoctor.utils.DensityUtils;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;

import java.util.ArrayList;
import java.util.List;

public class VoiceSettingsActivity extends BaseActivity {

    public static void show(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, VoiceSettingsActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    private RecyclerView recyclerView;

    private VoiceSettingsAdapter settingsAdapter;
    private AppConfManager confManager;
    private List<SettingModel> settings;
    private String initVoiceSetting;
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_settings);
        initData();
        initTitle();
        initView();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(this);

        settings = new ArrayList<>();
        String[] entries = getResources().getStringArray(R.array.voicer_cloud_entries);
        String[] values = getResources().getStringArray(R.array.voicer_cloud_values);
        initVoiceSetting = confManager.getNavigationVoiceSetting();
        if (TextUtils.isEmpty(initVoiceSetting)) {
            initVoiceSetting = values[0];
        }
        SettingModel setting;
        for (int i = 0; i < entries.length; i++) {
            setting = new SettingModel();
            setting.name = entries[i];
            setting.value = values[i];
            setting.checked = setting.value.equals(initVoiceSetting) ? true : false;
            settings.add(setting);
        }
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_voice_settings_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                finish();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_voice_settings_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));

        settingsAdapter = new VoiceSettingsAdapter(this, settings);
        settingsAdapter.setSettingSelectedListener(new VoiceSettingsAdapter.SettingSelectedListener() {
            @Override
            public void onSettingSelected(SettingModel setting) {
                if (initVoiceSetting.equals(setting.value)) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
                confManager.saveNavigationVoiceSetting(setting.value);
            }
        });
        recyclerView.setAdapter(settingsAdapter);
        settingsAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }

    @Override
    public void onBackPressed() {
        backAction();
        super.onBackPressed();
    }

    private void backAction() {
        if (isChanged) {
            setResult(RESULT_OK);
        }
    }
}
