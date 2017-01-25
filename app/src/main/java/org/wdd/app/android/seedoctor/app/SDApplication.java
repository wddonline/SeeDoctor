package org.wdd.app.android.seedoctor.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.location.LocationFinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdd on 16-11-27.
 */

public class SDApplication extends Application {

    private static SDApplication INSTANCE;

    public static SDApplication getInstance() {
        return INSTANCE;
    }

    private Handler uiHandler;
    private Map<String, Object> tempZone;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        tempZone = new HashMap<>();

        uiHandler = new Handler(Looper.getMainLooper());
        //设置umeng统计场景
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void putTempData(String key, Object data) {
        tempZone.put(key, data);
    }

    public Object getTempData(String key) {
        Object data = tempZone.get(key);
        tempZone.remove(key);
        return data;
    }

    public void exitApp() {
        LocationFinder.getInstance(this).stop();
        ActivityTaskStack.getInstance().clearActivities();
        HttpManager.getInstance(this).stopAllSession();
        Process.killProcess(Process.myPid());
    }
}
