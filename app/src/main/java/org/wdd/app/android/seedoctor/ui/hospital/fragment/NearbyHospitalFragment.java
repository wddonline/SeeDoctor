package org.wdd.app.android.seedoctor.ui.hospital.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.drugstore.adapter.DrugstoreAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.activity.NearbyHospitalMapActivity;
import org.wdd.app.android.seedoctor.ui.hospital.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.hospital.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.ui.hospital.presenter.NearbyHospitalListPresenter;
import org.wdd.app.android.seedoctor.ui.search.activity.NearbySearchActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.SimpleAnimationListener;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyHospitalFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        DrugstoreAdapter.OnLoadMoreListener {

    private View rootView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageButton mapButton;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;

    private List<Hospital> hospitals;
    private HospitalAdapter adapter;
    private NearbyHospitalListPresenter presenter;
    private TranslateAnimation hideAnim;
    private TranslateAnimation openAnim;

    private boolean isRefreshing = false;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_neary_hospital, null);
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
        presenter = new NearbyHospitalListPresenter(host, this);
    }

    private void initTitle() {
        toolbar = (Toolbar) rootView.findViewById(R.id.fragment_nearby_hospital_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        MenuItem menuItem = toolbar.getMenu().add(0, 0, 0, getString(R.string.search));
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.mipmap.img_search);
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NearbySearchActivity.show(getContext());
                return true;
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_nearby_hospital_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_nearby_hospital_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {//向下滑动
                    showMapHideAnim();
                } else if (dy < 0){//向上滑动
                    showMapOpenAnim();
                }
            }
        });
        mapButton = (ImageButton) rootView.findViewById(R.id.fragment_nearby_hospital_map);
        loadView = (LoadView) rootView.findViewById(R.id.fragment_nearby_hospital_load);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearbyHospitalMapActivity.show(getContext());
            }
        });
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.searchNearbyHospital();
            }
        });
    }

    private void showMapHideAnim() {
        if (mapButton.getVisibility() == View.GONE) return;
        if (hideAnim != null) return;
        Animation anim = mapButton.getAnimation();
        if (anim != null) {
            mapButton.clearAnimation();
        }
        hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.5f);
        hideAnim.setAnimationListener(new SimpleAnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mapButton.setVisibility(View.GONE);
                hideAnim = null;
            }

        });
        hideAnim.setFillAfter(true);
        hideAnim.setDuration(300);
        mapButton.startAnimation(hideAnim);
    }

    private void showMapOpenAnim() {
        if (mapButton.getVisibility() == View.VISIBLE) return;
        if (openAnim != null) return;
        Animation anim = mapButton.getAnimation();
        if (anim != null) {
            mapButton.clearAnimation();
        }
        mapButton.setVisibility(View.VISIBLE);
        openAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.5f, Animation.RELATIVE_TO_SELF, 0);
        openAnim.setAnimationListener(new SimpleAnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                openAnim = null;
            }

        });
        openAnim.setFillAfter(true);
        openAnim.setDuration(300);
        mapButton.startAnimation(openAnim);
    }

    @Override
    protected void lazyLoad() {
        presenter.searchNearbyHospital();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            mapButton.setVisibility(View.VISIBLE);
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

}
