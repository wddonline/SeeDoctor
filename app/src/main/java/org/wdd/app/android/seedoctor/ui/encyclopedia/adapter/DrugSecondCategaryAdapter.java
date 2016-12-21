package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;

import java.util.List;

public class DrugSecondCategaryAdapter extends AbstractCommonAdapter<String> {

    public DrugSecondCategaryAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_list, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, String item, int position) {

    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public View clickView;
        public TextView nameView;

        public ViewHolder(View view) {
            super(view);
            clickView = view.findViewById(R.id.item_drug_second_categary_list_click);
            nameView = (TextView) view.findViewById(R.id.item_drug_second_categary_list_name);
        }

    }
}
