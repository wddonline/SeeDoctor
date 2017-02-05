package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.BannerAdsBuilder;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDepartmentActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDiseaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDoctorActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugCategoryActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiEmergencyActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiHospitalActivity;
import org.wdd.app.android.seedoctor.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wiki, container, false);
            initTitle();
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    private void initTitle() {

    }

    private void initView() {
        rootView.findViewById(R.id.fragment_wiki_disease_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_drug_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_emergency_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_department_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_doctor_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_hospital_clickable).setOnClickListener(this);

        RelativeLayout bannerContainer = (RelativeLayout) rootView.findViewById(R.id.fragment_wiki_banner_container);
        BannerAdsBuilder adsBuilder = new BannerAdsBuilder(getActivity(), bannerContainer, Constants.WIKI_HOME_AD_ID);
        adsBuilder.addBannerAds();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_wiki_disease_clickable:
                WikiDiseaseActivity.show(getContext());
                break;
            case R.id.fragment_wiki_drug_clickable:
                WikiDrugCategoryActivity.show(getContext());
                break;
            case R.id.fragment_wiki_emergency_clickable:
                WikiEmergencyActivity.show(getContext());
                break;
            case R.id.fragment_wiki_department_clickable:
                WikiDepartmentActivity.show(getContext());
                break;
            case R.id.fragment_wiki_doctor_clickable:
                WikiDoctorActivity.show(getContext());
                break;
            case R.id.fragment_wiki_hospital_clickable:
                WikiHospitalActivity.show(getContext());
        }
    }

}
