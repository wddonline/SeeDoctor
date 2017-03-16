package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
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
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDoctorAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDiseaseGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiDoctorPresenter;
import org.wdd.app.android.seedoctor.ui.search.activity.DoctorSearchActivity;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class WikiDoctorActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, WikiDoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private final int REQUEST_DOCTOR_FILTER = 1;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadView loadView;
    private TextView filterView;

    private WikiDoctorAdapter adapter;
    private WikiDoctorPresenter presenter;
    private List<Doctor> doctors;
    private AppConfManager confManager;
    private String provinceid;
    private String hospitallevel;
    private String doclevelid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_doctor);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(this);
        presenter = new WikiDoctorPresenter(host, this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_doctor_toolbar);
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
                        DoctorFilterActivity.show(WikiDoctorActivity.this, REQUEST_DOCTOR_FILTER);
                        break;
                }
                return true;
            }
        });
        filterView = (TextView) findViewById(R.id.activity_wiki_doctor_filter);
        setDoctorFilter();
    }

    private void setDoctorFilter() {
        provinceid = confManager.getWikiDoctorProvinceId();
        hospitallevel = confManager.getWikiDoctorLevelId();
        doclevelid = confManager.getWikiDoctorJobLevelId();
        String provinceName = confManager.getWikiDoctorProvinceName();
        String levelName = confManager.getWikiDoctorLevelName();
        String doctorLevel = confManager.getWikiDoctorJobLevelName();
        if (TextUtils.isEmpty(provinceName) && TextUtils.isEmpty(levelName) && TextUtils.isEmpty(doctorLevel)) {
            filterView.setVisibility(View.GONE);
        } else {
            filterView.setVisibility(View.VISIBLE);
            List<String> list = new ArrayList<>();
            if (!TextUtils.isEmpty(provinceName)) list.add(provinceName);
            if (!TextUtils.isEmpty(levelName)) list.add(levelName);
            if (!TextUtils.isEmpty(doctorLevel)) list.add(doctorLevel);
            StringBuffer filter = new StringBuffer();
            for (String str : list) {
                filter.append(str + "+");
            }
            CharSequence filterStr = filter.subSequence(0, filter.length() - 1);
            filterView.setText(filterStr);
        }
    }

    private void initViews() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_wiki_doctor_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.activity_wiki_doctor_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) findViewById(R.id.activity_wiki_doctor_loadview);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDoctorListData(provinceid, hospitallevel, doclevelid, true);
            }
        });

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDoctorListData(provinceid, hospitallevel, doclevelid, true);
            }
        });

        presenter.getDoctorListData(provinceid, hospitallevel, doclevelid, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DOCTOR_FILTER:
                if (adapter != null) {
                    doctors.clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
                }
                setDoctorFilter();
                presenter.getDoctorListData(provinceid, hospitallevel, doclevelid, true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void onDoctorSearchClicked(View v) {
        DoctorSearchActivity.show(this, findViewById(R.id.activity_wiki_doctor_search_layout));
    }

    public void showNoDoctorListResult(boolean refresh) {
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

    public void showDoctorListData(List<Doctor> data, boolean refresh) {
        if (adapter == null) {
            doctors = new ArrayList<>();
            doctors.addAll(data);
            adapter = new WikiDoctorAdapter(getBaseContext(), doctors);
            adapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    presenter.getDoctorListData(provinceid, hospitallevel, doclevelid, false);
                }
            });
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            refreshLayout.setVisibility(View.VISIBLE);
        } else {

            if (refresh) {
                doctors.clear();
                refreshLayout.setRefreshing(false);
            } else {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            }
            doctors.addAll(data);
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
