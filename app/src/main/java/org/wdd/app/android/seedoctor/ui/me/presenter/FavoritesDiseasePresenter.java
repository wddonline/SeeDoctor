package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbDisease;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesDiseaseActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesDiseaseDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDiseasePresenter implements BasePresenter, FavoritesDiseaseDataGetter.DataCallback {

    private FavoritesDiseaseActivity view;
    private FavoritesDiseaseDataGetter dataGetter;

    public FavoritesDiseasePresenter(FavoritesDiseaseActivity view) {
        this.view = view;
        dataGetter = new FavoritesDiseaseDataGetter(view, this, view.host);
    }

    public void getFavoritesDiseases() {
        dataGetter.queryFavoritesDiseases();
    }

    public void deleteSelectedDiseases(List<DbDisease> selectedItems) {
        dataGetter.deleteSelectedDiseases(selectedItems);
    }

    public void deleteSelectedDisease(DbDisease selectedItem) {
        dataGetter.deleteSelectedDisease(selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DbDisease> data) {
        view.bindDiseaseListViews(data);
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
