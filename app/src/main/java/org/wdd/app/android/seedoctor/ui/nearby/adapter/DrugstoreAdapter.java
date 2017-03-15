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

public class DrugstoreAdapter extends AbstractCommonAdapter<Mark> {

    private Activity activity;
    private SwipeLayout openSwipeLayout;

    public DrugstoreAdapter(Activity activity, List<Mark> data) {
        super(activity, data);
        this.activity = activity;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nearby_drugstore, parent, false);
        RecyclerView.ViewHolder viewHolder = new DrugstoreVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Mark drugstore, int position) {
        final DrugstoreVH drugstoreVH = (DrugstoreVH) holder;
        if (drugstore.getImgUrls() != null && drugstore.getImgUrls().length > 0) {
            drugstoreVH.imageView.setImageUrl(drugstore.getImgUrls()[0]);
        } else {
            drugstoreVH.imageView.setImageUrl(null);
        }
        drugstoreVH.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drugstore.getImgUrls() != null && drugstore.getImgUrls().length > 0) {
                    ImageBrowserActivity.show(activity, v, drugstore.getImgUrls());
                } else {
                    drugstoreVH.rootView.performClick();
                }
            }
        });
        drugstoreVH.nameView.setText(drugstore.getName());
        drugstoreVH.addressView.setText(drugstore.getAddress());
        drugstoreVH.distanceView.setText(getDistanceDesc(drugstore.getDistance()));
        drugstoreVH.rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openSwipeLayout != null) {
                    openSwipeLayout.close(true);
                    return true;
                }
                return false;
            }
        });
        drugstoreVH.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteLineActivity.show(context, drugstore.getLatLong().latitude, drugstore.getLatLong().longitude);
            }
        });
        drugstoreVH.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
        drugstoreVH.callView.setEnabled(!TextUtils.isEmpty(drugstore.getTelephone()));
        drugstoreVH.callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + drugstore.getTelephone()));
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

    private class DrugstoreVH extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        View rootView;
        NetworkImageView imageView;
        TextView nameView;
        TextView addressView;
        TextView distanceView;
        View callView;

        public DrugstoreVH(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_nearby_drugstore_swipe);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.item_nearby_drugstore_drawer));

            rootView = itemView.findViewById(R.id.item_nearby_drugstore_root);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_nearby_drugstore_img);
            nameView = (TextView) itemView.findViewById(R.id.item_nearby_drugstore_name);
            addressView = (TextView) itemView.findViewById(R.id.item_nearby_drugstore_address);
            distanceView = (TextView) itemView.findViewById(R.id.item_nearby_drugstore_distance);
            callView = itemView.findViewById(R.id.item_nearby_drugstore_call);
        }
    }
}
