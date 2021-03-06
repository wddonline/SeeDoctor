package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiHospitalAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDiseaseGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Hospital;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiHospitalPresenter;
import org.wdd.app.android.seedoctor.ui.search.activity.HospitalSearchActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class WikiHospitalActivity extends BaseActivity {

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, WikiHospitalActivity.class);
        activity.startActivity(intent);
    }

    private final int REQUEST_HOSPITAL_FILTER = 1;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;
    private TextView filterView;

    private WikiHospitalAdapter adapter;
    private WikiHospitalPresenter presenter;
    private List<Hospital> hospitals;
    private AppConfManager confManager;
    private String provinceid;
    private String levelid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_hospital);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(this);
        presenter = new WikiHospitalPresenter(host, this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_hospital_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_filter:
                        HospitalFilterActivity.show(WikiHospitalActivity.this, REQUEST_HOSPITAL_FILTER);
                        break;
                }
                return true;
            }
        });
        filterView = (TextView) findViewById(R.id.activity_wiki_hospital_filter);
        setHospitalFilter();
    }

    private void setHospitalFilter() {
        provinceid = confManager.getWikiHospitalProvinceId();
        levelid = confManager.getWikiHospitalLevelId();
        String provinceName = confManager.getWikiHospitalProvinceName();
        String levelName = confManager.getWikiHospitalLevelName();
        if (TextUtils.isEmpty(provinceName) && TextUtils.isEmpty(levelName)) {
            filterView.setVisibility(View.GONE);
        } else {
            filterView.setVisibility(View.VISIBLE);
            StringBuffer filter = new StringBuffer();
            if (!TextUtils.isEmpty(provinceName) && TextUtils.isEmpty(levelName)) {
                filter.append(provinceName);
            } else if(TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(levelName)) {
                filter.append(levelName);
            } else if(!TextUtils.isEmpty(provinceName) && !TextUtils.isEmpty(levelName)) {
                filter.append(provinceName + "+" + levelName);
            }
            filterView.setText(filter.toString());
        }
    }

    private void initViews() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_wiki_hospital_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_wiki_hospital_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) findViewById(R.id.activity_wiki_hospital_loadview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getHospitalListData(provinceid, levelid, true);
            }
        });

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getHospitalListData(provinceid, levelid, true);
            }
        });

        presenter.getHospitalListData(provinceid, levelid, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_HOSPITAL_FILTER:
                setHospitalFilter();
                if (adapter != null) {
                    hospitals.clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
                }
                presenter.getHospitalListData(provinceid, levelid, true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void onHospitalSearchClicked(View v) {
        HospitalSearchActivity.show(this, findViewById(R.id.activity_wiki_hospital_search_layout));
    }

    public void showNoHospitalListResult(boolean refresh) {
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

    public void showHospitalListData(List<Hospital> data, boolean refresh) {
        if (adapter == null) {
            hospitals = new ArrayList<>();
            hospitals.addAll(data);
            adapter = new WikiHospitalAdapter(getBaseContext(), hospitals);
            adapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    presenter.getHospitalListData(provinceid, levelid, false);
                }
            });
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            refreshLayout.setVisibility(View.VISIBLE);
        } else {

            if (refresh) {
                hospitals.clear();
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
            hospitals.addAll(data);
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
}