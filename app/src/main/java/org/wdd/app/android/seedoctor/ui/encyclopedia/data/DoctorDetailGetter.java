package org.wdd.app.android.seedoctor.ui.encyclopedia.data;

import android.content.Context;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.database.manager.impl.DoctorDbManager;
import org.wdd.app.android.seedoctor.database.model.DbDepartment;
import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpManager;
import org.wdd.app.android.seedoctor.http.HttpRequestEntry;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.HttpSession;
import org.wdd.app.android.seedoctor.http.error.ErrorCode;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DoctorDetail;
import org.wdd.app.android.seedoctor.utils.ServiceApi;

/**
 * Created by richard on 12/20/16.
 */

public class DoctorDetailGetter {

    private Context context;
    private ActivityFragmentAvaliable host;
    private DoctorDetailCallback callback;
    private DoctorDbManager dbManager;
    private HttpManager manager;

    public DoctorDetailGetter(ActivityFragmentAvaliable host, Context context, DoctorDetailCallback callback) {
        this.host = host;
        this.context = context;
        this.callback = callback;
        manager = HttpManager.getInstance(context);
        dbManager = new DoctorDbManager(context);
    }

    public HttpSession requestDoctorDetailData(String doctorid) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setUrl(ServiceApi.DOCTOR_DETAIL);
        requestEntry.addRequestParam("doctorid", doctorid);
        HttpSession session = manager.sendHttpRequest(host, requestEntry, DoctorDetail.class, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                if (res.getData() != null) {
                    DoctorDetail detail = (DoctorDetail) res.getData();
                    callback.onRequestOk(detail);
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

    public void getCollectionStatus(String doctorid) {
        Thread thread = new Thread(new DoctorDetailGetter.GetCollectionStatusAction(doctorid));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectDoctor(String doctorid, String doctorname, String photourl) {
        Thread thread = new Thread(new DoctorDetailGetter.CollectDoctorAction(doctorid, doctorname, photourl));
        thread.setDaemon(true);
        thread.start();
    }

    public void collectDoctor(String doctorid) {
        Thread thread = new Thread(new DoctorDetailGetter.UncollectDoctorAction(doctorid));
        thread.setDaemon(true);
        thread.start();
    }

    private class GetCollectionStatusAction implements Runnable {

        private String doctorid;

        public GetCollectionStatusAction(String doctorid) {
            this.doctorid = doctorid;
        }

        @Override
        public void run() {
            final DbDoctor doctor = dbManager.getDoctorByDoctorid(doctorid);
            if (callback == null) return;
            if (!host.isAvaliable()) return;
            SDApplication.getInstance().getUiHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onCollectionStatusGetted(doctor != null);
                }
            });
        }
    }

    private class CollectDoctorAction implements Runnable {

        private String doctorid;
        private String doctorname;
        private String photourl;

        public CollectDoctorAction(String doctorid, String doctorname, String photourl) {
            this.doctorid = doctorid;
            this.doctorname = doctorname;
            this.photourl = photourl;
        }

        @Override
        public void run() {
            DbDoctor doctor = new DbDoctor(doctorid, doctorname, photourl);
            final long result = dbManager.insert(doctor);
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

    private class UncollectDoctorAction implements Runnable {

        private String doctorid;

        public UncollectDoctorAction(String doctorid) {
            this.doctorid = doctorid;
        }

        @Override
        public void run() {
            final long result = dbManager.deleteByDoctorid(doctorid);
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


    public interface DoctorDetailCallback {

        void onRequestOk(DoctorDetail data);
        void onRequestFailure(HttpError error);
        void onNetworkError();

        void onCollectionStatusGetted(boolean isCollected);
        void onCollectOver(boolean success);
        void onUncollectOver(boolean success);

    }
}
