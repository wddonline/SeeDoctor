package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DrugDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDrug;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DrugDetailGetter {

    private Context context;
    private DrugDetailCallback callback;
    private ActivityFragmentAvaliable host;
    private HttpManager manager;
    private DrugDbManager dbManager;
    private HttpSession session;

    public DrugDetailGetter(ActivityFragmentAvaliable host, Context context, DrugDetailCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
        dbManager = new DrugDbManager(context);
    }

    public void requestDrugDetailData(String drugId) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.WIKI_DRUG_DETAIL);
        requestEntry.addRequestParam("drugid", drugId);
        manager.sendHttpRequest(host, requestEntry, DrugDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                session = null;
                if (res.getData() != null) {
                    DrugDetail diseaseDetail = (DrugDetail) res.getData();
                    callback.onRequestOk(diseaseDetail);
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                session = null;
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError();
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }

        });
    }

    public void cancelRequest() {
        if (session == null) return;
        session.cancelRequest();
        session = null;
    }

    public void getCollectionStatus(String drugid) {
        Thread thread = new Thread(new DrugDetailGetter.GetCollectionStatusAction(drugid));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectDrug(String drugid, String drugname) {
        Thread thread = new Thread(new CollectDrugAction(drugid, drugname));
        thread.setDaemon(true);
        thread.start();
    }

    public void uncollectDrug(String drugid) {
        Thread thread = new Thread(new UncollectDrugAction(drugid));
        thread.setDaemon(true);
        thread.start();
    }

    private class GetCollectionStatusAction implements Runnable {

        private String drugid;

        public GetCollectionStatusAction(String drugid) {
            this.drugid = drugid;
        }

        @Override
        public void run() {
            final DbDrug drug = dbManager.getDrugByDrugid(drugid);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectionStatusGetted(drug != null);
                }
            });
        }
    }

    private class CollectDrugAction implements Runnable {

        private String drugid;
        private String drugname;

        public CollectDrugAction(String drugid, String drugname) {
            this.drugid = drugid;
            this.drugname = drugname;
        }

        @Override
        public void run() {
            DbDrug drug = new DbDrug(drugid, drugname);
            final long result = dbManager.insert(drug);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectOver(result != -1);
                }
            });
        }
    }

    private class UncollectDrugAction implements Runnable {

        private String drugid;

        public UncollectDrugAction(String drugid) {
            this.drugid = drugid;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByDrugid(drugid);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onUncollectOver(result != -1);
                }
            });
        }
    }

    public interface DrugDetailCallback {

        void onRequestOk(DrugDetail data);
        void onRequestFailure(String error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
