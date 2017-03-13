package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDiseaseAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDiseaseGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiDiseasePresenter;
import org.wdd.app.android.seedoctor.ui.search.activity.DiseaseSearchActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.DensityUtils;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class WikiDiseaseActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, WikiDiseaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;

    private WikiDiseasePresenter presenter;
    private WikiDiseaseAdapter adapter;
    private List<Disease> diseases;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_disease);
        initData();
        initTitle();
        initView();
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_disease_toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        toolbar.setNavigationIcon(R.mipmap.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        MobclickAgent.openActivityDurationTrack(false);
        presenter = new WikiDiseasePresenter(host, this);
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_wiki_disease_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_wiki_disease_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) findViewById(R.id.activity_wiki_disease_loadview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDiseaseListData(true);
            }
        });

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDiseaseListData(true);
            }
        });

        presenter.getDiseaseListData(false);
    }

    public void onDiseaseSearchClicked(View v) {
        DiseaseSearchActivity.show(this, findViewById(R.id.activity_wiki_disease_search_layout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showDiseaseListData(List<Disease> data, boolean refresh) {
        if (adapter == null) {
            diseases = new ArrayList<>();
            diseases.addAll(data);
            adapter = new WikiDiseaseAdapter(getBaseContext(), diseases);
            adapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    presenter.getDiseaseListData(false);
                }
            });
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            refreshLayout.setVisibility(View.VISIBLE);
        } else {

            if (refresh) {
                diseases.clear();
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
            diseases.addAll(data);
            adapter.notifyDataSetChanged();
        }
        if (data.size() < WikiDiseaseGetter.PAGE_SIZE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        }
    }

    public void showRequetErrorView(String errorMsg, boolean refresh) {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
        } else {
            AppToaster.show(errorMsg);
            if (refresh) {
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
        }
    }

    public void showNetworkErrorView(boolean refresh) {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.Network_Error);
        } else {
            AppToaster.show(R.string.no_connection_error);
            if (refresh) {
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
        }
    }

    public void showNoDiseaseListResult(boolean refresh) {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            AppToaster.show(R.string.no_data_error);
            if (refresh) {
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
        }
    }
}
