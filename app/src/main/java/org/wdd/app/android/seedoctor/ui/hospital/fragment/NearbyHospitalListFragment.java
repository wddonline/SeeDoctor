package org.wdd.app.android.seedoctor.ui.hospital.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.hospital.presenter.NearbyHospitalListPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class NearbyHospitalListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        HospitalAdapter.OnLoadMoreListener{

    private NearbyHospitalListPresenter presenter;

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;

    private List<Hospital> hospitals;
    private HospitalAdapter adapter;

    private boolean isRefreshing = false;

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
            presenter.searchNearbyHospital();
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

        loadView = (LoadView) rootView.findViewById(R.id.fragment_nearby_hospital_list_load);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.searchNearbyHospital();
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

    public void appendHospitalList(List<Hospital> data) {
        if (adapter == null) {
            hospitals = new ArrayList<>();
            hospitals.addAll(data);
            adapter = new HospitalAdapter(getContext(), hospitals);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            if (data.size() < HospitalListDataGetter.PAGEZISE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
            refreshLayout.setVisibility(View.VISIBLE);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            return;
        }
        if (isRefreshing) {
            hospitals.clear();
            refreshLayout.setRefreshing(false);
        } else {
            adapter.setLoadStatus(HospitalAdapter.LoadStatus.Normal);
        }
        hospitals.addAll(data);
        adapter.notifyDataSetChanged();
        if (data.size() < HospitalListDataGetter.PAGEZISE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        }

    }

    public void handleSearchDataErrorViews(LoadView.LoadStatus status) {
        if (adapter == null) {
            loadView.setStatus(status);
            return;
        }
        switch (status) {
            case Request_Failure:
                AppToaster.show(R.string.error_request);
                break;
            case Network_Error:
                AppToaster.show(R.string.error_no_connection);
                break;
            default:
                break;
        }
        if (isRefreshing) {
            refreshLayout.setRefreshing(false);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        presenter.reloadNearbyHospital();
    }

    @Override
    public void onLoadMore() {
        isRefreshing = false;
        presenter.searchNearbyHospital();
    }

    public void resetHospitalData() {
        refreshLayout.setRefreshing(true);
        onRefresh();
    }
}
