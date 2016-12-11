package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.RailwayStationItem;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.routeline.model.SchemeBusStep;

import java.util.List;

/**
 * Created by wangdd on 16-12-11.
 */

public class BusRouteDetailAdapter extends AbstractCommonAdapter<SchemeBusStep> {

    public BusRouteDetailAdapter(Context context, List<SchemeBusStep> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_route_line, null);
        BusRouteDetailAdapter.BusViewHolder holder = new BusRouteDetailAdapter.BusViewHolder(view);
        return holder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, SchemeBusStep item, int position) {
        BusRouteDetailAdapter.BusViewHolder viewHolder = (BusRouteDetailAdapter.BusViewHolder) holder;
        if (position == 0) {
            viewHolder.busDirIcon.setImageResource(R.mipmap.dir_start);
            viewHolder.busLineName.setText(R.string.set_out);
            viewHolder.busDirUp.setVisibility(View.INVISIBLE);
            viewHolder.busDirDown.setVisibility(View.VISIBLE);
            viewHolder.splitLine.setVisibility(View.GONE);
            viewHolder.busStationNum.setVisibility(View.GONE);
            viewHolder.busExpandImage.setVisibility(View.GONE);
        } else if (position == data.size() - 1) {
            viewHolder.busDirIcon.setImageResource(R.mipmap.dir_end);
            viewHolder.busLineName.setText(R.string.reach_the_end);
            viewHolder.busDirUp.setVisibility(View.VISIBLE);
            viewHolder.busDirDown.setVisibility(View.INVISIBLE);
            viewHolder.busStationNum.setVisibility(View.INVISIBLE);
            viewHolder.busExpandImage.setVisibility(View.INVISIBLE);
        } else {
            if (item.isWalk() && item.getWalk() != null && item.getWalk().getDistance() > 0) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir13);
                viewHolder.busDirUp.setVisibility(View.VISIBLE);
                viewHolder.busDirDown.setVisibility(View.VISIBLE);
                viewHolder.busLineName.setText(String.format(context.getString(R.string.walk_distance_format),
                        (int) item.getWalk().getDistance()));
                viewHolder.busStationNum.setVisibility(View.GONE);
                viewHolder.busExpandImage.setVisibility(View.GONE);
            }else if (item.isBus() && item.getBusLines().size() > 0) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir14);
                viewHolder.busDirUp.setVisibility(View.VISIBLE);
                viewHolder.busDirDown.setVisibility(View.VISIBLE);
                viewHolder.busLineName.setText(item.getBusLines().get(0).getBusLineName());
                viewHolder.busStationNum.setVisibility(View.VISIBLE);
                viewHolder.busStationNum.setText((String.format(context.getString(R.string.station),
                        item.getBusLines().get(0).getPassStationNum() + 1)));
                viewHolder.busExpandImage.setVisibility(View.VISIBLE);
                ArrowClick arrowClick = new ArrowClick(viewHolder, item);
                viewHolder.parent.setTag(position);
                viewHolder.parent.setOnClickListener(arrowClick);
            } else if (item.isRailway() && item.getRailway() != null) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir16);
                viewHolder.busDirUp.setVisibility(View.VISIBLE);
                viewHolder.busDirDown.setVisibility(View.VISIBLE);
                viewHolder.busLineName.setText(item.getRailway().getName());
                viewHolder.busStationNum.setVisibility(View.VISIBLE);
                viewHolder.busStationNum.setText((String.format(context.getString(R.string.station),
                        item.getRailway().getViastops().size() + 1)));
                viewHolder.busExpandImage.setVisibility(View.VISIBLE);
                ArrowClick arrowClick = new ArrowClick(viewHolder, item);
                viewHolder.parent.setTag(position);
                viewHolder.parent.setOnClickListener(arrowClick);
            } else if (item.isTaxi() && item.getTaxi() != null) {
                viewHolder.busDirIcon.setImageResource(R.mipmap.dir14);
                viewHolder.busDirUp.setVisibility(View.VISIBLE);
                viewHolder.busDirDown.setVisibility(View.VISIBLE);
                viewHolder.busLineName.setText(R.string.tax_the_end);
                viewHolder.busStationNum.setVisibility(View.GONE);
                viewHolder.busExpandImage.setVisibility(View.GONE);
            }
        }
    }

    private class BusViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parent;
        TextView busLineName;
        ImageView busDirIcon;
        TextView busStationNum;
        ImageView busExpandImage;
        ImageView busDirUp;
        ImageView busDirDown;
        ImageView splitLine;
        LinearLayout expandContent;
        boolean arrowExpend = false;

        public BusViewHolder(View itemView) {
            super(itemView);
            parent = (RelativeLayout) itemView.findViewById(R.id.item_route_line_bus_item);
            busLineName = (TextView) itemView.findViewById(R.id.item_route_line_bus_line_name);
            busDirIcon = (ImageView) itemView.findViewById(R.id.item_route_line_dir);
            busStationNum = (TextView) itemView.findViewById(R.id.item_route_line_bus_station_num);
            busExpandImage = (ImageView) itemView.findViewById(R.id.item_route_line_bus_expand_image);
            busDirUp = (ImageView) itemView.findViewById(R.id.item_route_line_dir_up);
            busDirDown = (ImageView) itemView.findViewById(R.id.item_route_line_dir_down);
            splitLine = (ImageView) itemView.findViewById(R.id.item_route_line_seg_split_line);
            expandContent = (LinearLayout) itemView.findViewById(R.id.item_route_line_expand_content);
        }
    }

    private class ArrowClick implements View.OnClickListener {
        private BusViewHolder mHolder;
        private SchemeBusStep mItem;

        public ArrowClick(final BusViewHolder holder, final SchemeBusStep item) {
            mHolder = holder;
            mItem = item;
        }

        @Override
        public void onClick(View v) {
            int position = Integer.parseInt(String.valueOf(v.getTag()));
            mItem = data.get(position);
            if (mItem.isBus()) {
                if (mHolder.arrowExpend == false) {
                    mHolder.arrowExpend = true;
                    mHolder.busExpandImage
                            .setImageResource(R.mipmap.up);
                    addBusStation(mItem.getBusLine().getDepartureBusStation());
                    for (BusStationItem station : mItem.getBusLine()
                            .getPassStations()) {
                        addBusStation(station);
                    }
                    addBusStation(mItem.getBusLine().getArrivalBusStation());

                } else {
                    mHolder.arrowExpend = false;
                    mHolder.busExpandImage
                            .setImageResource(R.mipmap.down);
                    mHolder.expandContent.removeAllViews();
                }
            } else if (mItem.isRailway()) {
                if (mHolder.arrowExpend == false) {
                    mHolder.arrowExpend = true;
                    mHolder.busExpandImage
                            .setImageResource(R.mipmap.up);
                    addRailwayStation(mItem.getRailway().getDeparturestop());
                    for (RailwayStationItem station : mItem.getRailway().getViastops()) {
                        addRailwayStation(station);
                    }
                    addRailwayStation(mItem.getRailway().getArrivalstop());

                } else {
                    mHolder.arrowExpend = false;
                    mHolder.busExpandImage
                            .setImageResource(R.mipmap.down);
                    mHolder.expandContent.removeAllViews();
                }
            }


        }

        private void addBusStation(BusStationItem station) {
            LinearLayout ll = (LinearLayout) View.inflate(context, R.layout.item_bus_segment_ex, null);
            TextView tv = (TextView) ll.findViewById(R.id.bus_line_station_name);
            tv.setText(station.getBusStationName());
            mHolder.expandContent.addView(ll);
        }

        private void addRailwayStation(RailwayStationItem station) {
            LinearLayout ll = (LinearLayout) View.inflate(context, R.layout.item_bus_segment_ex, null);
            TextView tv = (TextView) ll.findViewById(R.id.bus_line_station_name);
            tv.setText(station.getName()+ " "+getRailwayTime(station.getTime()));
            mHolder.expandContent.addView(ll);
        }
    }

    public static String getRailwayTime(String time) {
        return time.substring(0, 2) + ":" + time.substring(2, time.length());
    }

}
