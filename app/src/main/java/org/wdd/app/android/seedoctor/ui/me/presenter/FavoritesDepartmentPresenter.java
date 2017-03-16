package org.wdd.app.android.seedoctor.ui.me.presenter;

import org.wdd.app.android.seedoctor.database.model.DbDepartment;
import org.wdd.app.android.seedoctor.ui.base.BasePresenter;
import org.wdd.app.android.seedoctor.ui.me.activity.FavoritesDepartmentActivity;
import org.wdd.app.android.seedoctor.ui.me.data.FavoritesDepartmentDataGetter;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDepartmentPresenter implements BasePresenter, FavoritesDepartmentDataGetter.DataCallback {

    private FavoritesDepartmentActivity view;
    private FavoritesDepartmentDataGetter dataGetter;

    public FavoritesDepartmentPresenter(FavoritesDepartmentActivity view) {
        this.view = view;
        dataGetter = new FavoritesDepartmentDataGetter(view, this, view.host);
    }

    public void getFavoritesDepartments() {
        dataGetter.queryFavoritesDepartments();
    }

    public void deleteSelectedDepartments(List<DbDepartment> selectedItems) {
        dataGetter.deleteSelectedDepartments(selectedItems);
    }

    public void deleteSelectedDepartment(int position, DbDepartment selectedItem) {
        dataGetter.deleteSelectedDepartment(position, selectedItem);
    }

    @Override
    public void cancelRequest() {
    }

    @Override
    public void onDataGetted(List<DbDepartment> data) {
        view.bindDepartmentListViews(data);
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
