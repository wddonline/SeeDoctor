package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDepartmentAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiDepartmentPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;
import org.wdd.app.android.seedoctor.views.index_bar.IndexBar;
import org.wdd.app.android.seedoctor.views.index_bar.suspension.SuspensionDecoration;

import java.util.List;

public class WikiDepartmentActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, WikiDepartmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private IndexBar indexBar;
    private TextView hintView;
    private LoadView loadView;
    private View dataView;

    private WikiDepartmentPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_department);
        initData();
        initTitle();
        initViews();
    }

    private void initData() {
        presenter = new WikiDepartmentPresenter(this);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_department_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_wiki_department_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        dataView = findViewById(R.id.activity_wiki_department_dataview);
        loadView = (LoadView) findViewById(R.id.activity_wiki_department_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDepartmentListData();
            }
        });
        presenter.getDepartmentListData();

        //使用indexBar
        hintView = (TextView) findViewById(R.id.activity_wiki_department_hint);
        indexBar = (IndexBar) findViewById(R.id.activity_wiki_department_index_bar);

//        //indexbar初始化
        indexBar.setmPressedShowTextView(hintView).setNeedRealIndex(true).setmLayoutManager(manager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showNoDataViews() {
        loadView.setStatus(LoadView.LoadStatus.No_Data);
    }

    public void showEmergencyListViews(List<Department> data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        dataView.setVisibility(View.VISIBLE);

        WikiDepartmentAdapter adapter = new WikiDepartmentAdapter(this, data);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        recyclerView.setAdapter(adapter);

        indexBar.setmSourceDatas(data).invalidate();

        SuspensionDecoration mDecoration = new SuspensionDecoration(this, data);
        recyclerView.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void showDataGettedFailureViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
