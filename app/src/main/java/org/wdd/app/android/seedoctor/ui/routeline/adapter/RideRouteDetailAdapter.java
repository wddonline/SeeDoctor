package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.RideStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

/**
 * Created by richard on 12/9/16.
 */

public class RideRouteDetailAdapter extends AbstractCommonAdapter<RideStep> {

    public RideRouteDetailAdapter(Context context, List<RideStep> data) {
        super(context, data);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, RideStep item, int position) {
        WalkViewHolder viewHolder = (WalkViewHolder) holder;
        if (position == 0) {
            viewHolder.walkDirUp.setVisibility(View.INVISIBLE);
            viewHolder.dirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.walkDirDown.setVisibility(View.VISIBLE);
            viewHolder.lineName.setText(R.string.set_out);
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        } else if (position == data.size() - 1) {
            viewHolder.walkDirUp.setVisibility(View.VISIBLE);
            viewHolder.dirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.walkDirDown.setVisibility(View.INVISIBLE);
            viewHolder.lineName.setText(R.string.reach_the_end);
            viewHolder.splitLine.setVisibility(View.GONE);
        } else {
            String actionName = item.getAction();
            viewHolder.walkDirUp.setVisibility(View.VISIBLE);
            int resID = AMapUtil.getWalkActionID(actionName);
            viewHolder.dirIcon.setImageResource(resID);
            viewHolder.walkDirDown.setVisibility(View.VISIBLE);
            viewHolder.lineName.setText(item.getInstruction());
            viewHolder.splitLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_route_line, null);
        WalkViewHolder holder = new WalkViewHolder(view);
        return holder;
    }

    private class WalkViewHolder extends RecyclerView.ViewHolder {

        View walkDirUp;
        ImageView dirIcon;
        View walkDirDown;
        TextView lineName;
        View splitLine;

        public WalkViewHolder(View itemView) {
            super(itemView);
            walkDirUp = itemView.findViewById(R.id.bus_dir_icon_up);
            lineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            walkDirDown = itemView.findViewById(R.id.bus_dir_icon_down);
            dirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            splitLine = itemView.findViewById(R.id.item_route_line_seg_split_line);
        }
    }
}
