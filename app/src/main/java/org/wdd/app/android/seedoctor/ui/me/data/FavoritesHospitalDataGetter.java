package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.HospitalDbManager;
import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesHospitalDataGetter {

    private Context context;
    private HospitalDbManager dbManager;
    private ActivityFragmentAvaliable host;
    private DataCallback callback;

    public FavoritesHospitalDataGetter(Context context, DataCallback callback, ActivityFragmentAvaliable host) {
        this.context = context;
        this.callback = callback;
        this.host = host;
        dbManager = new HospitalDbManager(context);
    }

    public void queryFavoritesHospitals() {
        Thread thread = new Thread(new QueryAction());
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedHospitals(List<DbHospital> selectedItems) {
        Thread thread = new Thread(new DeleteItemsAction(selectedItems));
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedHospital(int position, DbHospital selectedItem) {
        Thread thread = new Thread(new DeleteItemAction(position, selectedItem));
        thread.setDaemon(true);
        thread.start();
    }

    private class DeleteItemsAction implements Runnable {

        private List<DbHospital> hospitals;

        public DeleteItemsAction(List<DbHospital> hospitals) {
            this.hospitals = hospitals;
        }

        @Override
        public void run() {
            dbManager.deleteHospitals(hospitals);
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

        private int position;
        private DbHospital hospital;

        public DeleteItemAction(int position, DbHospital hospital) {
            this.position = position;
            this.hospital = hospital;
        }

        @Override
        public void run() {
            dbManager.deleteById(hospital.id);
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (!host.isAvaliable()) return;
                    callback.onDeleteSelectedData(position);
                }
            });
        }
    }

    private class QueryAction implements Runnable {

        @Override
        public void run() {
            final List<DbHospital> result = dbManager.queryAll();
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

        void onDataGetted(List<DbHospital> data);
        void onNoDataGetted();
        void onDeleteSelectedData();
        void onDeleteSelectedData(int position);

    }
}
