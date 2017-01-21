package org.wdd.app.android.seedoctor.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.location.LocationFinder;

/**
 * Created by wangdd on 16-11-27.
 */

public class SDApplication extends Application {

    private static SDApplication INSTANCE;

    public static SDApplication getInstance() {
        return INSTANCE;
    }

    private Handler uiHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        uiHandler = new Handler(Looper.getMainLooper());
        //设置umeng统计场景
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void exitApp() {
        LocationFinder.getInstance(this).stop();
        ActivityTaskStack.getInstance().clearActivities();
        HttpManager.getInstance(this).stopAllSession();
        Process.killProcess(Process.myPid());
    }
}
