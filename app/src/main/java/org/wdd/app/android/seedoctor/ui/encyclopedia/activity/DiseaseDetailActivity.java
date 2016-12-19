package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;

public class DiseaseDetailActivity extends Activity {

    public static void show(Context context, Disease item) {
        Intent intent = new Intent(context, DiseaseDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
    }
}
