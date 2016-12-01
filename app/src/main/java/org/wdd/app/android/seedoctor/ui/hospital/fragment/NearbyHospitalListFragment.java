package org.wdd.app.android.seedoctor.ui.hospital.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.hospital.presenter.NearbyHospitalListPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;

import java.util.ArrayList;
import java.util.List;

public class NearbyHospitalListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        HospitalAdapter.OnLoadMoreListener{

    private NearbyHospitalListPresenter presenter;

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private List<Hospital> hospitals;
    private HospitalAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NearbyHospitalListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nearby_hospital_list, container, false);
            initViews();
            presenter.searchNearbyHospital(false);
        }
        return rootView;
    }

    private void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_nearby_hospital_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_nearby_hospital_list_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    public void appendHospitalList(boolean isRefreshing, List<Hospital> data) {
        if (adapter == null) {
            hospitals = new ArrayList<>();
            hospitals.addAll(data);
            adapter = new HospitalAdapter(getContext(), hospitals);
            adapter.setOnLoadMoreListener(this);
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

    @Override
    public void onLoadMore() {
        presenter.searchNearbyHospital(false);
    }
}
