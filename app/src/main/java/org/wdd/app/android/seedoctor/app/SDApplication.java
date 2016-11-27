package org.wdd.app.android.seedoctor.app;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by wangdd on 16-11-27.
 */

public class SDApplication extends Application {

    public static SDApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        //设置umeng统计场景
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
