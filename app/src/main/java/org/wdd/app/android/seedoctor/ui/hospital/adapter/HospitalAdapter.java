package org.wdd.app.android.seedoctor.ui.hospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.navigation.RouteLineActivity;
import org.wdd.app.android.seedoctor.views.HttpImageView;

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
    private SwipeLayout openSwipeLayout;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
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
            final Hospital hospital = data.get(position);
            HospitalVH hospitalVH = (HospitalVH) holder;
            if (hospital.getImgUrls() != null && hospital.getImgUrls().length > 0) {
                hospitalVH.imageView.setImageUrl(hospital.getImgUrls()[0]);
            } else {
                hospitalVH.imageView.setImageUrl(null);
            }
            if (!TextUtils.isEmpty(hospital.getTypeDes())) {
                hospitalVH.levelView.setVisibility(View.VISIBLE);
                String[] types = hospital.getTypeDes().split(";");
                hospitalVH.levelView.setText(types[types.length - 1]);
            } else {
                hospitalVH.levelView.setVisibility(View.GONE);
            }
            hospitalVH.nameView.setText(hospital.getName());
            hospitalVH.addressView.setText(hospital.getAddress());
            hospitalVH.distanceView.setText(getDistanceDesc(hospital.getDistance()));
            hospitalVH.rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (openSwipeLayout != null) {
                        openSwipeLayout.close(true);
                        return true;
                    }
                    return false;
                }
            });
            hospitalVH.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RouteLineActivity.show(context, hospital.getLatLong().latitude, hospital.getLatLong().longitude);
                }
            });
            hospitalVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    openSwipeLayout = layout;
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {
                    openSwipeLayout = null;
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
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

        SwipeLayout swipeLayout;
        View rootView;
        HttpImageView imageView;
        TextView levelView;
        TextView nameView;
        TextView addressView;
        TextView distanceView;

        public HospitalVH(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_nearby_hospital_swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.item_nearby_hospital_drawer));

            rootView = itemView.findViewById(R.id.item_nearby_hospital_root);
            imageView = (HttpImageView) itemView.findViewById(R.id.item_nearby_hospital_img);
            levelView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_level);
            nameView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_name);
            addressView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_address);
            distanceView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_distance);
        }
    }

    public interface OnLoadMoreListener {

        void onLoadMore();

    }
}
