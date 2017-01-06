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

public class HospitalDoctorGetter {

    public static final int PAGE_SIZE = 20;

    private Context context;
    private HttpManager manager;
    private HospitalDoctorDataCallback callback;

    private int page = 1;

    public HospitalDoctorGetter(Context context) {
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
                    if (callback != null) callback.onRequestOk(data);
                } else {
                    page--;
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    if (callback != null) callback.onRequestFailure(error);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                page--;
                if (callback != null) callback.onRequestFailure(error);
            }

            @Override
            public void onNetworkError() {
                page--;
                if (callback != null) callback.onNetworkError();
            }
        });
        page++;
        return request;
    }

    public void setCallback(HospitalDoctorDataCallback callback) {
        this.callback = callback;
    }

    public interface HospitalDoctorDataCallback {

        void onRequestOk(List<Doctor> data);
        void onRequestFailure(HttpError error);
        void onNetworkError();
    }
}
