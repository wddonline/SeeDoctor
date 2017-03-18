package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DoctorDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDoctorDataGetter {

    private Context context;
    private DoctorDbManager dbManager;
    private ActivityFragmentAvaliable host;
    private DataCallback callback;

    public FavoritesDoctorDataGetter(Context context, DataCallback callback, ActivityFragmentAvaliable host) {
        this.context = context;
        this.callback = callback;
        this.host = host;
        dbManager = new DoctorDbManager(context);
    }

    public void queryFavoritesDoctors() {
        Thread thread = new Thread(new QueryAction());
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDoctors(List<DbDoctor> selectedItems) {
        Thread thread = new Thread(new DeleteItemsAction(selectedItems));
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDoctor(DbDoctor selectedItem) {
        Thread thread = new Thread(new DeleteItemAction(selectedItem));
        thread.setDaemon(true);
        thread.start();
    }

    private class DeleteItemsAction implements Runnable {

        private List<DbDoctor> doctors;

        public DeleteItemsAction(List<DbDoctor> doctors) {
            this.doctors = doctors;
        }

        @Override
        public void run() {
            dbManager.deleteDoctors(doctors);
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

        private DbDoctor doctor;

        public DeleteItemAction(DbDoctor doctor) {
            this.doctor = doctor;
        }

        @Override
        public void run() {
            dbManager.deleteById(doctor.id);
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (!host.isAvaliable()) return;
                    callback.onDeleteSelectedData(doctor.id);
                }
            });
        }
    }

    private class QueryAction implements Runnable {

        @Override
        public void run() {
            final List<DbDoctor> result = dbManager.queryAll();
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

        void onDataGetted(List<DbDoctor> data);
        void onNoDataGetted();
        void onDeleteSelectedData();
        void onDeleteSelectedData(int position);

    }
}
