package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class RelativeDrugListGetter {

    public static final int PAGE_SIZE = 20;

    private Context context;
    private HttpManager manager;
    private RelativeDrugDataCallback callback;

    private int page = 1;

    public RelativeDrugListGetter(Context context) {
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDrugList(String diseaseid, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("diseaseid", diseaseid + "");
        requestEntry.setUrl(ServiceApi.NEW_WIKI_DRUG_LIST);
        HttpSession request = manager.sendHttpRequest(requestEntry, Drug.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Drug> data = (List<Drug>) res.getData();
                    if (callback != null) callback.onRequestOk(data, refresh);
                } else {
                    page--;
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    if (callback != null) callback.onRequestFailure(error, refresh);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                page--;
                if (callback != null) callback.onRequestFailure(error, refresh);
            }

            @Override
            public void onNetworkError() {
                page--;
                if (callback != null) callback.onNetworkError(refresh);
            }
        });
        page++;
        return request;
    }

    public void setCallback(RelativeDrugDataCallback callback) {
        this.callback = callback;
    }

    public interface RelativeDrugDataCallback {

        void onRequestOk(List<Drug> data, boolean refresh);
        void onRequestFailure(HttpError error, boolean refresh);
        void onNetworkError(boolean refresh);
    }
}
