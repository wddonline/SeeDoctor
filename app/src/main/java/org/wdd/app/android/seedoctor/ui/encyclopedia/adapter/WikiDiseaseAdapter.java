package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DiseaseDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.views.NetworkImageView;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDiseaseAdapter extends AbstractCommonAdapter<Disease> {

    public WikiDiseaseAdapter(Context context, List<Disease> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_wiki_disease_list, null);
        DiseaseViewHolder viewHolder = new DiseaseViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Disease item, int position) {
        DiseaseViewHolder viewHolder = (DiseaseViewHolder) holder;
        viewHolder.imageView.setImageUrl(item.diseasepicurl);
        viewHolder.nameView.setText(item.diseasename);
        viewHolder.descView.setText(item.introduction.trim());
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiseaseDetailActivity.show(context, item.diseaseid, item.diseasename);
            }
        });
    }

    private class DiseaseViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        NetworkImageView imageView;
        TextView nameView;
        TextView descView;

        public DiseaseViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_wiki_disease_list_click);
            imageView = (NetworkImageView) itemView.findViewById(R.id.item_wiki_disease_list_img);
            nameView = (TextView) itemView.findViewById(R.id.item_wiki_disease_list_name);
            descView = (TextView) itemView.findViewById(R.id.item_wiki_disease_list_desc);
        }
    }
}
