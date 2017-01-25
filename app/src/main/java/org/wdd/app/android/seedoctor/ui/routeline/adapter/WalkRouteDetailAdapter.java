package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.WalkStep;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

/**
 * Created by richard on 12/9/16.
 */

public class WalkRouteDetailAdapter extends AbstractCommonAdapter<WalkStep> {

    public WalkRouteDetailAdapter(Context context, List<WalkStep> data) {
        super(context, data);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, WalkStep item, int position) {
        WalkViewHolder viewHolder = (WalkViewHolder) holder;
        if (position == 0) {
            viewHolder.dirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.lineName.setText(R.string.set_out);
            viewHolder.splitLine.setVisibility(View.INVISIBLE);
        } else if (position == data.size() - 1) {
            viewHolder.dirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.lineName.setText(R.string.reach_the_end);
        } else {
            viewHolder.splitLine.setVisibility(View.VISIBLE);
            String actionName = item.getAction();
            int resID = AMapUtil.getWalkActionID(actionName);
            viewHolder.dirIcon.setImageResource(resID);
            viewHolder.lineName.setText(item.getInstruction());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_route_line, null);
        WalkViewHolder holder = new WalkViewHolder(view);
        return holder;
    }

    private class WalkViewHolder extends RecyclerView.ViewHolder {

        TextView lineName;
        ImageView dirIcon;
        ImageView splitLine;

        public WalkViewHolder(View itemView) {
            super(itemView);
            lineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            dirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            splitLine = (ImageView) itemView.findViewById(R.id.item_route_line_seg_split_line);
        }
    }
}
