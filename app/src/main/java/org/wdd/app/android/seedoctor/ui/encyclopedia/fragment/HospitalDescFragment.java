package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.NativeAdsBuilder;
import org.wdd.app.android.seedoctor.ui.base.BaseFragment;

/**
 * Created by richard on 1/6/17.
 */

public class HospitalDescFragment extends BaseFragment {

    public static final String KEY_DESC = "desc";

    private View rootView;

    private String hospitalDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalDesc = getArguments().getString(KEY_DESC);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = View.inflate(getContext(), R.layout.fragment_hospital_desc, null);
            TextView descView = (TextView) rootView.findViewById(R.id.fragment_hospital_desc_txt);
            descView.setText(hospitalDesc);

            new NativeAdsBuilder(getActivity(), (ViewGroup) rootView.findViewById(R.id.fragment_hospital_desc_container), "");

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void lazyLoad() {

    }
}
