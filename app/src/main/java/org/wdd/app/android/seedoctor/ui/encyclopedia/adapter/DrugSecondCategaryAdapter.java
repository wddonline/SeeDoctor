package org.wdd.app.android.seedoctor.ui.encyclopedia.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;

import java.util.List;
import java.util.Map;

public class DrugSecondCategaryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groups;
    private Map<String, List<DrugCategory>> childs;
    private OnCategoryClickedListener listener;

    public DrugSecondCategaryAdapter(Context context, List<String> groups, Map<String, List<DrugCategory>> childs) {
        this.context = context;
        this.groups = groups;
        this.childs = childs;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groups.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groups.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_drug_second_categary_group_list, null);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        String name = (String) getGroup(groupPosition);
        viewHolder.nameView.setText(name);
        viewHolder.arrowView.setImageResource(isExpanded ? R.mipmap.group_arrow_down : R.mipmap.group_arrow_right);
        viewHolder.numberView.setText(childs.get(name).size() + "");
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.onGroupClicked(groupPosition, isExpanded);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_drug_second_categary_child_list, null);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        final DrugCategory category = (DrugCategory) getChild(groupPosition, childPosition);
        if (!TextUtils.isEmpty(category.catname)) {
            viewHolder.nameView.setText(category.catname);
        }
        viewHolder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.onChildClicked(groupPosition, childPosition, category);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setOnCategoryClickedListener(OnCategoryClickedListener listener) {
        this.listener = listener;
    }

    private class GroupViewHolder {

        View clickView;
        TextView nameView;
        ImageView arrowView;
        TextView numberView;

        public GroupViewHolder(View convertView) {
            clickView = convertView.findViewById(R.id.item_drug_second_categary_group_list_click);
            nameView = (TextView) convertView.findViewById(R.id.item_drug_second_categary_group_list_name);
            arrowView = (ImageView) convertView.findViewById(R.id.item_drug_second_categary_group_list_arrow);
            numberView = (TextView) convertView.findViewById(R.id.item_drug_second_categary_group_list_num);
        }
    }

    private class ChildViewHolder {

        View clickView;
        TextView nameView;

        public ChildViewHolder(View convertView) {
            clickView = convertView.findViewById(R.id.item_drug_second_categary_child_list_click);
            nameView = (TextView) convertView.findViewById(R.id.item_drug_second_categary_child_list_name);
        }
    }

    public interface OnCategoryClickedListener {

        void onGroupClicked(int groupPosition, boolean isExpanded);
        void onChildClicked(int groupPosition, int childPosition, DrugCategory category);

    }
}
