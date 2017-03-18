package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DepartmentDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDepartment;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDepartmentDataGetter {

    private Context context;
    private DepartmentDbManager dbManager;
    private ActivityFragmentAvaliable host;
    private DataCallback callback;

    public FavoritesDepartmentDataGetter(Context context, DataCallback callback, ActivityFragmentAvaliable host) {
        this.context = context;
        this.callback = callback;
        this.host = host;
        dbManager = new DepartmentDbManager(context);
    }

    public void queryFavoritesDepartments() {
        Thread thread = new Thread(new QueryAction());
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDepartments(List<DbDepartment> selectedItems) {
        Thread thread = new Thread(new DeleteItemsAction(selectedItems));
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDepartment(DbDepartment selectedItem) {
        Thread thread = new Thread(new DeleteItemAction(selectedItem));
        thread.setDaemon(true);
        thread.start();
    }

    private class DeleteItemsAction implements Runnable {

        private List<DbDepartment> departments;

        public DeleteItemsAction(List<DbDepartment> departments) {
            this.departments = departments;
        }

        @Override
        public void run() {
            dbManager.deleteDepartments(departments);
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (!host.isAvaliable()) return;
                    callback.onDeleteSelectedData();
                }
            });
        }
    }

    private class DeleteItemAction implements Runnable {

        private DbDepartment department;

        public DeleteItemAction(DbDepartment department) {
            this.department = department;
        }

        @Override
        public void run() {
            dbManager.deleteById(department.id);
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (!host.isAvaliable()) return;
                    callback.onDeleteSelectedData(department.id);
                }
            });
        }
    }

    private class QueryAction implements Runnable {

        @Override
        public void run() {
            final List<DbDepartment> result = dbManager.queryAll();
            if (result == null || result.size() == 0) {
                SDApplication.getInstance().getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!host.isAvaliable()) return;
                        callback.onNoDataGetted();
                    }
                });
                return;
            }
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (!host.isAvaliable()) return;
                    callback.onDataGetted(result);
                }
            });
        }
    }

    public interface DataCallback {

        void onDataGetted(List<DbDepartment> data);
        void onNoDataGetted();
        void onDeleteSelectedData();
        void onDeleteSelectedData(int id);

    }
}
