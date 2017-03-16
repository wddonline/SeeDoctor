package org.wdd.app.android.seedoctor.ui.search.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.ui.search.adapter.SearchDrugAdapter;
import org.wdd.app.android.seedoctor.ui.search.data.DiseaseSearchGetter;
import org.wdd.app.android.seedoctor.ui.search.data.NearbySearchGetter;
import org.wdd.app.android.seedoctor.ui.search.presenter.DrugSearchPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class DrugSearchActivity extends BaseActivity implements AbstractCommonAdapter.OnLoadMoreListener {

    private static final String SHARED_NAME = "search_view";

    public static void show(Activity activity, View sharedView) {
        Intent intent = new Intent(activity, DrugSearchActivity.class);
        ViewCompat.setTransitionName(sharedView, SHARED_NAME);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, SHARED_NAME).toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

    private RecyclerView recyclerView;
    private EditText inputView;

    private DrugSearchPresenter presenter;
    private SearchDrugAdapter adapter;
    private List<Drug> drugs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_search);
        initData();
        initViews();
    }

    private void initData() {
        presenter = new DrugSearchPresenter(host, this);
    }

    private void initViews() {
        ViewCompat.setTransitionName(findViewById(R.id.activity_drug_search_input_layout), SHARED_NAME);
        inputView = (EditText) findViewById(R.id.activity_drug_search_input);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if (TextUtils.isEmpty(name)) {
                    presenter.cancelRequest();
                    if (adapter == null) return;
                    drugs.clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
                    return;
                }
                presenter.searchDrugByName(name, true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_drug_search_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void showDiseaseDataView(List<Drug> data, boolean refresh) {
        if (adapter == null) {
            drugs = new ArrayList<>();
            drugs.addAll(data);
            adapter = new SearchDrugAdapter(getBaseContext(), drugs);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            if (data.size() < NearbySearchGetter.PAGEZISE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
        }
        if (refresh) {
            drugs.clear();
            drugs.addAll(data);
        } else {
            drugs.addAll(data);
        }
        adapter.notifyDataSetChanged();
        if (data.size() < DiseaseSearchGetter.PAGE_SISE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }

    public void handleRequestErrorViews(LoadView.LoadStatus status, boolean refresh) {
        switch (status) {
            case Network_Error:
                break;
            case Request_Failure:
                break;
            case No_Data:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        String name = inputView.getText().toString();
        presenter.searchDrugByName(name, false);
    }
}
