package org.wdd.app.android.seedoctor.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.wdd.app.android.seedoctor.app.ActivityTaskStack;

/**
 * Created by wangdd on 16-11-26.
 */

public class BaseActivity extends AppCompatActivity {

    public ActivityFragmentAvaliable host = new ActivityFragmentAvaliable(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTaskStack.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskStack.getInstance().removeActivity(this);
    }
}
