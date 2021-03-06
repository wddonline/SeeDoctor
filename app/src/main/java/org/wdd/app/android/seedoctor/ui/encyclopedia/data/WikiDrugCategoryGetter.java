package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDrugCategoryGetter {

    private Context context;
    private HttpManager manager;
    private ActivityFragmentAvaliable host;
    private WikiDrugCategoryDataCallback callback;
    private HttpSession session;

    public WikiDrugCategoryGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
        manager = HttpManager.getInstance(context);
    }

    public void requestDrugCategoryList() {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.WIKI_DRUG_CATEGORY_LIST);
        session = manager.sendHttpRequest(host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    String jsonStr = (String) res.getData();
                    try {
                        Map<String, Map<String, List<DrugCategory>>> data = new LinkedHashMap<>();

                        JSONObject firstJson = new JSONObject(jsonStr);
                        JSONArray firstNames = firstJson.names();
                        for (int i = 0; i < firstNames.length(); i++) {
                            String firstName = firstNames.getString(i);
                            Object piece = firstJson.get(firstName);
                            Map<String, List<DrugCategory>> secondCategories = new LinkedHashMap<>();
                            if (piece instanceof JSONObject) {

                                JSONObject secondJson = (JSONObject) piece;
                                JSONArray secondNames = secondJson.names();

                                for (int j = 0; j < secondNames.length(); j++) {
                                    String secondName = secondNames.getString(j);

                                    List<DrugCategory> thirdCategories = new ArrayList<>();
                                    JSONArray thirdJson = secondJson.getJSONArray(secondName);
                                    for (int k = 0; k < thirdJson.length(); k++) {
                                        JSONObject json = thirdJson.getJSONObject(k);
                                        DrugCategory category = new DrugCategory();
                                        category.catid = json.getInt("catid");
                                        category.catname = json.getString("catname");
                                        thirdCategories.add(category);
                                    }
                                    secondCategories.put(secondName, thirdCategories);
                                }
                            } else {

                                List<DrugCategory> thirdCategories = new ArrayList<>();
                                JSONArray thirdJson = (JSONArray) piece;
                                for (int k = 0; k < thirdJson.length(); k++) {
                                    JSONObject json = thirdJson.getJSONObject(k);
                                    DrugCategory category = new DrugCategory();
                                    category.catid = json.getInt("catid");
                                    category.catname = json.getString("catname");
                                    thirdCategories.add(category);
                                }
                                secondCategories.put(firstName, thirdCategories);
                            }

                            data.put(firstName, secondCategories);
                        }
                        if (callback != null) callback.onRequestOk(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                    }

                } else {
                    if (callback != null) callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.UNKNOW_ERROR));
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                session = null;
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError();
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }

        });
    }

    public void cancelRequest() {
        if (session == null) return;
        session.cancelRequest();
        session = null;
    }

    public void setCallback(WikiDrugCategoryDataCallback callback) {
        this.callback = callback;
    }

    public interface WikiDrugCategoryDataCallback {

        void onRequestOk(Map<String, Map<String, List<DrugCategory>>> data);
        void onRequestFailure(String error);
        void onNetworkError();
    }
}
