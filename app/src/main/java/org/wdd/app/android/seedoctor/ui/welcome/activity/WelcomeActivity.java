package org.wdd.app.android.seedoctor.ui.welcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.main.activity.MainActivity;
import org.wdd.app.android.seedoctor.ui.welcome.presenter.WelcomePresenter;

public class WelcomeActivity extends BaseActivity implements Runnable {

    private Handler handler = new Handler();
    private WelcomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initData();
    }

    private void initData() {
        presenter = new WelcomePresenter(this);
        presenter.findLocation();
    }

    @Override
    public void run() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void jumpToNextActivity(boolean immediately) {
        if (immediately) {
            run();
        } else {
            handler.postDelayed(this, 3000);
        }
    }
}
