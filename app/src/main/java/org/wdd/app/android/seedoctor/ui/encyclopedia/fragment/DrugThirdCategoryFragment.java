package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugListActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.DrugSecondCategaryAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.DrugThirdCategaryAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;

import java.util.List;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DrugThirdCategoryFragment extends BaseFragment implements DrugThirdCategaryAdapter.OnCategoryClickedListener {

    private View rootView;
    private List<DrugCategory> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drug_third_category, container, false);
        initData();
        intView();
        return rootView;
    }

    private void initData() {
        categories = (List<DrugCategory>) getArguments().getSerializable("categories");
    }

    private void intView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_drug_third_category_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));

        DrugThirdCategaryAdapter adapter = new DrugThirdCategaryAdapter(getContext(), categories);
        adapter.setOnCategoryClickedListener(this);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCategoryClicked(int position, DrugCategory category) {
        WikiDrugListActivity.show(getContext(), category.catid);
    }
}
