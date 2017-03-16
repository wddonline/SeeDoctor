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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDiseaseGetter {

    public static final int PAGE_SIZE = 20;

    private Context context;
    private HttpManager manager;
    private ActivityFragmentAvaliable host;
    private WikiDiseaseDataCallback callback;
    private HttpSession session;

    private int page = 1;

    public WikiDiseaseGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public void requestDiseaseList(final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        requestEntry.setUrl(ServiceApi.WIKI_DISEASE_LIST);
        session = manager.sendHttpRequest(host, requestEntry, Disease.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    List<Disease> data = (List<Disease>) res.getData();
                    if (callback != null) callback.onRequestOk(data, refresh);
                } else {
                    page--;
                    if (callback != null) callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR), refresh);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                session = null;
                page--;
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError(refresh);
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()), refresh);
                }
            }

        });
        page++;
    }

    public void cancelRequest() {
        if (session == null) return;
        session.cancelRequest();
        session = null;
    }

    public void setCallback(WikiDiseaseDataCallback callback) {
        this.callback = callback;
    }

    public interface WikiDiseaseDataCallback {

        void onRequestOk(List<Disease> data, boolean refresh);
        void onRequestFailure(String error, boolean refresh);
        void onNetworkError(boolean refresh);
    }
}
