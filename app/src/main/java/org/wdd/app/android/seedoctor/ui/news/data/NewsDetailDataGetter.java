package org.wdd.app.android.seedoctor.ui.news.data;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.NewsDbManager;
import org.wdd.app.android.seedoctor.database.model.DBNews;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 3/17/17.
 */

public class NewsDetailDataGetter {

    private HttpSession mSession;

    private Context mContext;
    private ActivityFragmentAvaliable mHost;
    private NewsDetailCallback mCallback;
    private NewsDbManager dbManager;

    public NewsDetailDataGetter(Context context, ActivityFragmentAvaliable host) {
        this.mContext = context;
        this.mHost = host;
        dbManager = new NewsDbManager(context);
    }

    public void requestNewsDetailData(String id) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setShouldCached(true);
        String url = ServiceApi.NEWS_DETAIL;
        url = String.format(url, id, AppUtils.getScreenWidth(mContext));
        requestEntry.setUrl(url);
        mSession = HttpManager.getInstance(mContext).sendHtmlRequest("UTF-8", mHost, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                mSession = null;
                if (mCallback == null) return;
                if (res.getData() == null) {
                    mCallback.onFailure(HttpUtils.getErrorDescFromErrorCode(mContext, ErrorCode.SERVER_ERROR));
                } else {
                    Document document = (Document) res.getData();
                    document.getElementsByAttributeValue("class", "share-to").remove();
                    document.getElementsByAttributeValue("class", "news-func-item news-ask-doctor").remove();
                    document.getElementsByAttributeValue("class", "cy-dl sn-dl").remove();
                    mCallback.onDataGetted(document.html());
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                mSession = null;
                if (mCallback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    mCallback.onNetworkError();
                } else {
                    mCallback.onFailure(HttpUtils.getErrorDescFromErrorCode(mContext, error.getErrorCode()));
                }
            }
        });
    }

    public void setNewsDetailCallback(NewsDetailCallback callback) {
        this.mCallback = callback;
    }

    public void cancelRequest() {
        if (mSession == null) return;
        mSession.cancelRequest();
        mSession = null;
    }

    public void getCollectionStatus(String diseaseId) {
        Thread thread = new Thread(new GetCollectionStatusAction(diseaseId));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectNews(String newsId, String image, String title) {
        Thread thread = new Thread(new CollectNewsAction(newsId, image, title));
        thread.setDaemon(true);
        thread.start();
    }

    public void uncollectNews(String newsId) {
        Thread thread = new Thread(new UncollectNewsAction(newsId));
        thread.setDaemon(true);
        thread.start();
    }

    private class GetCollectionStatusAction implements Runnable {

        private String diseaseId;

        public GetCollectionStatusAction(String diseaseId) {
            this.diseaseId = diseaseId;
        }

        @Override
        public void run() {
            final DBNews news = dbManager.getNewsByNewsId(diseaseId);
            if (mCallback == null) return;
            if (!mHost.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCollectionStatusGetted(news != null);
                }
            });
        }
    }

    private class CollectNewsAction implements Runnable {

        private String newsId;
        private String image;
        private String title;

        public CollectNewsAction(String newsId, String image, String title) {
            this.newsId = newsId;
            this.image = image;
            this.title = title;
        }

        @Override
        public void run() {
            DBNews news = new DBNews(newsId, image, title);
            final long result = dbManager.insert(news);
            if (mCallback == null) return;
            if (!mHost.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCollectOver(result != -1);
                }
            });
        }
    }

    private class UncollectNewsAction implements Runnable {

        private String newsId;

        public UncollectNewsAction(String newsId) {
            this.newsId = newsId;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByNewsId(newsId);
            if (mCallback == null) return;
            if (!mHost.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onUncollectOver(result != -1);
                }
            });
        }
    }

    public interface NewsDetailCallback {

        void onDataGetted(String html);
        void onFailure(String error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
