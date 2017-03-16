package org.wdd.app.android.seedoctor.ui.news.data;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.news.model.Banner;
import org.wdd.app.android.seedoctor.ui.news.model.News;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 3/16/17.
 */

public class NewsDataGetter {

    private HttpSession mSession;

    private Context context;
    private ActivityFragmentAvaliable host;
    private NewsCallback callback;

    private int page = 1;

    public NewsDataGetter(Context context, ActivityFragmentAvaliable host) {
        this.context = context;
        this.host = host;
    }

    public void requestNewsData(String channelId, final boolean isAppend) {
        if (isAppend) {
            page++;
        } else {
            page = 1;
        }
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setShouldCached(false);
        requestEntry.setUrl(ServiceApi.NEWS_LIST);
        requestEntry.addRequestParam("page", page + "");
        if (!TextUtils.isEmpty(channelId)) {
            requestEntry.addRequestParam("channel_id", channelId);
        }
        mSession = HttpManager.getInstance(context).sendHttpRequest(host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                mSession = null;
                if (callback == null) return;
                if (res.getData() == null) {
                    callback.onFailure(isAppend, HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                } else {
                    JSONObject json = (JSONObject)res.getData();
                    JSONArray bannerJson = json.getJSONArray("banner_list");
                    List<Banner> banners = new ArrayList<>();
                    if (bannerJson != null && bannerJson.size() > 0) {
                        Banner banner;
                        for (int i = 0; i < bannerJson.size(); i++) {
                            banner = JSON.parseObject(bannerJson.getString(i), Banner.class);
                            if ("推广".equals(banner.description)) continue;
                            banners.add(banner);
                        }
                    }
                    JSONArray contentJson = json.getJSONArray("content_list");
                    List<News> contents = new ArrayList<>();
                    if (contentJson != null && contentJson.size() > 0) {
                        News news;
                        JSONObject newsJson;
                        String type;
                        JSONArray dataJson;
                        for (int i = 0; i < contentJson.size(); i++) {
                            newsJson = contentJson.getJSONObject(i);
                            dataJson = newsJson.getJSONArray("content");
                            if (dataJson == null || dataJson.size() == 0) continue;
                            type = newsJson.getString("type");
                            if ("news".equals(type)) {
                                news = JSON.parseObject(dataJson.getString(0), News.class);
                            } else {
                                continue;
                            }
                            contents.add(news);
                        }
                    }
                    callback.onDataGetted(isAppend, banners, contents);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                mSession = null;
                if (isAppend) {
                    page--;
                }
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError(isAppend);
                } else {
                    callback.onFailure(isAppend, HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }
        });
    }

    public void setNewsCallback(NewsCallback callback) {
        this.callback = callback;
    }

    public void cancelRequest() {
        if (mSession == null) return;
        mSession.cancelRequest();
        mSession = null;
    }

    public interface NewsCallback {

        void onDataGetted(boolean isAppend, List<Banner> banners, List<News> contents);
        void onFailure(boolean isAppend, String error);
        void onNetworkError(boolean isAppend);

    }
}
