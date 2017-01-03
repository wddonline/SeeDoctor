package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DepartmentDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;

import java.util.List;

/**
 * Created by richard on 12/19/16.
 */

public class WikiDepartmentAdapter extends AbstractCommonAdapter<Department> {

    public WikiDepartmentAdapter(Context context, List<Department> data) {
        super(context, data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
        DepartmentViewHolder viewHolder = new DepartmentViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Department item, int position) {
        DepartmentViewHolder viewHolder = (DepartmentViewHolder) holder;
        viewHolder.nameView.setText(item.departmentname);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepartmentDetailActivity.show(context, item.departmentid, item.departmentname);
            }
        });
    }

    private class DepartmentViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView nameView;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) itemView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }
}
