package org.wdd.app.android.seedoctor.ui.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;

public class FavoritesActivity extends BaseActivity {

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, FavoritesActivity.class);
        activity.startActivity(intent);
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

    public void onNewsClicked(View v) {
        FavoritesNewsActivity.show(this);
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
