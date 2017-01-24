package org.wdd.app.android.seedoctor.ui.me.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DrugDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDrug;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

import java.util.List;

/**
 * Created by richard on 1/24/17.
 */

public class FavoritesDrugDataGetter {

    private Context context;
    private DrugDbManager dbManager;
    private ActivityFragmentAvaliable host;
    private DataCallback callback;

    public FavoritesDrugDataGetter(Context context, DataCallback callback, ActivityFragmentAvaliable host) {
        this.context = context;
        this.callback = callback;
        this.host = host;
        dbManager = new DrugDbManager(context);
    }

    public void queryFavoritesDrugs() {
        Thread thread = new Thread(new QueryAction());
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDrugs(List<DbDrug> selectedItems) {
        Thread thread = new Thread(new DeleteItemsAction(selectedItems));
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteSelectedDrug(int position, DbDrug selectedItem) {
        Thread thread = new Thread(new DeleteItemAction(position, selectedItem));
        thread.setDaemon(true);
        thread.start();
    }

    private class DeleteItemsAction implements Runnable {

        private List<DbDrug> drugs;

        public DeleteItemsAction(List<DbDrug> drugs) {
            this.drugs = drugs;
        }

        @Override
        public void run() {
            dbManager.deleteDrugs(drugs);
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
        private DbDrug drug;

        public DeleteItemAction(int position, DbDrug drug) {
            this.position = position;
            this.drug = drug;
        }

        @Override
        public void run() {
            dbManager.deleteById(drug.id);
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
            final List<DbDrug> result = dbManager.queryAll();
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

        void onDataGetted(List<DbDrug> data);
        void onNoDataGetted();
        void onDeleteSelectedData();
        void onDeleteSelectedData(int position);

    }
}
