package org.wdd.app.android.seedoctor.http.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpConnecter;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.HttpUtils;
import org.wdd.app.android.seedoctor.http.StatusCode;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangdd on 16-11-26.
 */

public class VolleyHttpConnecter implements HttpConnecter {

    private Context context;
    private List<HttpSession> sessionList;
    private RequestQueue requestQueue;

    public VolleyHttpConnecter(Context context) {
        this.context = context;
        sessionList = Collections.synchronizedList(new ArrayList<HttpSession>());

        requestQueue = VolleyTool.getInstance(context).getRequestQueue();
        requestQueue.start();
    }

    @Override
    public HttpSession sendHttpRequest(final HttpRequestEntry requestEntry, final Class clazz, final HttpConnectCallback callback) {
        if (!HttpUtils.isNetworkEnabled(context)) {
            callback.onNetworkError();
            return null;
        }
        int method = Request.Method.POST;
        if (requestEntry.getMethod() == HttpRequestEntry.Method.GET) {
            method = Request.Method.GET;
            requestEntry.setUrl(generateGetUrl(requestEntry.getUrl(), requestEntry.getRequestParams()));
        }
        StringRequest request = new StringRequest(method, requestEntry.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String txt) {
                Object data = JSON.parseObject(txt, clazz);
                HttpResponseEntry responseEntry = new HttpResponseEntry();
                responseEntry.setStatusCode(StatusCode.HTTP_OK);
                responseEntry.setData(data);
                callback.onRequestOk(responseEntry);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                HttpError error;
                if (err.networkResponse == null)
                    error= new HttpError(ErrorCode.CONNECT_ERROR, err.getMessage());
                else
                    error= new HttpError(err.networkResponse.statusCode, err.getMessage());
                callback.onRequestFailure(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestEntry.getRequestParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return requestEntry.getRequestHeaders();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(requestEntry.getTimeOut(),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        HttpSession session = new VolleyHttpSession(request, requestEntry);
        sessionList.add(session);
        return session;
    }

    private String generateGetUrl(String url, Map<String, String> params) {
        StringBuffer urlBuff = new StringBuffer(url);
        urlBuff.append("?");
        Set<String> keys = params.keySet();
        Iterator<String> it = keys.iterator();
        String key;
        String value;
        while (it.hasNext()) {
            key = it.next();
            value = params.get(key);
            urlBuff.append(key + "=" + value + "&");
        }
        return urlBuff.subSequence(0, urlBuff.length() - 1).toString();
    }

    @Override
    public void stopAllSession() {
        requestQueue.stop();
        for (HttpSession session : sessionList) {
            session.cancelRequest();
        }
        sessionList.clear();
    }

    @Override
    public void stopSessionByTag(String tag) {
        Iterator<HttpSession> it = sessionList.iterator();
        while (it.hasNext()) {
            HttpSession session = it.next();
            if (session.getRequestEntry().getTag().equals(tag)) {
                session.cancelRequest();
                it.remove();
            }
        }
    }
}