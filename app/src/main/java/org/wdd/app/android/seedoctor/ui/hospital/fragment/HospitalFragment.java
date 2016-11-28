package org.wdd.app.android.seedoctor.ui.hospital.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.hospital.presenter.HospitalPresenter;

import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private HospitalPresenter presenter;

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private List<Hospital> hospitals;
    private HospitalAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HospitalPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hospital, container, false);
        initViews();
        presenter.searchNearbyHospital(false);
        return rootView;
    }

    private void initViews() {
        initTitle();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_hospital_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_hospital_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fragment_hospital_toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.app_name);
    }

    public void appendHospitalList(boolean isRefreshing, List<Hospital> data) {
        if (adapter == null) {
            hospitals = new ArrayList<>();
            hospitals.addAll(data);
            adapter = new HospitalAdapter(getContext(), hospitals);
            recyclerView.setAdapter(adapter);
            return;
        }
        if (isRefreshing) {
            data.clear();
            refreshLayout.setRefreshing(false);
        } else {
            adapter.setLoadStatus(HospitalAdapter.LoadStatus.Normal);
        }
        hospitals.addAll(data);
        adapter.notifyDataSetChanged();
    }

    public void handleSearchErrorViews(boolean isRefreshing) {
        if (adapter == null) {
            return;
        }
        if (isRefreshing) {
            refreshLayout.setRefreshing(false);
        } else {
            adapter.setLoadStatus(HospitalAdapter.LoadStatus.Normal);
        }
    }

    @Override
    public void onRefresh() {
        presenter.searchNearbyHospital(true);
    }
}
