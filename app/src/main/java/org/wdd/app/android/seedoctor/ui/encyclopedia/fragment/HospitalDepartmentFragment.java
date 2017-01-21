package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.HospitalDepartmentAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Department;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalDepartmentPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/6/17.
 */

public class HospitalDepartmentFragment extends BaseFragment {

    public static final String KEY_ID = "id";

    private View rootView;
    private RecyclerView recyclerView;
    private LoadView loadView;

    private HospitalDepartmentAdapter adapter;
    private HospitalDepartmentPresenter presenter;
    private List<Department> departments;

    private String hospitalid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalid = getArguments().getString(KEY_ID);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = View.inflate(getContext(), R.layout.fragment_hospital_department, null);
            initData();
            initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initData() {
        presenter = new HospitalDepartmentPresenter(host, this);
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_hospital_department_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) rootView.findViewById(R.id.fragment_hospital_department_loadview);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDepartmentListData(hospitalid, "");
            }
        });
    }

    @Override
    protected void lazyLoad() {
        presenter.getDepartmentListData(hospitalid, "0");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.destory();
    }

    public void showNoDoctorListResult() {
        loadView.setStatus(LoadView.LoadStatus.No_Data);
    }

    public void showDepartmentListData(List<Department> data) {
        departments = new ArrayList<>();
        departments.addAll(data);
        adapter = new HospitalDepartmentAdapter(getContext(), departments, HospitalDepartmentAdapter.DepartmentCategory.FIRST);
        recyclerView.setAdapter(adapter);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        loadView.setStatus(LoadView.LoadStatus.Normal);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showRequetErrorView(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorView() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
