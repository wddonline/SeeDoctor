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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalFilterGetter {

    private Context context;
    private DataCallback callback;
    private ActivityFragmentAvaliable host;
    private HttpManager manager;
    private HttpSession session;

    public HospitalFilterGetter(ActivityFragmentAvaliable host, Context context, DataCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public void requestHospitalDetailData() {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.PROVINCE_LIST);
        session = manager.sendHttpRequest(host, requestEntry, Province.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    List<Province> provinces = (List<Province>) res.getData();
                    callback.onRequestOk(provinces);
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

    public interface DataCallback {

        void onRequestOk(List<Province> data);
        void onRequestFailure(String error);
        void onNetworkError();

    }
}
