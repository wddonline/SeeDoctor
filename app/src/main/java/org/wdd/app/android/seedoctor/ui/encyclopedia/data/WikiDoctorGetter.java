package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;
import android.text.TextUtils;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDoctorGetter {

    public static final int PAGE_SIZE = 20;

    private Context context;
    private HttpManager manager;
    private WikiDoctorDataCallback callback;

    private int page = 1;

    public WikiDoctorGetter(Context context) {
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDoctorList(String hospitalid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        if (!TextUtils.isEmpty(hospitalid)) {
            requestEntry.addRequestParam("hospitalid", hospitalid);
        }
        requestEntry.setUrl(ServiceApi.DOCTOR_LIST);
        HttpSession request = manager.sendHttpRequest(requestEntry, Doctor.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Doctor> data = (List<Doctor>) res.getData();
                    if (callback != null) callback.onRequestOk(data, false);
                } else {
                    page--;
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    if (callback != null) callback.onRequestFailure(error, false);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                page--;
                if (callback != null) callback.onRequestFailure(error, false);
            }

            @Override
            public void onNetworkError() {
                page--;
                if (callback != null) callback.onNetworkError(false);
            }
        });
        page++;
        return request;
    }

    public HttpSession requestDoctorList(String provinceid, String hospitallevel, String doclevelid, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        if (!TextUtils.isEmpty(provinceid)) {
            requestEntry.addRequestParam("provinceid", provinceid);
        }
        if (!TextUtils.isEmpty(hospitallevel)) {
            requestEntry.addRequestParam("hospitallevel", hospitallevel);
        }
        if (!TextUtils.isEmpty(doclevelid)) {
            requestEntry.addRequestParam("doclevelid", doclevelid);
        }
        requestEntry.setUrl(ServiceApi.DOCTOR_LIST);
        HttpSession request = manager.sendHttpRequest(requestEntry, Doctor.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Doctor> data = (List<Doctor>) res.getData();
                    if (callback != null) callback.onRequestOk(data, refresh);
                } else {
                    page--;
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    if (callback != null) callback.onRequestFailure(error, refresh);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                page--;
                if (callback != null) callback.onRequestFailure(error, refresh);
            }

            @Override
            public void onNetworkError() {
                page--;
                if (callback != null) callback.onNetworkError(refresh);
            }
        });
        page++;
        return request;
    }

    public void setCallback(WikiDoctorDataCallback callback) {
        this.callback = callback;
    }

    public interface WikiDoctorDataCallback {

        void onRequestOk(List<Doctor> data, boolean refresh);
        void onRequestFailure(HttpError error, boolean refresh);
        void onNetworkError(boolean refresh);
    }
}
