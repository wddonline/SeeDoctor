package org.wdd.app.android.seedoctor.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;

/**
 * Created by richard on 11/30/16.
 */

public class RouteLineActivity extends BaseActivity {

    public static void show(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context, RouteLineActivity.class);
        intent.putExtra("lat", latitude);
        intent.putExtra("lon", longitude);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_root_line);
    }
}
