package org.wdd.app.android.seedoctor.ui.search.data;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.utils.LogUtils;
import org.wdd.app.android.seedoctor.utils.NetworkUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class DiseaseSearchGetter {

    public static final int PAGEZISE = 20;
    private int page = 1;

    private Context context;
    private HttpManager manager;
    private SearchCallback callback;

    public DiseaseSearchGetter(Context context, SearchCallback callback) {
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
    }

    public HttpSession getDiseaseListByName(String keyword, final boolean refresh) {
        if (refresh) page = 1;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.addRequestParam("keyword", keyword);
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

    public interface SearchCallback {

        void onRequestOk(List<Disease> data, boolean refresh);
        void onRequestFailure(HttpError error, boolean refresh);
        void onNetworkError(boolean refresh);

    }
}
