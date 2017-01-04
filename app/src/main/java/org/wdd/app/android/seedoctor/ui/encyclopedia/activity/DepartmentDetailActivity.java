package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Disease;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DepartmentDetailPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;

public class DepartmentDetailActivity extends BaseActivity {

    public static void show(Context context, String departmentid, String departmentname) {
        Intent intent = new Intent(context, DepartmentDetailActivity.class);
        intent.putExtra("departmentid", departmentid);
        intent.putExtra("departmentname", departmentname);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private LoadView loadView;

    private DepartmentDetailPresenter presenter;

    private String departmentid;
    private String departmentname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        presenter = new DepartmentDetailPresenter(this);

        departmentid = getIntent().getStringExtra("departmentid");
        departmentname = getIntent().getStringExtra("departmentname");
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_department_detail_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titleView = (TextView) findViewById(R.id.activity_department_detail_title);
        titleView.setText(departmentname);
    }

    private void initViews() {
        loadView = (LoadView) findViewById(R.id.activity_department_detail_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDepartmentDetailData(departmentid);
                presenter.getDiseaseListByDepartmentId(departmentid);
            }
        });
        presenter.getDepartmentDetailData(departmentid);
        presenter.getDiseaseListByDepartmentId(departmentid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showDepartmentDetailViews(Department data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_department_detail_dataview).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.activity_department_detail_desc)).setText(data.introduction);
    }

    public void showDataGettedFailureViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showDiseaseListViews(List<Disease> data) {
        LinearLayout container = (LinearLayout) findViewById(R.id.activity_department_detail_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp1.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        int count = data.size() > 10 ? 10 : data.size();
        for (int i = 0; i < count; i++) {
            final Disease disease = data.get(i);
            View view = View.inflate(this, R.layout.item_common_arror_right, null);
            TextView labelView = (TextView) view.findViewById(R.id.item_common_arrow_right_label);
            labelView.setText(disease.diseasename);
            view.findViewById(R.id.item_common_arror_right_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DiseaseDetailActivity.show(getBaseContext(), disease.diseaseid, disease.diseasename);
                }
            });
            container.addView(view, lp);

            ImageView divider = new ImageView(getBaseContext());
            divider.setBackgroundColor(getResources().getColor(R.color.wiki_divider_color));
            container.addView(divider, lp1);
        }
        if (data.size() > 10) {
            View view = View.inflate(this, R.layout.item_common_arror_right, null);
            TextView labelView = (TextView) view.findViewById(R.id.item_common_arrow_right_label);
            labelView.setText(R.string.more);
            view.findViewById(R.id.item_common_arror_right_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeDiseaseListActivity.showFromDepartment(getBaseContext(), departmentid, departmentname);
                }
            });
            container.addView(view, lp);
        } else {
            container.removeViewAt(container.getChildCount() - 1);
        }
    }
}
