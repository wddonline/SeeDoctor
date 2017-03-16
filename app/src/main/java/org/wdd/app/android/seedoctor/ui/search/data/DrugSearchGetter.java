package org.wdd.app.android.seedoctor.ui.search.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class DrugSearchGetter {

    public static final int PAGE_SISE = 20;
    private int page = 1;

    private Context context;
    private HttpManager manager;
    private ActivityFragmentAvaliable host;
    private SearchCallback callback;
    private HttpSession session;

    public DrugSearchGetter(ActivityFragmentAvaliable host, Context context, SearchCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public void getDrugListByName(String keyword, final boolean refresh) {
        if (session != null) session.cancelRequest();
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("keyword", keyword);
        requestEntry.addRequestParam("pagesize", PAGE_SISE + "");
        requestEntry.setUrl(ServiceApi.WIKI_DRUG_LIST);
        session = manager.sendHttpRequest(host, requestEntry, Drug.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    List<Drug> data = (List<Drug>) res.getData();
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

    public interface SearchCallback {

        void onRequestOk(List<Drug> data, boolean refresh);
        void onRequestFailure(String error, boolean refresh);
        void onNetworkError(boolean refresh);

    }
}
