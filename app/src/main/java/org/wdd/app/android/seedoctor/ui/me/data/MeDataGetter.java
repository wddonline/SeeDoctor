package org.wdd.app.android.seedoctor.ui.me.data;

import android.app.Activity;
import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;

/**
 * Created by richard on 1/20/17.
 */

public class MeDataGetter {

    private Activity activity;
    private DataCallbck callbck;
    private ActivityFragmentAvaliable host;

    public MeDataGetter(ActivityFragmentAvaliable host, Activity activity, DataCallbck callbck) {
        this.host = host;
        this.activity = activity;
        this.callbck = callbck;
    }

    public void checkVersion() {
//        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//            @Override
//            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
//                if (!host.isAvaliable()) return;
//                callbck.onUpdateReturned(updateStatus, updateInfo);
//            }
//        });
        UmengUpdateAgent.forceUpdate(activity);
    }

    public interface DataCallbck {

        void onUpdateReturned(int updateStatus,UpdateResponse updateInfo);

    }
}
