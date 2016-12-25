package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.message.util.HttpRequest;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDiseaseGetter {

    private Context context;
    private HttpManager manager;
    private WikiDiseaseDataCallback callback;

    private int page = 1;

    public WikiDiseaseGetter(Context context) {
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDiseaseList(String drugid, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        if (!TextUtils.isEmpty(drugid)) {
            requestEntry.addRequestParam("drugid", drugid);
        }
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServiceApi.WIKI_DISEASE_LIST);
        HttpSession request = manager.sendHttpRequest(requestEntry, Disease.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Disease> data = (List<Disease>) res.getData();
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

    public void setCallback(WikiDiseaseDataCallback callback) {
        this.callback = callback;
    }

    public interface WikiDiseaseDataCallback {

        void onRequestOk(List<Disease> data, boolean refresh);
        void onRequestFailure(HttpError error, boolean refresh);
        void onNetworkError(boolean refresh);
    }
}
