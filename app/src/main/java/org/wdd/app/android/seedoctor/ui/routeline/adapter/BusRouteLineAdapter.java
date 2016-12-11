package org.wdd.app.android.seedoctor.ui.routeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.utils.AMapUtil;

import java.util.List;

/**
 * Created by richard on 12/1/16.
 */

public class BusRouteLineAdapter extends AbstractCommonAdapter<BusPath> {

    private OnItemClickedListener listener;

    public BusRouteLineAdapter(Context context, List<BusPath> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_route_line, parent, false);
        RecyclerView.ViewHolder viewHolder = new BusLineViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final BusPath item, final int position) {
        BusLineViewHolder busLineViewHolder = (BusLineViewHolder) holder;
        busLineViewHolder.nameView .setText(AMapUtil.getBusPathTitle(item));
        busLineViewHolder.descView.setText(AMapUtil.getBusPathDes(item));
        busLineViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.onItemClicked(position, item);
            }
        });
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    private class BusLineViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView nameView;
        private TextView descView;

        public BusLineViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.item_bus_route_line_root);
            nameView = (TextView) itemView.findViewById(R.id.item_bus_route_line_name);
            descView = (TextView) itemView.findViewById(R.id.item_bus_route_line_desc);
        }
    }

    public interface OnItemClickedListener {

        void onItemClicked(int position, BusPath path);

    }
}
