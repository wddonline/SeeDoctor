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
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDoctorAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.data.WikiDoctorGetter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.HospitalDoctorPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/6/17.
 */

public class HospitalDoctorFragment extends BaseFragment {

    public static final String KEY_ID = "id";

    private View rootView;
    private RecyclerView recyclerView;
    private LoadView loadView;

    private WikiDoctorAdapter adapter;
    private HospitalDoctorPresenter presenter;
    private List<Doctor> doctors;

    private String hospitalid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalid = getArguments().getString(KEY_ID);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = View.inflate(getContext(), R.layout.fragment_hospital_doctor, null);
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
        presenter = new HospitalDoctorPresenter(host, this);
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_hospital_doctor_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LineDividerDecoration decoration = new LineDividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        loadView = (LoadView) rootView.findViewById(R.id.fragment_hospital_doctor_loadview);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDoctorListData(hospitalid);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        presenter.getDoctorListData(hospitalid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.destory();
    }

    public void showNoDoctorListResult() {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            AppToaster.show(R.string.no_data_error);
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        }
    }

    public void showDoctorListData(List<Doctor> data) {
        if (adapter == null) {
            doctors = new ArrayList<>();
            doctors.addAll(data);
            adapter = new WikiDoctorAdapter(getContext(), doctors);
            adapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    presenter.getDoctorListData(hospitalid);
                }
            });
            recyclerView.setAdapter(adapter);
            loadView.setStatus(LoadView.LoadStatus.Normal);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            doctors.addAll(data);
            adapter.notifyDataSetChanged();
        }
        if (data.size() < WikiDoctorGetter.PAGE_SIZE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        }
    }

    public void showRequetErrorView(String errorMsg) {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
        } else {
            AppToaster.show(errorMsg);
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }

    public void showNetworkErrorView() {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.Network_Error);
        } else {
            AppToaster.show(R.string.no_connection_error);
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }
}
