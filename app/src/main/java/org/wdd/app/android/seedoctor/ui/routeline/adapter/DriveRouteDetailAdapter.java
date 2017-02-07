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

public class DriveRouteDetailAdapter extends AbstractCommonAdapter<DriveStep> {

    public DriveRouteDetailAdapter(Context context, List<DriveStep> steps) {
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
            viewHolder.driveDirUp.setVisibility(View.INVISIBLE);
            viewHolder.driveDirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.driveDirDown.setVisibility(View.VISIBLE);
            viewHolder.driveLineName.setText("出发");
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        } else if (position == data.size() - 1) {
            viewHolder.driveDirUp.setVisibility(View.VISIBLE);
            viewHolder.driveDirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.driveDirDown.setVisibility(View.INVISIBLE);
            viewHolder.driveLineName.setText("到达终点");
            viewHolder.splitLine.setVisibility(View.GONE);
        } else {
            viewHolder.driveDirUp.setVisibility(View.VISIBLE);
            String actionName = item.getAction();
            int resID = AMapUtil.getDriveActionID(actionName);
            viewHolder.driveDirIcon.setImageResource(resID);
            viewHolder.driveDirDown.setVisibility(View.VISIBLE);
            viewHolder.driveLineName.setText(item.getInstruction());
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        }
    }

    private class DriveRouteViewHolder extends RecyclerView.ViewHolder {
        View driveDirUp;
        ImageView driveDirIcon;
        View driveDirDown;
        TextView driveLineName;
        View splitLine;

        public DriveRouteViewHolder(View itemView) {
            super(itemView);
            driveDirUp = itemView.findViewById(R.id.bus_dir_icon_up);
            driveDirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            driveDirDown = itemView.findViewById(R.id.bus_dir_icon_down);
            driveLineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            splitLine = itemView.findViewById(R.id.item_route_line_seg_split_line);
        }
    }
}
