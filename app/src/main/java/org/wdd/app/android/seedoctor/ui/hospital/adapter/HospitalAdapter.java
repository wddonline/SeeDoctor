package org.wdd.app.android.seedoctor.ui.hospital.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.views.HttpImageView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class HospitalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_DATA = 0;
    private final int TYPE_MORE = 1;

    public enum LoadStatus {
        Normal, Loading, NoMore
    }

    private Context context;
    private List<Hospital> data;
    private OnLoadMoreListener loadMoreListener;
    private LoadStatus status = LoadStatus.Normal;

    public HospitalAdapter(Context context, List<Hospital> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if (viewType == TYPE_MORE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_common_load_more_data, parent, false);
            viewHolder = new MoreDataVH(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_nearby_hospital, parent, false);
            viewHolder = new HospitalVH(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == TYPE_MORE) {
            MoreDataVH moreDataVH = (MoreDataVH) holder;
            switch (status) {
                case Normal:
                    moreDataVH.outerView.setVisibility(View.VISIBLE);
                    moreDataVH.loadBtn.setVisibility(View.VISIBLE);
                    moreDataVH.loadingLayout.setVisibility(View.GONE);
                    break;
                case Loading:
                    moreDataVH.outerView.setVisibility(View.VISIBLE);
                    moreDataVH.loadBtn.setVisibility(View.GONE);
                    moreDataVH.loadingLayout.setVisibility(View.VISIBLE);
                    break;
                case NoMore:
                    moreDataVH.outerView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            Hospital hospital = data.get(position);
            HospitalVH hospitalVH = (HospitalVH) holder;
            if (hospital.getImgUrls() != null && hospital.getImgUrls().length > 0) {
                hospitalVH.imageView.setImageUrl(hospital.getImgUrls()[0]);
            } else {
                hospitalVH.imageView.setImageUrl(null);
            }
            hospitalVH.nameView.setText(hospital.getName());
            hospitalVH.addressView.setText(hospital.getAddress());
            hospitalVH.distanceView.setText(getDistanceDesc(hospital.getDistance()));
        }
    }

    private String getDistanceDesc(long distance) {
        if (distance > 1000) {
            return String.format(context.getString(R.string.distance_desc_km), String.format("%.2f", distance / 1000f));
        } else {
            return String.format(context.getString(R.string.distance_desc_m), distance);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? TYPE_MORE : TYPE_DATA;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setLoadStatus(LoadStatus status) {
        this.status = status;
        notifyItemChanged(data.size());
    }

    private class MoreDataVH extends RecyclerView.ViewHolder {

        private View outerView;
        Button loadBtn;
        View loadingLayout;

        public MoreDataVH(View itemView) {
            super(itemView);
            outerView = itemView.findViewById(R.id.item_common_load_more_data_outer);
            loadBtn = (Button) itemView.findViewById(R.id.item_common_load_more_data_btn);
            loadingLayout = itemView.findViewById(R.id.item_common_load_more_data_loading);
            loadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = LoadStatus.Loading;
                    notifyItemChanged(data.size());
                    if (loadMoreListener != null) loadMoreListener.onLoadMore();
                }
            });
        }
    }

    private class HospitalVH extends RecyclerView.ViewHolder {

        HttpImageView imageView;
        TextView nameView;
        TextView addressView;
        TextView distanceView;

        public HospitalVH(View itemView) {
            super(itemView);
            imageView = (HttpImageView) itemView.findViewById(R.id.item_nearby_hospital_img);
            nameView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_name);
            addressView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_address);
            distanceView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_distance);
        }
    }

    public interface OnLoadMoreListener {

        void onLoadMore();

    }
}
