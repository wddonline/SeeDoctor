package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.cache.ImageCache;
import org.wdd.app.android.seedoctor.utils.NumberUtils;

/**
 * Created by richard on 1/20/17.
 */

public class AppDiskCacheManeger {

    private Context context;
    private AppDiskCacheCallback cacheCallback;
    private DiskCacheQueryAction queryAction;

    public AppDiskCacheManeger(Context context, AppDiskCacheCallback cacheCallback) {
        this.context = context;
        this.cacheCallback = cacheCallback;
    }

    public void getDiskCache() {
        queryAction = new DiskCacheQueryAction();
        Thread thread = new Thread(queryAction);
        thread.setDaemon(true);
        thread.start();
    }

    private class DiskCacheQueryAction implements Runnable {

        @Override
        public void run() {
            String result = NumberUtils.formatFloatString(NumberUtils.b2mb(ImageCache.instance(context).getDiskCacheSize()));
            cacheCallback.onDiskCacheGetted(result);
            queryAction = null;
        }
    }

    public interface AppDiskCacheCallback {

        void onDiskCacheGetted(String result);

    }
}
