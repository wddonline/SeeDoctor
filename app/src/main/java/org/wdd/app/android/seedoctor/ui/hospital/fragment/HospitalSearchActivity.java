package org.wdd.app.android.seedoctor.ui.hospital.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalSearchAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalSearchGetter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalSearchActivity extends BaseActivity implements AbstractCommonAdapter.OnLoadMoreListener {

    public static void show(Context context) {
        Intent intent = new Intent(context, HospitalSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private EditText inputView;

    private HospitalSearchPresenter presenter;
    private HospitalSearchAdapter adapter;
    private List<Hospital> hospitals;

    private boolean isAppend = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_search);
        initData();
        initViews();
    }

    private void initData() {
        presenter = new HospitalSearchPresenter(this);
    }

    private void initViews() {
        inputView = (EditText) findViewById(R.id.activity_hospital_search_input);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if (TextUtils.isEmpty(name)) {
                    if (adapter == null) return;
                    hospitals.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }
                isAppend = false;
                presenter.searchHospitalByName(name, isAppend);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_hospital_search_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    public void showHospitalDataView(List<Hospital> data) {
        if (adapter == null) {
            hospitals = new ArrayList<>();
            hospitals.addAll(data);
            adapter = new HospitalSearchAdapter(getBaseContext(), hospitals);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            if (data.size() < HospitalSearchGetter.PAGEZISE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
        }
        if (isAppend) {
            hospitals.addAll(data);
        } else {
            hospitals.clear();
            hospitals.addAll(data);
        }
        adapter.notifyDataSetChanged();
        if (data.size() < HospitalSearchGetter.PAGEZISE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }

    public void handleRequestErrorViews(LoadView.LoadStatus status) {
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
        isAppend = true;
        String name = inputView.getText().toString();
        presenter.searchHospitalByName(name, isAppend);
    }
}
