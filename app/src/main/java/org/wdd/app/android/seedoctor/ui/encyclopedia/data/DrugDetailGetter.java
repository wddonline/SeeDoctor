package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DrugDetailGetter {

    private Context context;
    private DrugDetailCallback callback;
    private HttpManager manager;

    public DrugDetailGetter(Context context, DrugDetailCallback callback) {
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDrugDetailData(int drugId) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.WIKI_DRUG_DETAIL);
        requestEntry.addRequestParam("drugid", drugId + "");
        HttpSession session = manager.sendHttpRequest(requestEntry, DrugDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    DrugDetail diseaseDetail = (DrugDetail) res.getData();
                    callback.onRequestOk(diseaseDetail);
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

    public interface DrugDetailCallback {

        void onRequestOk(DrugDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

    }
}
