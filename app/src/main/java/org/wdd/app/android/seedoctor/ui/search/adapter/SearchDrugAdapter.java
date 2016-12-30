package org.wdd.app.android.seedoctor.ui.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DrugDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class SearchDrugAdapter extends AbstractCommonAdapter<Drug> {

    public SearchDrugAdapter(Context context, List<Drug> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
        DiseaseViewHolder viewHolder = new DiseaseViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Drug item, int position) {
        DiseaseViewHolder viewHolder = (DiseaseViewHolder) holder;
        viewHolder.nameView.setText(item.drugname);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrugDetailActivity.show(context, item.drugid, item.drugname);
            }
        });
    }

    private class DiseaseViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView nameView;

        public DiseaseViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) itemView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }
}
