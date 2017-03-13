package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.HospitalDbManager;
import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.HospitalDetail;
import org.wdd.app.android.seedoctor.utils.HttpUtils;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class HospitalDetailGetter {

    private Context context;
    private HospitalDetailCallback callback;
    private ActivityFragmentAvaliable host;
    private HospitalDbManager dbManager;
    private HttpManager manager;

    public HospitalDetailGetter(ActivityFragmentAvaliable host, Context context, HospitalDetailCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
        dbManager = new HospitalDbManager(context);
    }

    public HttpSession requestHospitalDetailData(String hospitalid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.HOSPITAL_DETAIL);
        requestEntry.addRequestParam("hospitalid", hospitalid);
        HttpSession session = manager.sendHttpRequest(host, requestEntry, HospitalDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    HospitalDetail hospitalDetail = (HospitalDetail) res.getData();
                    callback.onRequestOk(hospitalDetail);
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, ErrorCode.SERVER_ERROR));
                }
            }

            @Override
            public void onRequestFailure(HttpError error) {
                if (callback == null) return;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    callback.onNetworkError();
                } else {
                    callback.onRequestFailure(HttpUtils.getErrorDescFromErrorCode(context, error.getErrorCode()));
                }
            }

        });
        return session;
    }

    public void getCollectionStatus(String hospitalid) {
        Thread thread = new Thread(new HospitalDetailGetter.GetCollectionStatusAction(hospitalid));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectHospital(String hospitalid, String hospitalname, String picurl) {
        Thread thread = new Thread(new HospitalDetailGetter.CollectHospitalAction(hospitalid, hospitalname, picurl));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectHospital(String hospitalid) {
        Thread thread = new Thread(new HospitalDetailGetter.UncollectHospitalAction(hospitalid));
        thread.setDaemon(true);
        thread.start();
    }

    private class GetCollectionStatusAction implements Runnable {

        private String hospitalid;

        public GetCollectionStatusAction(String hospitalid) {
            this.hospitalid = hospitalid;
        }

        @Override
        public void run() {
            final DbHospital hospital = dbManager.getHospitaByHospitaid(hospitalid);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectionStatusGetted(hospital != null);
                }
            });
        }
    }

    private class CollectHospitalAction implements Runnable {

        private String hospitalid;
        private String hospitalname;
        private String picurl;

        public CollectHospitalAction(String hospitalid, String hospitalname, String picurl) {
            this.hospitalid = hospitalid;
            this.hospitalname = hospitalname;
            this.picurl = picurl;
        }

        @Override
        public void run() {
            DbHospital hospital = new DbHospital(hospitalid, hospitalname, picurl);
            final long result = dbManager.insert(hospital);
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

    private class UncollectHospitalAction implements Runnable {

        private String hospitalid;

        public UncollectHospitalAction(String hospitalid) {
            this.hospitalid = hospitalid;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByHospitalid(hospitalid);
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

    public interface HospitalDetailCallback {

        void onRequestOk(HospitalDetail data);
        void onRequestFailure(String error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
