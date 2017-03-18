package org.wdd.app.android.seedoctor.ui.news.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.news.activity.NewsDetailActivity;
import org.wdd.app.android.seedoctor.ui.news.adapter.NewsAdapter;
import org.wdd.app.android.seedoctor.ui.news.model.Banner;
import org.wdd.app.android.seedoctor.ui.news.model.News;
import org.wdd.app.android.seedoctor.ui.news.presenter.NewsPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 3/16/17.
 */

public class NewsFragment extends BaseFragment {

    public static NewsFragment newInstance(String channelId) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString("channel_id", channelId);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewGroup mRootView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mListView;
    private LoadView mLoadView;

    private NewsPresenter mPresenter;
    private NewsAdapter mAdapter;
    private List<News> mNews;
    private String mChannelId;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);
            initData();
            initTitles();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initData() {
        mChannelId = getArguments().getString("channel_id");
        mPresenter = new NewsPresenter(this);
    }

    private void initTitles() {

    }

    private void initViews() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.fragment_news_refresh_layout);
        mListView = (RecyclerView) mRootView.findViewById(R.id.fragment_news_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(layoutManager);
        LineDividerDecoration decoration = new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL, Color.parseColor("#e7e7e7"));
        mListView.addItemDecoration(decoration);
        mLoadView = (LoadView) mRootView.findViewById(R.id.fragment_news_loadview);

        mLoadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                mPresenter.getNewsData(mChannelId, false);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNewsData(mChannelId, false);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getNewsData(mChannelId, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
    }

    public void onNetworkError(boolean isAppend) {
        if (mAdapter != null) {
            AppToaster.show(R.string.no_connection_error);
            if (isAppend) {
                mAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
        } else {
            mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
        }
    }

    public void showRequestFailureViews(boolean isAppend, String error) {
        if (mAdapter != null) {
            AppToaster.show(error);
            if (isAppend) {
                mAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
        } else {
            mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
        }
    }

    public void showNewsDataViews(boolean isAppend, List<Banner> banners, List<News> contents) {
        if (mAdapter == null) {
            mNews = new ArrayList<>();
            mNews.addAll(contents);
            mAdapter = new NewsAdapter(getContext(), mChannelId, mNews, banners);
            mAdapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getNewsData(mChannelId, true);
                }
            });
            mAdapter.setOnBannerClickedListener(new NewsAdapter.OnNewsClickedListener() {
                @Override
                public void onNewsClicked(News news) {
                    NewsDetailActivity.show(getActivity(), news.id, news.image, news.title);
                }

                @Override
                public void onBannerClicked(Banner banner) {
                    NewsDetailActivity.show(getActivity(), banner.id, banner.image, banner.description);
                }
            });
            mLoadView.setStatus(LoadView.LoadStatus.Normal);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mListView.setAdapter(mAdapter);
        } else {
            if (!isAppend) {
                mNews.clear();
                mAdapter.refreshData(contents, banners);
                mRefreshLayout.setRefreshing(false);
            } else {
                mAdapter.appendData(contents);
            }
            mAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            mNews.addAll(contents);
        }
    }

    public void showNoMoreNewsViews() {
        mAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }

    public void showNoNewsViews() {
        mLoadView.setStatus(LoadView.LoadStatus.No_Data);
    }
}
