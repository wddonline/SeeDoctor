package org.wdd.app.android.seedoctor.ui.nearby.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.gallery.activity.ImageBrowserActivity;
import org.wdd.app.android.seedoctor.ui.nearby.model.Mark;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class HospitalAdapter extends AbstractCommonAdapter<Mark> {

    private Activity activity;
    private SwipeLayout openSwipeLayout;

    public HospitalAdapter(Activity activity, List<Mark> data) {
        super(activity, data);
        this.activity = activity;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nearby_hospital, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(final RecyclerView.ViewHolder holder, final Mark hospital, int position) {
        final HospitalVH hospitalVH = (HospitalVH) holder;
        if (hospital.getImgUrls() != null && hospital.getImgUrls().length > 0) {
            hospitalVH.imageView.setImageUrl(hospital.getImgUrls()[0]);
        } else {
            hospitalVH.imageView.setImageUrl(null);
        }
        hospitalVH.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hospital.getImgUrls() != null && hospital.getImgUrls().length > 0) {
                    ImageBrowserActivity.show(activity, v, hospital.getImgUrls());
                } else {
                    hospitalVH.rootView.performClick();
                }
            }
        });
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
        hospitalVH.callView.setEnabled(!TextUtils.isEmpty(hospital.getTelephone()));
        hospitalVH.callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + hospital.getTelephone()));
                context.startActivity(intent);
            }
        });
    }

    private String getDistanceDesc(long distance) {
        if (distance > 1000) {
            return String.format(context.getString(R.string.distance_desc_km), String.format("%.2f", distance / 1000f));
        } else {
            return String.format(context.getString(R.string.distance_desc_m), distance);
        }
    }

    private class HospitalVH extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View rootView;
        NetworkImageView imageView;
        TextView levelView;
        TextView nameView;
        TextView addressView;
        TextView distanceView;
        View callView;

        public HospitalVH(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_nearby_hospital_swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.item_nearby_hospital_drawer));

            rootView = itemView.findViewById(R.id.item_nearby_hospital_root);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_nearby_hospital_img);
            levelView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_level);
            nameView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_name);
            addressView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_address);
            distanceView = (TextView) itemView.findViewById(R.id.item_nearby_hospital_distance);
            callView = itemView.findViewById(R.id.item_nearby_hospital_call);
        }
    }
}
