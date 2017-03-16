package org.wdd.app.android.seedoctor.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.news.model.Banner;
import org.wdd.app.android.seedoctor.ui.news.model.News;
import org.wdd.app.android.seedoctor.views.BannerLayout;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 3/16/17.
 */

public class NewsAdapter extends AbstractCommonAdapter<NewsAdapter.Item> {

    private final int TYPE_BANNER = 0;
    private final int TYPE_NEWS = 1;

    private LayoutInflater mInflater;
    private String mChannelId;
    private OnNewsClickedListener mListener;

    private boolean isRefresh = false;

    public NewsAdapter(Context context, String channelId, List<News> newses, List<Banner> banners) {
        super(context);
        this.mChannelId = channelId;
        mInflater = LayoutInflater.from(context);
        refreshData(newses, banners);
    }

    public void appendData(List<News> newses) {
        Item item;
        for (News news : newses) {
            item = new Item();
            item.type = TYPE_NEWS;
            item.news = news;
            data.add(item);
        }
        notifyDataSetChanged();
    }

    public void refreshData(List<News> newses, List<Banner> banners) {
        isRefresh = true;
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        Item item;
        if (banners != null && banners.size() > 0) {
            item = new Item();
            item.type = TYPE_BANNER;
            item.banners = banners;
            data.add(item);
        }
        for (News news : newses) {
            item = new Item();
            item.type = TYPE_NEWS;
            item.news = news;
            data.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TYPE_BANNER:
                itemView = mInflater.inflate(R.layout.item_news_banners, parent, false);
                viewHolder = new BannerViewHolder(itemView);
                break;
            default:
                itemView = mInflater.inflate(R.layout.item_news_list, parent, false);
                viewHolder = new NewsViewHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Item item, int position) {
        switch (item.type) {
            case TYPE_BANNER:
                BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
                if (isRefresh) {
                    isRefresh = false;
                    List<String> urls = new ArrayList<>();
                    for (int i = 0; i < item.banners.size(); i++) {
                        urls.add(item.banners.get(i).image);
                    }
                    List<String> titles = new ArrayList<>();
                    for (int i = 0; i < item.banners.size(); i++) {
                        titles.add(item.banners.get(i).description);
                    }
                    bannerViewHolder.bannerLayout.setViewUrls(urls, titles);
                    bannerViewHolder.bannerLayout.setAutoPlay(true);
                    bannerViewHolder.bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (mListener == null) return;
                            mListener.onBannerClicked(item.banners.get(position));
                        }
                    });
                }
                break;
            default:
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.imageView.setImageUrl(item.news.image);
                newsViewHolder.titleView.setText(item.news.title);
                if (TextUtils.isEmpty(mChannelId)) {
                    newsViewHolder.typeView.setText(item.news.channel_name);
                } else {
                    newsViewHolder.typeView.setVisibility(View.GONE);
                }
                newsViewHolder.dateView.setText(item.news.date_str);
                newsViewHolder.clickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener == null) return;
                        mListener.onNewsClicked(item.news);
                    }
                });
                break;
        }

    }

    @Override
    protected int getSubItemViewType(int position) {
        return data.get(position).type;
    }

    public void setOnBannerClickedListener(OnNewsClickedListener listener) {
        this.mListener = listener;
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {

        private View clickView;
        NetworkImageView imageView;
        TextView titleView;
        TextView typeView;
        TextView dateView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_news_list_click);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_news_list_img);
            titleView = (TextView) itemView.findViewById(R.id.item_news_list_title);
            typeView = (TextView) itemView.findViewById(R.id.item_news_list_type);
            dateView = (TextView) itemView.findViewById(R.id.item_news_list_date);
        }
    }

    private class BannerViewHolder extends RecyclerView.ViewHolder {

        private BannerLayout bannerLayout;

        public BannerViewHolder(View itemView) {
            super(itemView);
            bannerLayout = (BannerLayout) itemView.findViewById(R.id.item_news_banners_ads);
        }
    }

    class Item {

        int type;
        News news;
        List<Banner> banners;

    }

    public interface OnNewsClickedListener {

        void onNewsClicked(News news);
        void onBannerClicked(Banner banner);

    }
}
