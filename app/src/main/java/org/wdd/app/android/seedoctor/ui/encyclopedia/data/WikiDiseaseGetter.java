package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

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
    private WikiDiseaseDataCallback callback;

    private int page = 0;

    public WikiDiseaseGetter(Context context) {
        this.context = context;
    }

    /*
    * Map<String, String> params = new HashMap<String, String>();
			if (!getIntent().hasExtra("diseaseClasify")) {
				params.put("page", page + "");
			}
			if (keyword != null) {
				params.put("keyword", keyword);
			}
			if (sectionId != null) {
				params.put("departmentid", sectionId);
			}
			if (!TextUtils.isEmpty(className)) {
				if (className.equals(MyAttentionActivity.class.getName())) {
					params.put("type", 1 + "");
					action = "/collection/list/";
				} else if (className.equals(SectionDetailActivity.class.getName())) {
					if (getIntent().hasExtra("diseaseClasify")) {
						action = "/wiki/commondisease/";
						plv_disease.setOnRefreshListener(null);
					}
				}
			}
    * */
    public HttpSession requestDiseaseList(final boolean refresh) {
        if (refresh) page = 0;
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.addRequestParam("page", page + "");
        requestEntry.setUrl(ServiceApi.WIKI_DISEASE_LIST);
        HttpSession request = HttpManager.getInstance(context).sendHttpRequest(requestEntry, Disease.class, new HttpConnectCallback() {
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
