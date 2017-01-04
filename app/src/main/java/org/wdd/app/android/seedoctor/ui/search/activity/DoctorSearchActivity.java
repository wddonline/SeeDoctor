package org.wdd.app.android.seedoctor.ui.search.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.adapter.WikiDoctorAdapter;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Doctor;
import org.wdd.app.android.seedoctor.ui.search.data.DoctorSearchGetter;
import org.wdd.app.android.seedoctor.ui.search.presenter.DoctorSearchPresenter;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class DoctorSearchActivity extends Activity implements AbstractCommonAdapter.OnLoadMoreListener {

    private static final String SHARED_NAME = "search_view";

    public static void show(Activity activity, View sharedView) {
        Intent intent = new Intent(activity, DoctorSearchActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, SHARED_NAME).toBundle();
        intent.putExtras(bundle);
        ActivityCompat.startActivity(activity, intent, bundle);
    }

    private RecyclerView recyclerView;
    private EditText inputView;

    private DoctorSearchPresenter presenter;
    private WikiDoctorAdapter adapter;
    private List<Doctor> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);
        initData();
        initViews();
    }

    private void initData() {
        presenter = new DoctorSearchPresenter(this);
    }

    private void initViews() {
        ViewCompat.setTransitionName(findViewById(R.id.activity_disease_search_input_layout), SHARED_NAME);
        inputView = (EditText) findViewById(R.id.activity_disease_search_input);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                if (TextUtils.isEmpty(name)) {
                    if (adapter == null) return;
                    doctors.clear();
                    adapter.notifyDataSetChanged();
                    adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
                    return;
                }
                presenter.searchDiseaseByName(name, true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.activity_disease_search_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    public void showDiseaseDataView(List<Doctor> data, boolean refresh) {
        if (adapter == null) {
            doctors = new ArrayList<>();
            doctors.addAll(data);
            adapter = new WikiDoctorAdapter(getBaseContext(), doctors);
            adapter.setOnLoadMoreListener(this);
            recyclerView.setAdapter(adapter);
            if (data.size() < DoctorSearchGetter.PAGE_SIZE) {
                adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            }
        }
        if (refresh) {
            doctors.clear();
            doctors.addAll(data);
        } else {
            doctors.addAll(data);
        }
        adapter.notifyDataSetChanged();
        if (data.size() < DoctorSearchGetter.PAGE_SIZE) {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
        }
    }

    public void handleRequestErrorViews(LoadView.LoadStatus status, boolean refresh) {
        switch (status) {
            case Network_Error:
                break;
            case Request_Failure:
                break;
            case No_Data:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        String name = inputView.getText().toString();
        presenter.searchDiseaseByName(name, false);
    }
}
