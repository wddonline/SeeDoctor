package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DiseaseDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDisease;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DiseaseDetailGetter {

    private Context context;
    private ActivityFragmentAvaliable host;
    private DiseaseDbManager dbManager;
    private DiseaseDetailCallback callback;
    private HttpManager manager;

    public DiseaseDetailGetter(ActivityFragmentAvaliable host, Context context, DiseaseDetailCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        dbManager = new DiseaseDbManager(context);
        manager = HttpManager.getInstance(context);
    }

    public HttpSession requestDiseaseDetailData(String diseaseId) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.WIKI_DISEASE_DETAIL);
        requestEntry.addRequestParam("diseaseid", diseaseId);
        HttpSession session = manager.sendHttpRequest(host, requestEntry, DiseaseDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    DiseaseDetail diseaseDetail = (DiseaseDetail) res.getData();
                    callback.onRequestOk(diseaseDetail);
                } else {
                    HttpError error = new HttpError(ErrorCode.UNKNOW_ERROR, "");
                    callback.onRequestFailure(error);
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                callback.onRequestFailure(error);
            }

            @Override
            public void onNetworkError() {
                callback.onNetworkError();
            }
        });
        return session;
    }

    public void getCollectionStatus(String diseaseId) {
        Thread thread = new Thread(new DiseaseDetailGetter.GetCollectionStatusAction(diseaseId));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectDisease(String diseaseId, String diseasename) {
        Thread thread = new Thread(new CollectDiseaseAction(diseaseId, diseasename));
        thread.setDaemon(true);
        thread.start();
    }

    public void uncollectDisease(String diseaseId) {
        Thread thread = new Thread(new UncollectDiseaseAction(diseaseId));
        thread.setDaemon(true);
        thread.start();
    }

    private class GetCollectionStatusAction implements Runnable {

        private String diseaseId;

        public GetCollectionStatusAction(String diseaseId) {
            this.diseaseId = diseaseId;
        }

        @Override
        public void run() {
            final DbDisease disease = dbManager.getDiseaseByDiseaseid(diseaseId);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectionStatusGetted(disease != null);
                }
            });
        }
    }

    private class CollectDiseaseAction implements Runnable {

        private String diseaseid;
        private String diseasename;

        public CollectDiseaseAction(String diseaseid, String diseasename) {
            this.diseaseid = diseaseid;
            this.diseasename = diseasename;
        }

        @Override
        public void run() {
            DbDisease disease = new DbDisease(diseaseid, diseasename);
            final long result = dbManager.insert(disease);
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

    private class UncollectDiseaseAction implements Runnable {

        private String diseaseid;

        public UncollectDiseaseAction(String diseaseid) {
            this.diseaseid = diseaseid;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByDiseaseid(diseaseid);
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


    public interface DiseaseDetailCallback {

        void onRequestOk(DiseaseDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
