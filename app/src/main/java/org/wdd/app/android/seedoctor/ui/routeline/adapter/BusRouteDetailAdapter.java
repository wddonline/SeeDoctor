package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.RailwayStationItem;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.routeline.model.SchemeBusStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdd on 16-12-11.
 */

public class BusRouteDetailAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<SchemeBusStepWrapper> mData;

    public BusRouteDetailAdapter(Context context, List<SchemeBusStep> busStepList) {
        this.mContext = context;
        mData = new ArrayList<>();
        for (SchemeBusStep step : busStepList) {
            mData.add(new SchemeBusStepWrapper(false, step));
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        SchemeBusStep step = mData.get(groupPosition).step;
        if (step.isBus()) {
            return step.getBusLine().getPassStations().size();
        } else if (step.isRailway()) {
            return step.getRailway().getViastops().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        SchemeBusStep step = mData.get(groupPosition).step;
        if (step.isBus()) {
            return step.getBusLine().getPassStations().get(childPosition);
        } else if (step.isRailway()) {
            return step.getRailway().getViastops().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        BusRouteDetailAdapter.BusViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_route_line, null);
            viewHolder = new BusRouteDetailAdapter.BusViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BusViewHolder) convertView.getTag();
        }
        SchemeBusStepWrapper wrapper = mData.get(groupPosition);
        if (groupPosition == 0) {
            viewHolder.busDirUp.setVisibility(View.INVISIBLE);
            viewHolder.busDirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.busDirDown.setVisibility(View.VISIBLE);
            viewHolder.busLineName.setText(R.string.set_out);
            viewHolder.splitLine.setVisibility(View.VISIBLE);
            viewHolder.busStationNum.setVisibility(View.GONE);
            viewHolder.busExpandImage.setVisibility(View.GONE);
        } else if (groupPosition == mData.size() - 1) {
            viewHolder.busDirUp.setVisibility(View.VISIBLE);
            viewHolder.busDirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.busDirDown.setVisibility(View.INVISIBLE);
            viewHolder.busLineName.setText(R.string.reach_the_end);
            viewHolder.splitLine.setVisibility(View.GONE);
            viewHolder.busStationNum.setVisibility(View.INVISIBLE);
            viewHolder.busExpandImage.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.busDirUp.setVisibility(View.VISIBLE);
            viewHolder.busDirDown.setVisibility(View.VISIBLE);
            if (wrapper.step.isWalk() && wrapper.step.getWalk() != null && wrapper.step.getWalk().getDistance() > 0) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir13);
                viewHolder.busLineName.setText(String.format(mContext.getString(R.string.walk_distance_format), (int) wrapper.step.getWalk().getDistance()));
                viewHolder.busStationNum.setVisibility(View.GONE);
                viewHolder.busExpandImage.setVisibility(View.GONE);
            }else if (wrapper.step.isBus() && wrapper.step.getBusLines().size() > 0) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir14);
                viewHolder.busLineName.setText(wrapper.step.getBusLines().get(0).getBusLineName());
                viewHolder.busStationNum.setVisibility(View.VISIBLE);
                viewHolder.busStationNum.setText((String.format(mContext.getString(R.string.station), wrapper.step.getBusLines().get(0).getPassStationNum() + 1)));
                viewHolder.busExpandImage.setVisibility(View.VISIBLE);
                if (wrapper.isExpanded) {
                    viewHolder.busExpandImage.setImageResource(R.mipmap.down);
                    viewHolder.splitLine.setVisibility(View.GONE);
                } else {
                    viewHolder.busExpandImage.setImageResource(R.mipmap.up);
                    viewHolder.splitLine.setVisibility(View.VISIBLE);
                }
            } else if (wrapper.step.isRailway() && wrapper.step.getRailway() != null) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir16);
                viewHolder.busLineName.setText(wrapper.step.getRailway().getName());
                viewHolder.busStationNum.setVisibility(View.VISIBLE);
                viewHolder.busStationNum.setText((String.format(mContext.getString(R.string.station), wrapper.step.getRailway().getViastops().size() + 1)));
                if (wrapper.isExpanded) {
                    viewHolder.busExpandImage.setImageResource(R.mipmap.down);
                    viewHolder.splitLine.setVisibility(View.GONE);
                } else {
                    viewHolder.busExpandImage.setImageResource(R.mipmap.up);
                    viewHolder.splitLine.setVisibility(View.VISIBLE);
                }

            } else if (wrapper.step.isTaxi() && wrapper.step.getTaxi() != null) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir14);
                viewHolder.busLineName.setText(R.string.tax_the_end);
                viewHolder.busStationNum.setVisibility(View.GONE);
                viewHolder.busExpandImage.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        StationViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_bus_segment_ex, null);
            viewHolder = new StationViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StationViewHolder) convertView.getTag();
        }
        viewHolder.stationName = (TextView) convertView.findViewById(R.id.bus_line_station_name);
        SchemeBusStep step = mData.get(groupPosition).step;
        if (step.isBus()) {
            viewHolder.stationName.setText(step.getBusLine().getPassStations().get(childPosition).getBusStationName());
            if (childPosition == step.getBusLine().getPassStations().size() - 1) {
                viewHolder.line.setVisibility(mData.get(groupPosition).isExpanded ? View.VISIBLE : View.GONE);
            } else {
                viewHolder.line.setVisibility(View.GONE);
            }
        } else if (step.isRailway()) {
            RailwayStationItem station = step.getRailway().getViastops().get(childPosition);
            viewHolder.stationName.setText(station.getName()+ " "+getRailwayTime(station.getTime()));
            if (childPosition == step.getRailway().getViastops().size() - 1) {
                viewHolder.line.setVisibility(mData.get(groupPosition).isExpanded ? View.VISIBLE : View.GONE);
            } else {
                viewHolder.line.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public static String getRailwayTime(String time) {
        return time.substring(0, 2) + ":" + time.substring(2, time.length());
    }

    public void toggleExpandStatus(int groupPosition) {
        SchemeBusStepWrapper wrapper = mData.get(groupPosition);
        wrapper.isExpanded = !wrapper.isExpanded;
        notifyDataSetChanged();
    }

    private class SchemeBusStepWrapper {

        boolean isExpanded;
        SchemeBusStep step;

        public SchemeBusStepWrapper(boolean isExpanded, SchemeBusStep step) {
            this.isExpanded = isExpanded;
            this.step = step;
        }

    }

    private class StationViewHolder {

        TextView stationName;
        View line;

        public StationViewHolder(View itemView) {
            stationName = (TextView) itemView.findViewById(R.id.bus_line_station_name);
            line = itemView.findViewById(R.id.bus_line_split_line);
        }
    }

    private class BusViewHolder {

        View busDirUp;
        ImageView busDirIcon;
        View busDirDown;
        TextView busLineName;
        TextView busStationNum;
        ImageView busExpandImage;
        View splitLine;

        public BusViewHolder(View itemView) {
            busDirUp = itemView.findViewById(R.id.bus_dir_icon_up);
            busDirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            busDirDown = itemView.findViewById(R.id.bus_dir_icon_down);
            busLineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            busStationNum = (TextView) itemView.findViewById(R.id.item_route_line_bus_station_num);
            busExpandImage = (ImageView) itemView.findViewById(R.id.item_route_line_bus_expand_image);
            splitLine = itemView.findViewById(R.id.item_route_line_seg_split_line);
        }
    }

}
