package org.wdd.app.android.seedoctor.ui.search.data;

import android.content.Context;
import android.text.TextUtils;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class HospitalSearchGetter {

    public static final int PAGE_SIZE = 20;
    private int page = 1;

    private Context context;
    private HttpManager manager;
    private ActivityFragmentAvaliable host;
    private SearchCallback callback;

    public HospitalSearchGetter(ActivityFragmentAvaliable host, Context context, SearchCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession getHospitalList(String provinceid, String hospitallevel, String keyword, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("keyword", keyword);
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        if (!TextUtils.isEmpty(provinceid)) {
            requestEntry.addRequestParam("provinceid", provinceid);
        }
        if (!TextUtils.isEmpty(hospitallevel)) {
            requestEntry.addRequestParam("level", hospitallevel);
        }
        requestEntry.setUrl(ServiceApi.HOSPITAL_LIST);
        HttpSession request = manager.sendHttpRequest(host, requestEntry, Hospital.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Hospital> data = (List<Hospital>) res.getData();
                    if (callback != null) callback.onRequestOk(data, refresh);
                } else {
                    page--;
                    if (callback != null) callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR), refresh);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
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
        return request;
    }

    public interface SearchCallback {

        void onRequestOk(List<Hospital> data, boolean refresh);
        void onRequestFailure(String error, boolean refresh);
        void onNetworkError(boolean refresh);

    }
}
