package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DBNews;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesNewsActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesNewsDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesNewsPresenter implements BasePresenter, FavoritesNewsDataGetter.DataCallback {

    private FavoritesNewsActivity view;
    private FavoritesNewsDataGetter dataGetter;

    public FavoritesNewsPresenter(FavoritesNewsActivity view) {
        this.view = view;
        dataGetter = new FavoritesNewsDataGetter(view, this, view.host);
    }

    public void getFavoritesNewses() {
        dataGetter.queryFavoritesNewses();
    }

    public void deleteSelectedNewses(List<DBNews> selectedItems) {
        dataGetter.deleteSelectedNewses(selectedItems);
    }

    public void deleteSelectedNews(DBNews selectedItem) {
        dataGetter.deleteSelectedNews(selectedItem);
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void onDataGetted(List<DBNews> data) {
        view.bindNewsListViews(data);
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
