package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesDoctorActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesDoctorDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDoctorPresenter implements BasePresenter, FavoritesDoctorDataGetter.DataCallback {

    private FavoritesDoctorActivity view;
    private FavoritesDoctorDataGetter dataGetter;

    public FavoritesDoctorPresenter(FavoritesDoctorActivity view) {
        this.view = view;
        dataGetter = new FavoritesDoctorDataGetter(view, this, view.host);
    }

    public void getFavoritesDoctors() {
        dataGetter.queryFavoritesDoctors();
    }

    public void deleteSelectedDoctors(List<DbDoctor> selectedItems) {
        dataGetter.deleteSelectedDoctors(selectedItems);
    }

    public void deleteSelectedDoctor(DbDoctor selectedItem) {
        dataGetter.deleteSelectedDoctor(selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DbDoctor> data) {
        view.bindDoctorListViews(data);
    }

    @Override
    public void onNoDataGetted() {
        view.showNoDataViews();
    }

    @Override
    public void onDeleteSelectedData() {
        view.showDeleteOverViews();
    }

    @Override
    public void onDeleteSelectedData(int position) {
        view.showDeleteOverViews(position);
    }
}
