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
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class WikiDepartmentGetter {

    private Context context;
    private ActivityFragmentAvaliable host;
    private DepartmentCallback callback;

    public WikiDepartmentGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
    }

    public HttpSession requestDepartmentData(String hospitalid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        if (!TextUtils.isEmpty(hospitalid)) {
            requestEntry.addRequestParam("hospitalid", hospitalid);
        }
        requestEntry.setUrl(ServiceApi.WIKI_DEPARTMENT_LIST);
        HttpSession session = HttpManager.getInstance(context).sendHttpRequest(host, requestEntry, Department.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (callback == null) return;
                if (res.getData() == null) {
                    callback.onFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                } else {
                    callback.onDataGetted((List<Department>) res.getData());
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError();
                } else {
                    callback.onFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }

        });
        return session;
    }

    public HttpSession requestDepartmentData() {
        return requestDepartmentData(null);
    }

    public void setDepartmentCallback(DepartmentCallback callback) {
        this.callback = callback;
    }

    public interface DepartmentCallback {

        void onDataGetted(List<Department> data);
        void onFailure(String error);
        void onNetworkError();

    }
}
