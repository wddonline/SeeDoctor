package org.wdd.app.android.seedoctor.ui.drugstore.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.drugstore.adapter.DrugstoreAdapter;
import org.wdd.app.android.seedoctor.ui.drugstore.model.Drugstore;
import org.wdd.app.android.seedoctor.ui.drugstore.presenter.NearbyDrugstoreListPresenter;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.search.activity.NearbySearchActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyDrugstoreFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        DrugstoreAdapter.OnLoadMoreListener {

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;
    private Toolbar toolbar;

    private NearbyDrugstoreListPresenter presenter;
    private List<Drugstore> drugstores;
    private DrugstoreAdapter adapter;

    private boolean isRefreshing = false;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nearby_drugstore, null);
            initData();
            initTitle();
            initViews();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initData() {
        presenter = new NearbyDrugstoreListPresenter(this);
    }

    private void initTitle() {
        setHasOptionsMenu(true);
        toolbar = (Toolbar) rootView.findViewById(R.id.fragment_nearby_drugstore_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_search:
                        NearbySearchActivity.show(getContext());
                        return true;
                }
                return false;
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_nearby_drugstore_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_nearby_drugstore_refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        loadView = (LoadView) rootView.findViewById(R.id.fragment_nearby_drugstore_load);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.searchNearbyDrugstores();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        presenter.searchNearbyDrugstores();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby_list, menu);
    }

    public void appendHospitalList(List<Drugstore> data) {
        if (adapter == null) {
            drugstores = new ArrayList<>();
            drugstores.addAll(data);
            adapter = new DrugstoreAdapter(getContext(), drugstores);
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
            drugstores.clear();
            refreshLayout.setRefreshing(false);
        } else {
            adapter.setLoadStatus(HospitalAdapter.LoadStatus.Normal);
        }
        drugstores.addAll(data);
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
        presenter.searchNearbyDrugstores();
    }
}
