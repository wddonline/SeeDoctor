package org.wdd.app.android.seedoctor.ui.nearby.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.builder.BannerAdsBuilder;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.nearby.activity.NearbyDrugstoreMapActivity;
import org.wdd.app.android.seedoctor.ui.nearby.adapter.DrugstoreAdapter;
import org.wdd.app.android.seedoctor.ui.nearby.adapter.HospitalAdapter;
import org.wdd.app.android.seedoctor.ui.nearby.data.HospitalListDataGetter;
import org.wdd.app.android.seedoctor.ui.nearby.model.Mark;
import org.wdd.app.android.seedoctor.ui.nearby.presenter.NearbyDrugstoreListPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.Constants;
import org.wdd.app.android.seedoctor.utils.SimpleAnimationListener;
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
    private ImageButton mapButton;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;

    private NearbyDrugstoreListPresenter presenter;
    private List<Mark> drugstores;
    private DrugstoreAdapter adapter;
    private TranslateAnimation hideAnim;
    private TranslateAnimation openAnim;
    private BannerAdsBuilder adsBuilder;

    private boolean isRefreshing = false;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nearby_drugstore, container, false);
            initData();
            initViews();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initData() {
        presenter = new NearbyDrugstoreListPresenter(host, this);
    }

    private void initViews() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_nearby_drugstore_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));
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

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_nearby_drugstore_refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        mapButton = (ImageButton) rootView.findViewById(R.id.fragment_nearby_drugstore_map);
        loadView = (LoadView) rootView.findViewById(R.id.fragment_nearby_drugstore_load);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.searchNearbyDrugstores();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearbyDrugstoreMapActivity.show(getActivity());
            }
        });

        ViewGroup adsView = (ViewGroup) rootView.findViewById(R.id.fragment_nearby_drugstore_ads);
        adsBuilder = new BannerAdsBuilder(getActivity(), adsView, Constants.HOME_DRUGSTORE_AD_ID);
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
        presenter.searchNearbyDrugstores();
    }

    public void appendHospitalList(List<Mark> data) {
        if (adapter == null) {
            drugstores = new ArrayList<>();
            drugstores.addAll(data);
            adapter = new DrugstoreAdapter(getActivity(), drugstores);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            if (data.size() < HospitalListDataGetter.PAGEZISE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
            refreshLayout.setVisibility(View.VISIBLE);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            mapButton.setVisibility(View.VISIBLE);

            if (BannerAdsBuilder.shouldShowAds(BannerAdsBuilder.BannerType.NearbyDrugstore)) {
                adsBuilder.addBannerAds();
            }
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
                AppToaster.show(R.string.request_failure);
                break;
            case Network_Error:
                AppToaster.show(R.string.no_connection_error);
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
