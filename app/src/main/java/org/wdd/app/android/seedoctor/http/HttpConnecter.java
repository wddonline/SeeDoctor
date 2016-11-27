package org.wdd.app.android.seedoctor.http;

/**
 * Created by wangdd on 16-11-26.
 */

public interface HttpConnecter {

    HttpSession sendHttpRequest(HttpRequestEntry entry, Class clazz, HttpConnectCallback callback);

    void stopAllSession();

    void stopSessionByTag(String tag);
}
