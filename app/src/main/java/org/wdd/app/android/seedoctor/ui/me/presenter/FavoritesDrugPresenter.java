package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbDrug;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesDrugActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesDrugDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDrugPresenter implements BasePresenter, FavoritesDrugDataGetter.DataCallback {

    private FavoritesDrugActivity view;
    private FavoritesDrugDataGetter dataGetter;

    public FavoritesDrugPresenter(FavoritesDrugActivity view) {
        this.view = view;
        dataGetter = new FavoritesDrugDataGetter(view, this, view.host);
    }

    public void getFavoritesDrugs() {
        dataGetter.queryFavoritesDrugs();
    }

    public void deleteSelectedDrugs(List<DbDrug> selectedItems) {
        dataGetter.deleteSelectedDrugs(selectedItems);
    }

    public void deleteSelectedDrug(DbDrug selectedItem) {
        dataGetter.deleteSelectedDrug(selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DbDrug> data) {
        view.bindDrugListViews(data);
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
