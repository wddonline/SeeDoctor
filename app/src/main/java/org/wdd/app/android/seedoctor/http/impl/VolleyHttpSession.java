package org.wdd.app.android.seedoctor.http.impl;

import com.android.volley.Request;

import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;

/**
 * Created by wangdd on 16-11-26.
 */

public class VolleyHttpSession extends HttpSession {

    private Request request;
    private HttpRequestEntry requestEntry;

    public VolleyHttpSession() {
    }

    public VolleyHttpSession(Request request, HttpRequestEntry requestEntry) {
        this.request = request;
        this.requestEntry = requestEntry;
    }

    public void setRequestEntry(HttpRequestEntry requestEntry) {
        this.requestEntry = requestEntry;
    }

    @Override
    public void cancelRequest() {
        request.cancel();
    }

    @Override
    public String getRequetUrl() {
        return requestEntry.getUrl();
    }

    @Override
    public HttpRequestEntry getRequestEntry() {
        return requestEntry;
    }
}
