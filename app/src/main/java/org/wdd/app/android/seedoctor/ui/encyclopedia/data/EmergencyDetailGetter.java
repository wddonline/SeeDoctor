package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.EmergencyDbManager;
import org.wdd.app.android.seedoctor.database.model.DbEmergency;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Emergency;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 1/3/17.
 */

public class EmergencyDetailGetter {

    private Context context;
    private EmergencyCallback callback;
    private EmergencyDbManager dbManager;
    private ActivityFragmentAvaliable host;

    public EmergencyDetailGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
        dbManager = new EmergencyDbManager(context);
    }

    public HttpSession requestEmergencyData(String emeid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.EMERGENCY_DETAIL);
        requestEntry.addRequestParam("emeid", emeid);
        HttpSession session = HttpManager.getInstance(context).sendHttpRequest(host, requestEntry, Emergency.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (callback == null) return;
                if (res.getData() == null) {
                    callback.onFailure(new HttpError(ErrorCode.UNKNOW_ERROR, ""));
                } else {
                    callback.onDataGetted((Emergency) res.getData());
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback == null) return;
                callback.onFailure(error);
            }

            @Override
            public void onNetworkError() {
                if (callback == null) return;
                callback.onNetworkError();
            }
        });
        return session;
    }

    public void getCollectionStatus(String emeid) {
        Thread thread = new Thread(new EmergencyDetailGetter.GetCollectionStatusAction(emeid));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectEmergency(String emeid, String eme) {
        Thread thread = new Thread(new CollectEmergencyAction(emeid, eme));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectEmergency(String emeid) {
        Thread thread = new Thread(new UncollectEmergencyAction(emeid));
        thread.setDaemon(true);
        thread.start();
    }

    public void setEmergencyCallback(EmergencyCallback callback) {
        this.callback = callback;
    }

    private class GetCollectionStatusAction implements Runnable {

        private String emeid;

        public GetCollectionStatusAction(String emeid) {
            this.emeid = emeid;
        }

        @Override
        public void run() {
            final DbEmergency department = dbManager.getEmergencyByEmeid(emeid);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectionStatusGetted(department != null);
                }
            });
        }
    }

    private class CollectEmergencyAction implements Runnable {

        private String emeid;
        private String eme;

        public CollectEmergencyAction(String emeid, String eme) {
            this.emeid = emeid;
            this.eme = eme;
        }

        @Override
        public void run() {
            DbEmergency emergency = new DbEmergency(emeid, eme);
            final long result = dbManager.insert(emergency);
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

    private class UncollectEmergencyAction implements Runnable {

        private String emeid;

        public UncollectEmergencyAction(String emeid) {
            this.emeid = emeid;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByEmeid(emeid);
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

    public interface EmergencyCallback {

        void onDataGetted(Emergency data);
        void onFailure(HttpError error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
