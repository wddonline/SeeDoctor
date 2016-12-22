package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;

import java.util.List;

public class DrugThirdCategaryAdapter extends AbstractCommonAdapter<DrugCategory> {

    private OnCategoryClickedListener listener;

    public DrugThirdCategaryAdapter(Context context, List<DrugCategory> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final DrugCategory item, final int position) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        if (!TextUtils.isEmpty(item.catname)) {
            viewHolder.nameView.setText(item.catname);
        }
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.onCategoryClicked(position, item);
            }
        });
    }

    public void setOnCategoryClickedListener(OnCategoryClickedListener listener) {
        this.listener = listener;
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView nameView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) itemView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }

    public interface OnCategoryClickedListener {

        void onCategoryClicked(int position, DrugCategory category);

    }
}
