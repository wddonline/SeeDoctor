package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.EmergencyDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiEmergencyAdapter extends AbstractCommonAdapter<Emergency> {

    public WikiEmergencyAdapter(Context context, List<Emergency> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
        EmergencyViewHolder viewHolder = new EmergencyViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Emergency item, int position) {
        EmergencyViewHolder viewHolder = (EmergencyViewHolder) holder;
        viewHolder.nameView.setText(item.eme);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmergencyDetailActivity.show(context, item.emeid, item.eme);
            }
        });
    }

    private class EmergencyViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView nameView;

        public EmergencyViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) itemView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }
}
