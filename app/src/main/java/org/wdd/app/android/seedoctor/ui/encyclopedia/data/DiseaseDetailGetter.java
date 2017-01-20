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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DiseaseDetailGetter {

    private Context context;
    private ActivityFragmentAvaliable holder;
    private DiseaseDetailCallback callback;
    private HttpManager manager;

    public DiseaseDetailGetter(ActivityFragmentAvaliable holder, Context context, DiseaseDetailCallback callback) {
        this.holder = holder;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDiseaseDetailData(int diseaseId) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.WIKI_DISEASE_DETAIL);
        requestEntry.addRequestParam("diseaseid", diseaseId + "");
        HttpSession session = manager.sendHttpRequest(requestEntry, DiseaseDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (!holder.isAvaliable()) return;
                if (res.getData() != null) {
                    DiseaseDetail diseaseDetail = (DiseaseDetail) res.getData();
                    callback.onRequestOk(diseaseDetail);
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

    public interface DiseaseDetailCallback {

        void onRequestOk(DiseaseDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

    }
}
