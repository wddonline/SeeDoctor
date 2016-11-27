package org.wdd.app.android.seedoctor.ui.doctor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.http.HttpConnectCallback;
import org.wdd.app.android.seedoctor.http.HttpResponseEntry;
import org.wdd.app.android.seedoctor.http.error.HttpError;
import org.wdd.app.android.seedoctor.utils.CommonHttpMethod;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {

    public static DoctorFragment newInstance() {
        DoctorFragment fragment = new DoctorFragment();
        return fragment;
    }

    public DoctorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonHttpMethod.getDoctorList(getContext(), 1, 20, new HttpConnectCallback() {

            @Override
            public void onRequestOk(HttpResponseEntry res) {

            }

            @Override
            public void onRequestFailure(HttpError error) {

            }

            @Override
            public void onNetworkError() {

            }
        });
        return inflater.inflate(R.layout.fragment_doctor, container, false);
    }

}
