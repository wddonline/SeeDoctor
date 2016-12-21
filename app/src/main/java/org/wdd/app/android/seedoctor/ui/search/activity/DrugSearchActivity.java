package org.wdd.app.android.seedoctor.ui.search.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import org.wdd.app.android.seedoctor.R;

public class DrugSearchActivity extends Activity {

    private static final String SHARED_NAME = "search_view";

    public static void show(Activity activity, View sharedView) {
        Intent intent = new Intent(activity, DrugSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ViewCompat.setTransitionName(sharedView, SHARED_NAME);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, SHARED_NAME).toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_search);
    }
}
