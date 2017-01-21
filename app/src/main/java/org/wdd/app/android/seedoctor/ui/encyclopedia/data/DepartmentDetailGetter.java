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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class DepartmentDetailGetter {

    private Context context;
    private ActivityFragmentAvaliable host;
    private DepartmentDetailCallback callback;

    public enum Type {
        Department,
        Disease
    }

    public DepartmentDetailGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
    }

    public HttpSession requestDepartmentData(String departmentid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.DEPARTMENT_DETAIL);
        requestEntry.addRequestParam("departmentid", departmentid);
        HttpSession session = HttpManager.getInstance(context).sendHttpRequest(host, requestEntry, Department.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (callback == null) return;
                if (res.getData() == null) {
                    callback.onFailure(Type.Department, new HttpError(ErrorCode.UNKNOW_ERROR, ""));
                } else {
                    callback.onDataGetted(Type.Department, res.getData());
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback == null) return;
                callback.onFailure(Type.Department, error);
            }

            @Override
            public void onNetworkError() {
                if (callback == null) return;
                callback.onNetworkError(Type.Department);
            }
        });
        return session;
    }

    public HttpSession requestDiseaseData(String departmentid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.NEW_WIKI_DISEASE_LIST);
        requestEntry.addRequestParam("departmentid", departmentid);
        HttpSession session = HttpManager.getInstance(context).sendHttpRequest(host, requestEntry, Disease.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (callback == null) return;
                if (res.getData() == null && ((List)res.getData()).size() == 0) {
                    callback.onFailure(Type.Disease, new HttpError(ErrorCode.UNKNOW_ERROR, ""));
                } else {
                    callback.onDataGetted(Type.Disease, res.getData());
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback == null) return;
                callback.onFailure(Type.Disease, error);
            }

            @Override
            public void onNetworkError() {
                if (callback == null) return;
                callback.onNetworkError(Type.Disease);
            }
        });
        return session;
    }


    public void setDepartmentDetailCallback(DepartmentDetailCallback callback) {
        this.callback = callback;
    }

    public interface DepartmentDetailCallback {

        void onDataGetted(Type type, Object data);
        void onFailure(Type type, HttpError error);
        void onNetworkError(Type type);

    }
}
