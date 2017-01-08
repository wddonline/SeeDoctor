package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class HospitalDepartmentGetter {

    private Context context;
    private HttpManager manager;
    private DepartmentDataCallback callback;

    public HospitalDepartmentGetter(Context context) {
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDepartmentData(String hospitalid, String parenthosdepid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("hospitalid",  hospitalid);
        requestEntry.addRequestParam("parenthosdepid", parenthosdepid);
        requestEntry.setUrl(ServiceApi.HOSPITAL_DEPARTMENT);
        final HttpSession request = manager.sendHttpRequest(requestEntry, Department.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Department> data = (List<Department>) res.getData();
                    if (callback != null) callback.onRequestOk(data);
                } else {
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    if (callback != null) callback.onRequestFailure(error);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback != null) callback.onRequestFailure(error);
            }

            @Override
            public void onNetworkError() {
                if (callback != null) callback.onNetworkError();
            }
        });
        return request;
    }

    public void setCallback(DepartmentDataCallback callback) {
        this.callback = callback;
    }

    public interface DepartmentDataCallback {

        void onRequestOk(List<Department> data);
        void onRequestFailure(HttpError error);
        void onNetworkError();
    }
}
