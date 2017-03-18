package org.wdd.app.android.seedoctor.ui.news.presenter;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.news.activity.NewsDetailActivity;
import org.wdd.app.android.seedoctor.ui.news.data.NewsDetailDataGetter;

/**
 * Created by richard on 3/16/17.
 */

public class NewsDetailPresenter implements BasePresenter, NewsDetailDataGetter.NewsDetailCallback {

    private NewsDetailActivity mView;
    private NewsDetailDataGetter mGetter;

    public NewsDetailPresenter(NewsDetailActivity view) {
        this.mView = view;
        mGetter = new NewsDetailDataGetter(view, view.host);
        mGetter.setNewsDetailCallback(this);
    }

    public void getNewsDetailData(String id) {
        mGetter.requestNewsDetailData(id);
    }

    public void getCollectionStatus(String newsId) {
        mGetter.getCollectionStatus(newsId);
    }

    public void collectNews(String newsId, String image, String title) {
        mGetter.collectNews(newsId, image, title);
    }

    public void uncollectNews(String newsId) {
        mGetter.uncollectNews(newsId);
    }
    @Override
    public void cancelRequest() {
        mGetter.cancelRequest();
    }

    @Override
    public void onDataGetted(String html) {
//        mView.showNewsDetailDataViews(html);
    }

    @Override
    public void onFailure(String error) {
//        mView.showRequestFailureViews(error);
    }

    @Override
    public void onNetworkError() {
//        mView.onNetworkError();
    }

    @Override
    public void onCollectionStatusGetted(boolean isCollected) {
        mView.setNewsCollectionViews(isCollected);
    }

    @Override
    public void onCollectOver(boolean success) {
        mView.updateNewsCollectedStatus(success);
    }

    @Override
    public void onUncollectOver(boolean success) {
        mView.updateNewsUncollectedStatus(success);
    }
}
