package org.wdd.app.android.seedoctor.ui.news.presenter;

import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.news.data.NewsDataGetter;
import org.wdd.app.android.seedoctor.ui.news.fragment.NewsFragment;
import org.wdd.app.android.seedoctor.ui.news.model.Banner;
import org.wdd.app.android.seedoctor.ui.news.model.News;

import java.util.List;

/**
 * Created by richard on 3/16/17.
 */

public class NewsPresenter implements BasePresenter, NewsDataGetter.NewsCallback {

    private NewsFragment mView;
    private NewsDataGetter mGetter;

    public NewsPresenter(NewsFragment view) {
        this.mView = view;
        mGetter = new NewsDataGetter(view.getContext(), view.host);
        mGetter.setNewsCallback(this);
    }

    public void getNewsData(String channelId, boolean isAppend) {
        mGetter.requestNewsData(channelId, isAppend);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelRequest();
    }

    @Override
    public void onDataGetted(boolean isAppend, List<Banner> banners, List<News> contents) {
        if (isAppend) {
            if (contents.size() == 0) {
                mView.showNoMoreNewsViews();
                return;
            }
        } else {
            if (banners.size() == 0 && contents.size() == 0) {
                mView.showNoNewsViews();
                return;
            }
        }
        mView.showNewsDataViews(isAppend, banners, contents);
    }

    @Override
    public void onFailure(boolean isAppend, String error) {
        mView.showRequestFailureViews(isAppend, error);
    }

    @Override
    public void onNetworkError(boolean isAppend) {
        mView.onNetworkError(isAppend);
    }
}
