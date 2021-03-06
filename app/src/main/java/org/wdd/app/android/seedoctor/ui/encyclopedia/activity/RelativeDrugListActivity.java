package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.RelativeDrugAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.RelativeDiseaseListGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.RelativeDrugListPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class RelativeDrugListActivity extends BaseActivity {

    public static void show(Activity activity, String diseaseid, String diseasename) {
        Intent intent = new Intent(activity, RelativeDrugListActivity.class);
        intent.putExtra("diseaseid", diseaseid);
        intent.putExtra("diseasename", diseasename);
        activity.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;

    private RelativeDrugListPresenter presenter;
    private RelativeDrugAdapter adapter;
    private List<Drug> drugs;

    private String diseaseid;
    private String diseasename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_drug_list);
        initData();
        initTitle();
        initView();
    }

    private void initData() {
        diseaseid = getIntent().getStringExtra("diseaseid");
        diseasename = getIntent().getStringExtra("diseasename");
        presenter = new RelativeDrugListPresenter(host, this);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_relative_drug_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.activity_relative_drug_list_titile)).setText(diseasename);
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_relative_drug_list_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_relative_drug_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) findViewById(R.id.activity_relative_drug_list_loadview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDrugListData(diseaseid, true);
            }
        });

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDrugListData(diseaseid, true);
            }
        });

        presenter.getDrugListData(diseaseid, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void showDiseaseListData(List<Drug> data, boolean refresh) {
        if (adapter == null) {
            drugs = new ArrayList<>();
            drugs.addAll(data);
            adapter = new RelativeDrugAdapter(getBaseContext(), drugs);
            adapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    presenter.getDrugListData(diseaseid, false);
                }
            });
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            refreshLayout.setVisibility(View.VISIBLE);
        } else {

            if (refresh) {
                drugs.clear();
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
            drugs.addAll(data);
            adapter.notifyDataSetChanged();
        }
        if (data.size() < RelativeDiseaseListGetter.PAGE_SIZE) {
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

    public void showNoDrugListResult(boolean refresh) {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            AppToaster.show(R.string.no_data_error);
            if (refresh) {
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
        }
    }
}
