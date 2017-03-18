package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesHospitalActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesHospitalDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesHospitalPresenter implements BasePresenter, FavoritesHospitalDataGetter.DataCallback {

    private FavoritesHospitalActivity view;
    private FavoritesHospitalDataGetter dataGetter;

    public FavoritesHospitalPresenter(FavoritesHospitalActivity view) {
        this.view = view;
        dataGetter = new FavoritesHospitalDataGetter(view, this, view.host);
    }

    public void getFavoritesHospitals() {
        dataGetter.queryFavoritesHospitals();
    }

    public void deleteSelectedHospitals(List<DbHospital> selectedItems) {
        dataGetter.deleteSelectedHospitals(selectedItems);
    }

    public void deleteSelectedHospital(DbHospital selectedItem) {
        dataGetter.deleteSelectedHospital(selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DbHospital> data) {
        view.bindHospitalListViews(data);
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
    public void onDeleteSelectedData(int id) {
        view.showDeleteOverViews(id);
    }
}
