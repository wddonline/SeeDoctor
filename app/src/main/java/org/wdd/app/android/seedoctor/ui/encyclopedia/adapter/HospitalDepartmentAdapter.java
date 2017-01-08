package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.DepartmentDetailActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalSecondDepartmentAdtivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;

import java.util.List;

/**
 * Created by wangdd on 17-1-7.
 */

public class HospitalDepartmentAdapter extends AbstractCommonAdapter<Department> {

    public enum DepartmentCategory {
        FIRST,
        SECOND
    }

    private DepartmentCategory category;

    public HospitalDepartmentAdapter(Context context, List<Department> data, DepartmentCategory category) {
        super(context, data);
        this.category = category;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
        HospitalDepartmentViewHolder viewHolder = new HospitalDepartmentViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, final Department item, int position) {
        HospitalDepartmentViewHolder viewHolder = (HospitalDepartmentViewHolder) holder;
        viewHolder.nameView.setText(item.departmentname);
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (category) {
                    case FIRST:
                        HospitalSecondDepartmentAdtivity.show(context, item.hospitalid, item.hospitalname, item.departmentid, item.departmentname);
                        break;
                    case SECOND:
                        DepartmentDetailActivity.show(context, item.departmentid, item.departmentname);
                        break;
                }
            }
        });
    }

    private class HospitalDepartmentViewHolder extends RecyclerView.ViewHolder {
        View clickView;
        TextView nameView;

        public HospitalDepartmentViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) itemView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }
}
