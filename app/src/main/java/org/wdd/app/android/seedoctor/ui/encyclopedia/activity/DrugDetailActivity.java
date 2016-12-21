package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.wdd.app.android.seedoctor.R;

public class DrugDetailActivity extends Activity {

    public static void show(Context context, int drugid) {
        Intent intent = new Intent(context, DrugDetailActivity.class);
        intent.putExtra("", drugid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail);
    }


}
