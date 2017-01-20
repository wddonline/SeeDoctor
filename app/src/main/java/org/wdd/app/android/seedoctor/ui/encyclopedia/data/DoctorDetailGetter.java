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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DoctorDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DoctorDetailGetter {

    private Context context;
    private ActivityFragmentAvaliable holder;
    private DoctorDetailCallback callback;
    private HttpManager manager;

    public DoctorDetailGetter(ActivityFragmentAvaliable holder, Context context, DoctorDetailCallback callback) {
        this.holder = holder;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDoctorDetailData(String doctorid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.DOCTOR_DETAIL);
        requestEntry.addRequestParam("doctorid", doctorid);
        HttpSession session = manager.sendHttpRequest(requestEntry, DoctorDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (!holder.isAvaliable()) return;
                if (res.getData() != null) {
                    DoctorDetail detail = (DoctorDetail) res.getData();
                    callback.onRequestOk(detail);
                } else {
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    callback.onRequestFailure(error);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (!holder.isAvaliable()) return;
                callback.onRequestFailure(error);
            }

            @Override
            public void onNetworkError() {
                if (!holder.isAvaliable()) return;
                callback.onNetworkError();
            }
        });
        return session;
    }

    public interface DoctorDetailCallback {

        void onRequestOk(DoctorDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

    }
}
