package org.wdd.app.android.seedoctor.utils;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.doctor.model.Doctor;

/**
 * Created by wangdd on 16-11-26.
 */

public class CommonHttpMethod {

    private static final String BASE_URL = "http://api.niruoanhao.com/";

    public static HttpSession getDoctorList(Context context, int page, int pageSize, HttpConnectCallback callback) {
        HttpManager manager = HttpManager.getInstance(context);
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(BASE_URL + "doctor/");
        requestEntry.setMethod(HttpRequestEntry.Method.POST);
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("pagesize", "2");

        HttpSession session = manager.sendHttpRequest(requestEntry, Doctor.class, callback);
        return session;
    }
}
