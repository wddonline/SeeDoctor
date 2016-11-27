package org.wdd.app.android.seedoctor.http;

import org.wdd.app.android.seedoctor.http.error.HttpError;

/**
 * Created by wangdd on 16-11-26.
 */

public interface HttpConnectCallback {

    void onRequestOk(HttpResponseEntry res);

    void onRequestFailure(HttpError error);

    void onNetworkError();

}
