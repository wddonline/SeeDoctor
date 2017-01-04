package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DoctorDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.views.HttpImageView;

import java.util.List;

/**
 * Created by richard on 1/4/17.
 */

public class WikiDoctorAdapter extends AbstractCommonAdapter<Doctor> {

    public WikiDoctorAdapter(Context context, List<Doctor> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_wiki_doctor_list, null);
        DoctorViewHolder viewHolder = new DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Doctor item, int position) {
        DoctorViewHolder viewHolder = (DoctorViewHolder) holder;
        viewHolder.imageView.setImageUrl(item.photourl);
        viewHolder.nameView.setText(item.doctorname);
        viewHolder.levelView.setText(item.doclevelname);
        viewHolder.descView.setText(item.feature);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorDetailActivity.show(context, item.doctorid);
            }
        });
    }

    private class DoctorViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        HttpImageView imageView;
        TextView nameView;
        TextView levelView;
        TextView descView;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_wiki_doctor_list_click);
            imageView = (HttpImageView) itemView.findViewById(R.id.item_wiki_doctor_list_img);
            nameView = (TextView) itemView.findViewById(R.id.item_wiki_doctor_list_name);
            levelView = (TextView) itemView.findViewById(R.id.item_wiki_doctor_list_level);
            descView = (TextView) itemView.findViewById(R.id.item_wiki_doctor_list_desc);
        }
    }
}
