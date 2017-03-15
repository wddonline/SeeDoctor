package org.wdd.app.android.seedoctor.ui.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

public class FavoritesActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, FavoritesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initData();
        initTitiles();
        initViews();
    }

    private void initData() {

    }

    private void initTitiles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_favorites_toolbar);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
    }

    public void onDiseaseClicked(View v) {
        FavoritesDiseaseActivity.show(this);
    }

    public void onDrugClicked(View v) {
        FavoritesDrugActivity.show(this);
    }

    public void onEmergencyClicked(View v) {
        FavoritesEmergencyActivity.show(this);
    }

    public void onDepartmentClicked(View v) {
        FavoritesDepartmentActivity.show(this);
    }

    public void onDoctorClicked(View v) {
        FavoritesDoctorActivity.show(this);
    }

    public void onHospitalClicked(View v) {
        FavoritesHospitalActivity.show(this);
    }
}
