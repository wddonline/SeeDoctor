package org.wdd.app.android.seedoctor.ui.nearby.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.gallery.activity.ImageBrowserActivity;
import org.wdd.app.android.seedoctor.ui.nearby.model.Mark;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */
public class NearbySearchAdapter extends AbstractCommonAdapter<Mark> {

    private Activity mActivity;

    public NearbySearchAdapter(Activity activity, List<Mark> data) {
        super(activity, data);
        this.mActivity = activity;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nearby_search, parent, false);
        RecyclerView.ViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Mark item, int position) {
        final SearchViewHolder vh = (SearchViewHolder) holder;
        if (item.getImgUrls() != null && item.getImgUrls().length > 0) {
            vh.imageView.setImageUrl(item.getImgUrls()[0]);
        } else {
            vh.imageView.setImageUrl(null);
        }
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getImgUrls() != null && item.getImgUrls().length > 0) {
                    ImageBrowserActivity.show(mActivity, v, item.getImgUrls());
                } else {
                    vh.clickView.performClick();
                }
            }
        });
        if (!TextUtils.isEmpty(item.getTypeDes())) {
            vh.levelView.setVisibility(View.VISIBLE);
            String[] types = item.getTypeDes().split(";");
            vh.levelView.setText(types[types.length - 1]);
        } else {
            vh.levelView.setVisibility(View.GONE);
        }
        vh.nameView.setText(item.getName());
        vh.addressView.setText(item.getAddress());
        vh.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteLineActivity.show(context, item.getLatLong().latitude, item.getLatLong().longitude);
            }
        });
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        NetworkImageView imageView;
        TextView levelView;
        TextView nameView;
        TextView addressView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_nearby_search_click);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_nearby_search_img);
            levelView = (TextView) itemView.findViewById(R.id.item_nearby_search_level);
            nameView = (TextView) itemView.findViewById(R.id.item_nearby_search_name);
            addressView = (TextView) itemView.findViewById(R.id.item_nearby_search_address);
        }

    }
}