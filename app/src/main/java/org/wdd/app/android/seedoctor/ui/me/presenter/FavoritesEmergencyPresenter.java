package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbEmergency;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesEmergencyActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesEmergencyDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesEmergencyPresenter implements BasePresenter, FavoritesEmergencyDataGetter.DataCallback {

    private FavoritesEmergencyActivity view;
    private FavoritesEmergencyDataGetter dataGetter;

    public FavoritesEmergencyPresenter(FavoritesEmergencyActivity view) {
        this.view = view;
        dataGetter = new FavoritesEmergencyDataGetter(view, this, view.host);
    }

    public void getFavoritesEmergencys() {
        dataGetter.queryFavoritesEmergencys();
    }

    public void deleteSelectedEmergencys(List<DbEmergency> selectedItems) {
        dataGetter.deleteSelectedEmergencys(selectedItems);
    }

    public void deleteSelectedEmergency(int position, DbEmergency selectedItem) {
        dataGetter.deleteSelectedEmergency(position, selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DbEmergency> data) {
        view.bindEmergencyListViews(data);
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
