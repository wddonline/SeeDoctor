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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalDetailGetter {

    private Context context;
    private HospitalDetailCallback callback;
    private ActivityFragmentAvaliable host;
    private HttpManager manager;

    public HospitalDetailGetter(ActivityFragmentAvaliable host, Context context, HospitalDetailCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestHospitalDetailData(String hospitalid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.HOSPITAL_DETAIL);
        requestEntry.addRequestParam("hospitalid", hospitalid);
        HttpSession session = manager.sendHttpRequest(host, requestEntry, HospitalDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    HospitalDetail hospitalDetail = (HospitalDetail) res.getData();
                    callback.onRequestOk(hospitalDetail);
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

    public interface HospitalDetailCallback {

        void onRequestOk(HospitalDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

    }
}
