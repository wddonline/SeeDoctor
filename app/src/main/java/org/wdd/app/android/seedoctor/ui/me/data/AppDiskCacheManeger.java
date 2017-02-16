package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import com.android.volley.Cache;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.cache.ImageCache;
import org.wdd.app.android.seedoctor.http.impl.VolleyTool;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.utils.NumberUtils;

/**
 * Created by richard on 1/20/17.
 */

public class AppDiskCacheManeger {

    private Context context;
    private ActivityFragmentAvaliable host;
    private AppDiskCacheCallback cacheCallback;
    private DiskCacheCleanAction queryAction;

    public AppDiskCacheManeger(ActivityFragmentAvaliable host, Context context, AppDiskCacheCallback cacheCallback) {
        this.host = host;
        this.context = context;
        this.cacheCallback = cacheCallback;
    }

    public void cleanDiskCache() {
        queryAction = new DiskCacheCleanAction();
        Thread thread = new Thread(queryAction);
        thread.setDaemon(true);
        thread.start();
    }

    private class DiskCacheCleanAction implements Runnable {

        @Override
        public void run() {
            Cache cache = VolleyTool.getInstance(context).getRequestQueue().getCache();
            final String result = NumberUtils.formatFloatString(NumberUtils.b2mb(cache.getCacheSize()));
            cache.clear();
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    cacheCallback.onDiskCacheCleaned(result);
                }
            });
            queryAction = null;
        }
    }

    public interface AppDiskCacheCallback {

        void onDiskCacheCleaned(String result);

    }
}
