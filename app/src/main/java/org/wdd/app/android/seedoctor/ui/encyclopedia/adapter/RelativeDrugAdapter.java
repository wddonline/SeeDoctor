package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DrugDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.views.HttpImageView;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class RelativeDrugAdapter extends AbstractCommonAdapter<Drug> {

    public RelativeDrugAdapter(Context context, List<Drug> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_relative_drug_list, null);
        DrugViewHolder viewHolder = new DrugViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Drug item, int position) {
        DrugViewHolder viewHolder = (DrugViewHolder) holder;
        viewHolder.imageView.setImageUrl(item.picurl);
        viewHolder.nameView.setText(item.drugname);
        viewHolder.sellerView.setText(item.companyname);
        viewHolder.descView.setText(item.indication);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrugDetailActivity.show(context, item.drugid, item.drugname);
            }
        });
    }

    private class DrugViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        HttpImageView imageView;
        TextView nameView;
        TextView sellerView;
        TextView descView;

        public DrugViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_relative_drug_list_click);
            imageView = (HttpImageView) itemView.findViewById(R.id.item_relative_drug_list_img);
            nameView = (TextView) itemView.findViewById(R.id.item_relative_drug_list_name);
            sellerView = (TextView) itemView.findViewById(R.id.item_relative_drug_list_seller);
            descView = (TextView) itemView.findViewById(R.id.item_relative_drug_list_desc);
        }
    }
}
