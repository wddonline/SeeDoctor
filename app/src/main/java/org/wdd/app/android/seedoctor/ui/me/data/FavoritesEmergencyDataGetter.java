package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.EmergencyDbManager;
import org.wdd.app.android.seedoctor.database.model.DbEmergency;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesEmergencyDataGetter {

    private Context context;
    private EmergencyDbManager dbManager;
    private ActivityFragmentAvaliable host;
    private DataCallback callback;

    public FavoritesEmergencyDataGetter(Context context, DataCallback callback, ActivityFragmentAvaliable host) {
        this.context = context;
        this.callback = callback;
        this.host = host;
        dbManager = new EmergencyDbManager(context);
    }

    public void queryFavoritesEmergencys() {
        Thread thread = new Thread(new QueryAction());
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedEmergencys(List<DbEmergency> selectedItems) {
        Thread thread = new Thread(new DeleteItemsAction(selectedItems));
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedEmergency(int position, DbEmergency selectedItem) {
        Thread thread = new Thread(new DeleteItemAction(position, selectedItem));
        thread.setDaemon(true);
        thread.start();
    }

    private class DeleteItemsAction implements Runnable {

        private List<DbEmergency> emergencys;

        public DeleteItemsAction(List<DbEmergency> emergencys) {
            this.emergencys = emergencys;
        }

        @Override
        public void run() {
            dbManager.deleteEmergencys(emergencys);
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
        private DbEmergency emergency;

        public DeleteItemAction(int position, DbEmergency emergency) {
            this.position = position;
            this.emergency = emergency;
        }

        @Override
        public void run() {
            dbManager.deleteById(emergency.id);
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
            final List<DbEmergency> result = dbManager.queryAll();
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

        void onDataGetted(List<DbEmergency> data);
        void onNoDataGetted();
        void onDeleteSelectedData();
        void onDeleteSelectedData(int position);

    }
}
