package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Province;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalFilterGetter {

    private Context context;
    private DataCallback callback;
    private HttpManager manager;

    public HospitalFilterGetter(Context context, DataCallback callback) {
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestHospitalDetailData() {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.PROVINCE_LIST);
        HttpSession session = manager.sendHttpRequest(requestEntry, Province.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Province> provinces = (List<Province>) res.getData();
                    callback.onRequestOk(provinces);
                } else {
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    callback.onRequestFailure(error);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                callback.onRequestFailure(error);
            }

            @Override
            public void onNetworkError() {
                callback.onNetworkError();
            }
        });
        return session;
    }

    public interface DataCallback {

        void onRequestOk(List<Province> data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

    }
}
