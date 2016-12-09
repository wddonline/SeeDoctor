package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.DriveStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

/**
 * Created by wangdd on 16-12-3.
 */

public class DriveRouteAdapter extends AbstractCommonAdapter<DriveStep> {

    public DriveRouteAdapter(Context context, List<DriveStep> steps) {
        super(context, steps);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_route_line, parent, false);
        DriveRouteViewHolder viewHolder = new DriveRouteViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, DriveStep item, int position) {
        DriveRouteViewHolder viewHolder = (DriveRouteViewHolder) holder;
        if (position == 0) {
            viewHolder.driveDirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.driveLineName.setText("出发");
            viewHolder.driveDirUp.setVisibility(View.GONE);
            viewHolder.driveDirDown.setVisibility(View.VISIBLE);
            viewHolder.splitLine.setVisibility(View.GONE);
        } else if (position == data.size() - 1) {
            viewHolder.driveDirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.driveLineName.setText("到达终点");
            viewHolder.driveDirUp.setVisibility(View.VISIBLE);
            viewHolder.driveDirDown.setVisibility(View.GONE);
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        } else {
            String actionName = item.getAction();
            int resID = AMapUtil.getDriveActionID(actionName);
            viewHolder.driveDirIcon.setImageResource(resID);
            viewHolder.driveLineName.setText(item.getInstruction());
            viewHolder.driveDirUp.setVisibility(View.VISIBLE);
            viewHolder.driveDirDown.setVisibility(View.VISIBLE);
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        }
    }

    private class DriveRouteViewHolder extends RecyclerView.ViewHolder {
        TextView driveLineName;
        ImageView driveDirIcon;
        ImageView driveDirUp;
        ImageView driveDirDown;
        ImageView splitLine;

        public DriveRouteViewHolder(View itemView) {
            super(itemView);
            driveDirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            driveLineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            driveDirUp = (ImageView) itemView.findViewById(R.id.item_route_line_dir_up);
            driveDirDown = (ImageView) itemView.findViewById(R.id.item_route_line_dir_down);
            splitLine = (ImageView) itemView.findViewById(R.id.item_route_line_seg_split_line);
        }
    }
}
