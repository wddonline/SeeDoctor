package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalDepartmentGetter {

    private Context context;
    private HttpManager manager;
    private ActivityFragmentAvaliable host;
    private DepartmentDataCallback callback;
    private HttpSession session;

    public HospitalDepartmentGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public void requestDepartmentData(String hospitalid, String parenthosdepid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("hospitalid",  hospitalid);
        requestEntry.addRequestParam("parenthosdepid", parenthosdepid);
        requestEntry.setUrl(ServiceApi.HOSPITAL_DEPARTMENT);
        session = manager.sendHttpRequest(host, requestEntry, Department.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    List<Department> data = (List<Department>) res.getData();
                    if (callback != null) callback.onRequestOk(data);
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                session = null;
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError();
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }

        });
    }

    public void cancelRequest() {
        if (session == null) return;
        session.cancelRequest();
        session = null;
    }

    public void setCallback(DepartmentDataCallback callback) {
        this.callback = callback;
    }

    public interface DepartmentDataCallback {

        void onRequestOk(List<Department> data);
        void onRequestFailure(String error);
        void onNetworkError();
    }
}
