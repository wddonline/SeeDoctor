package org.wdd.app.android.seedoctor.ui.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.HospitalDetailActivity;
import org.wdd.app.android.seedoctor.ui.me.adapter.FavoritesHospitalAdapter;
import org.wdd.app.android.seedoctor.ui.me.presenter.FavoritesHospitalPresenter;
import org.wdd.app.android.seedoctor.utils.AppToaster;
import org.wdd.app.android.seedoctor.utils.DensityUtils;
import org.wdd.app.android.seedoctor.views.LineDividerDecoration;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesHospitalActivity extends BaseActivity implements FavoritesHospitalAdapter.FavoritesHospitalCallback {

    public static void show(Context context) {
        Intent intent = new Intent(context, FavoritesHospitalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private final int REQUEST_CODE_DETAIL = 1;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LoadView loadView;
    private CheckBox checkBox;

    private List<FavoritesHospitalAdapter.HospitalFavorites> hospitals;
    private FavoritesHospitalPresenter presenter;
    private FavoritesHospitalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_hospital);
        initData();
        initTitles();
        initViews();
    }

    private void initData() {
        presenter = new FavoritesHospitalPresenter(this);
    }

    private void initTitles() {
        toolbar = (Toolbar) findViewById(R.id.activity_favorites_hospital_toolbar);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_favorites_hospital_recyclerview);
        recyclerView.addItemDecoration(new LineDividerDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        checkBox = (CheckBox) findViewById(R.id.activity_favorites_hospital_all);
        loadView = (LoadView) findViewById(R.id.activity_favorites_hospital_loadview);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    adapter.selectAll();
                } else {
                    adapter.unselectAll();
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_favorites_cancel:
                        if (adapter.getMode() == FavoritesHospitalAdapter.Mode.Select) {
                            cancelSelectMode();
                        }
                        return true;
                    case R.id.menu_favorites_delete:
                        if (adapter.getSelectedItem().size() == 0) {
                            AppToaster.show(R.string.no_favorite_selected);
                            return true;
                        }
                        showLoadingDialog(R.string.deleting_favotites_items);
                        presenter.deleteSelectedHospitals(adapter.getSelectedItem());
                        return true;
                }
                return false;
            }
        });

    }

    private void cancelSelectMode() {
        adapter.setMode(FavoritesHospitalAdapter.Mode.Normal);
        toolbar.setNavigationIcon(R.mipmap.back);
        checkBox.setVisibility(View.GONE);
        toolbar.getMenu().findItem(R.id.menu_favorites_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.menu_favorites_cancel).setVisible(false);
        checkBox.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        presenter.getFavoritesHospitals();
        return true;
    }

    public void showNoDataViews() {
        if (adapter == null) {
            loadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
        }
    }

    public void bindHospitalListViews(List<DbHospital> data) {
        hospitals = new ArrayList<>();
        for (DbHospital hospital : data) {
            hospitals.add(new FavoritesHospitalAdapter.HospitalFavorites(false, hospital));
        }
        adapter = new FavoritesHospitalAdapter(this, hospitals);
        adapter.setCallback(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setMode(FavoritesHospitalAdapter.Mode.Normal);
        adapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
    }

    @Override
    public void onBackPressed() {
        if (adapter != null && adapter.getMode() == FavoritesHospitalAdapter.Mode.Select) {
            cancelSelectMode();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void jumpToDetailActivity(int position, String hospitalid, String hospitalname) {
        HospitalDetailActivity.showForResult(this, position, hospitalid, hospitalname, REQUEST_CODE_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_DETAIL:
                int position = data.getIntExtra("position", -1);
                if (position == -1) return;
                hospitals.remove(position);
                adapter.notifyItemRemoved(position);
                break;
        }
    }

    @Override
    public void onHospitalDeleted(int position, FavoritesHospitalAdapter.HospitalFavorites favorites) {
        showLoadingDialog(R.string.deleting_favotites_items);
        presenter.deleteSelectedHospital(position, favorites.hospital);
    }

    @Override
    public void switchSelectMode() {
        toolbar.setNavigationIcon(null);
        checkBox.setVisibility(View.VISIBLE);
        toolbar.getMenu().findItem(R.id.menu_favorites_delete).setVisible(true);
        toolbar.getMenu().findItem(R.id.menu_favorites_cancel).setVisible(true);
    }

    @Override
    public void onAllSelected() {
        checkBox.setChecked(true);
    }

    @Override
    public void onPartSelected() {
        checkBox.setChecked(false);
    }

    public void showDeleteOverViews() {
        hideLoadingDialog();
        List<FavoritesHospitalAdapter.HospitalFavorites> selectedItems = adapter.getSelectedOriginItem();
        hospitals.removeAll(selectedItems);
        adapter.notifyDataSetChanged();
        cancelSelectMode();
    }

    public void showDeleteOverViews(int position) {
        hideLoadingDialog();
        cancelSelectMode();
        hospitals.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
