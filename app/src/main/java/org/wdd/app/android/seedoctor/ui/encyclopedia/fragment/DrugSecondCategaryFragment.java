package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.DrugSecondCategaryAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class DrugSecondCategaryFragment extends BaseFragment implements DrugSecondCategaryAdapter.OnCategoryClickedListener {

    private View rootView;
    private ExpandableListView listView;

    private List<String> categoriesList;
    private Map<String, List<DrugCategory>> categoriesMap;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drug_second_categary, container, false);
        initData();
        initView();
        return rootView;
    }

    private void initData() {
        categoriesMap = (Map<String, List<DrugCategory>>) getArguments().getSerializable("categories");
        categoriesList = new ArrayList<>();
        Set<String> set = categoriesMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            categoriesList.add(iterator.next());
        }
    }

    private void initView() {
        listView = (ExpandableListView) rootView.findViewById(R.id.fragment_drug_second_categary_listview);
        listView.setGroupIndicator(null);
        DrugSecondCategaryAdapter adapter = new DrugSecondCategaryAdapter(getContext(), categoriesList, categoriesMap);
        adapter.setOnCategoryClickedListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onGroupClicked(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            listView.collapseGroup(groupPosition);
        } else {
            listView.expandGroup(groupPosition);
        }
    }

    @Override
    public void onChildClicked(int groupPosition, int childPosition, DrugCategory category) {
        WikiDrugListActivity.show(getContext(), category.catid);
    }
}
