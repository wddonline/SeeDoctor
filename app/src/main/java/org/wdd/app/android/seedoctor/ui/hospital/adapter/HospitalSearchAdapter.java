package org.wdd.app.android.seedoctor.ui.hospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.routeline.activity.RouteLineActivity;

import java.util.List;

/**
 * Created by richard on 12/5/16.
 */
public class HospitalSearchAdapter extends AbstractCommonAdapter<Hospital> {

    public HospitalSearchAdapter(Context context, List<Hospital> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hospital_search, parent, false);
        RecyclerView.ViewHolder viewHolder = new HospitalViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Hospital item, int position) {
        HospitalViewHolder viewHolder = (HospitalViewHolder) holder;
        viewHolder.titleView.setText(item.getName());
        String[] types = item.getTypeDes().split(";");
        viewHolder.levelView.setText(types[types.length - 1]);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteLineActivity.show(context, item.getLatLong().latitude, item.getLatLong().longitude);
            }
        });
    }

    private class HospitalViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView titleView;
        TextView levelView;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_hospital_search_click);
            titleView = (TextView) itemView.findViewById(R.id.item_hospital_search_title);
            levelView = (TextView) itemView.findViewById(R.id.item_hospital_search_level);
        }
    }
}