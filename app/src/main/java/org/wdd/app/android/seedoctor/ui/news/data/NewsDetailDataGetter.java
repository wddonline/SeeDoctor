package org.wdd.app.android.seedoctor.ui.news.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.NewsDbManager;
import org.wdd.app.android.seedoctor.database.model.DBNews;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

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

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
