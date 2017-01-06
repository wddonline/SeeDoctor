package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;
import android.text.TextUtils;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiHospitalGetter {

    public static final int PAGE_SIZE = 20;

    private Context context;
    private HttpManager manager;
    private WikiHospitalDataCallback callback;

    private int page = 1;

    public WikiHospitalGetter(Context context) {
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestHospitalList(String provinceid, String hospitallevel, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("pagesize", PAGE_SIZE + "");
        if (!TextUtils.isEmpty(provinceid)) {
            requestEntry.addRequestParam("provinceid", provinceid);
        }
        if (!TextUtils.isEmpty(hospitallevel)) {
            requestEntry.addRequestParam("level", hospitallevel);
        }
        requestEntry.setUrl(ServiceApi.HOSPITAL_LIST);
        HttpSession request = manager.sendHttpRequest(requestEntry, Hospital.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    List<Hospital> data = (List<Hospital>) res.getData();
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

    public void setCallback(WikiHospitalDataCallback callback) {
        this.callback = callback;
    }

    public interface WikiHospitalDataCallback {

        void onRequestOk(List<Hospital> data, boolean refresh);
        void onRequestFailure(HttpError error, boolean refresh);
        void onNetworkError(boolean refresh);
    }
}
