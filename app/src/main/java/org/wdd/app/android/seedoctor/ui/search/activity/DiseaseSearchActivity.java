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
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDiseaseAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.search.data.DiseaseSearchGetter;
import org.wdd.app.android.seedoctor.ui.search.data.NearbySearchGetter;
import org.wdd.app.android.seedoctor.ui.search.presenter.DiseaseSearchPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class DiseaseSearchActivity extends BaseActivity implements AbstractCommonAdapter.OnLoadMoreListener {

    private static final String SHARED_NAME = "search_view";

    public static void show(Activity activity, View sharedView) {
        Intent intent = new Intent(activity, DiseaseSearchActivity.class);
        ViewCompat.setTransitionName(sharedView, SHARED_NAME);
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, SHARED_NAME).toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

    private RecyclerView recyclerView;
    private EditText inputView;

    private DiseaseSearchPresenter presenter;
    private WikiDiseaseAdapter adapter;
    private List<Disease> diseases;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_search);
        initData();
        initViews();
    }

    private void initData() {
        presenter = new DiseaseSearchPresenter(this);
    }

    private void initViews() {
        ViewCompat.setTransitionName(findViewById(R.id.activity_disease_search_input_layout), SHARED_NAME);
        inputView = (EditText) findViewById(R.id.activity_disease_search_input);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if (TextUtils.isEmpty(name)) {
                    presenter.destory();
                    if (adapter == null) return;
                    diseases.clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
                    return;
                }
                presenter.searchDiseaseByName(name, true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_disease_search_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showDiseaseDataView(List<Disease> data, boolean refresh) {
        if (adapter == null) {
            diseases = new ArrayList<>();
            diseases.addAll(data);
            adapter = new WikiDiseaseAdapter(getBaseContext(), diseases);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            if (data.size() < NearbySearchGetter.PAGEZISE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
        }
        if (refresh) {
            diseases.clear();
            diseases.addAll(data);
        } else {
            diseases.addAll(data);
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
        presenter.searchDiseaseByName(name, false);
    }
}
