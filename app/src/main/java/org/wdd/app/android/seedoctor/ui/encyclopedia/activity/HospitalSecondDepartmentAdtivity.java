package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.HospitalDepartmentAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalSecondDepartmentPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class HospitalSecondDepartmentAdtivity extends BaseActivity {

    public static void show(Context context, String hospitalid, String hospitalname, String parenthosdepid, String parenthosdepname) {
        Intent intent = new Intent(context, HospitalSecondDepartmentAdtivity.class);
        intent.putExtra("hospitalid", hospitalid);
        intent.putExtra("hospitalname", hospitalname);
        intent.putExtra("parenthosdepname", parenthosdepname);
        intent.putExtra("parenthosdepid", parenthosdepid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private RecyclerView recyclerView;
    private LoadView loadView;

    private HospitalSecondDepartmentPresenter presenter;
    private HospitalDepartmentAdapter adapter;
    private List<Department> departments;

    private String hospitalid;
    private String hospitalname;
    private String parenthosdepname;
    private String parenthosdepid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_second_department);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        hospitalid = getIntent().getStringExtra("hospitalid");
        hospitalname = getIntent().getStringExtra("hospitalname");
        parenthosdepname = getIntent().getStringExtra("parenthosdepname");
        parenthosdepid = getIntent().getStringExtra("parenthosdepid");

        presenter = new HospitalSecondDepartmentPresenter(host, this);
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_hospital_second_department_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titleView = (TextView) findViewById(R.id.activity_hospital_second_department_title);
        titleView.setText(hospitalname + " - " + parenthosdepname);
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_hospital_second_department_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) findViewById(R.id.activity_hospital_second_department_loadview);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDepartmentListData(hospitalid, parenthosdepid);
            }
        });

        presenter.getDepartmentListData(hospitalid, parenthosdepid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelRequest();
    }

    public void showNetworkErrorView() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showRequetErrorView(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNoDoctorListResult() {
        loadView.setStatus(LoadView.LoadStatus.No_Data);
    }

    public void showDepartmentListData(List<Department> data) {
        departments = new ArrayList<>();
        departments.addAll(data);
        adapter = new HospitalDepartmentAdapter(this, departments, HospitalDepartmentAdapter.DepartmentCategory.SECOND);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        loadView.setStatus(LoadView.LoadStatus.Normal);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
