package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;
import org.wdd.app.android.seedoctor.views.HttpImageView;

import java.util.List;

/**
 * Created by richard on 1/5/17.
 */

public class WikiHospitalAdapter extends AbstractCommonAdapter<Hospital> {

    public WikiHospitalAdapter(Context context, List<Hospital> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_wiki_hospital_list, null);
        HospitalViewHolder viewHolder = new HospitalViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Hospital item, int position) {
        HospitalViewHolder viewHolder = (HospitalViewHolder) holder;
        viewHolder.imageView.setImageUrl(item.picurl);
        viewHolder.levelView.setText(item.levelname);
        viewHolder.nameView.setText(item.hospitalname);
        viewHolder.descView.setText(item.address);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HospitalDetailActivity.show(context, item.hospitalid, item.hospitalname);
            }
        });
    }

    private class HospitalViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        HttpImageView imageView;
        TextView levelView;
        TextView nameView;
        TextView descView;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_wiki_hospital_list_click);
            imageView = (HttpImageView) itemView.findViewById(R.id.item_wiki_hospital_list_img);
            levelView = (TextView) itemView.findViewById(R.id.item_wiki_hospital_list_level);
            nameView = (TextView) itemView.findViewById(R.id.item_wiki_hospital_list_name);
            descView = (TextView) itemView.findViewById(R.id.item_wiki_hospital_list_desc);
        }
    }

}
