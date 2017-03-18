package org.wdd.app.android.seedoctor.http;

import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

/**
 * Created by wangdd on 16-11-26.
 */

public interface HttpConnecter {

    HttpSession sendHttpRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, Class clazz, HttpConnectCallback callback);

    HttpSession sendHttpRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, HttpConnectCallback callback);

    void stopAllSession();

    void stopSessionByTag(String tag);
}
