package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ads.builder.BannerAdsBuilder;
import org.wdd.app.android.seedoctor.app.SDApplication;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDepartmentActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDiseaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDoctorActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugCategoryActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiEmergencyActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiHospitalActivity;
import org.wdd.app.android.seedoctor.utils.AppUtils;
import org.wdd.app.android.seedoctor.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends Fragment implements View.OnClickListener {

    private ViewGroup mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_wiki, container, false);
            initTitle();
            initView();
        }
        if (mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
        return mRootView;
    }

    private void initTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View spaceView = mRootView.getChildAt(0);
            spaceView.setVisibility(View.VISIBLE);
            spaceView.getLayoutParams().height = AppUtils.getStatusHeight(getActivity());
        }
    }

    private void initView() {
        mRootView.findViewById(R.id.fragment_wiki_disease_clickable).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_wiki_drug_clickable).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_wiki_emergency_clickable).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_wiki_department_clickable).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_wiki_doctor_clickable).setOnClickListener(this);
        mRootView.findViewById(R.id.fragment_wiki_hospital_clickable).setOnClickListener(this);

        if (SDApplication.getInstance().isAdsOpen()) {
            RelativeLayout bannerContainer = (RelativeLayout) mRootView.findViewById(R.id.fragment_wiki_banner_container);
            BannerAdsBuilder adsBuilder = new BannerAdsBuilder(getActivity(), bannerContainer, Constants.WIKI_HOME_AD_ID, true);
            adsBuilder.addBannerAds();
        }
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
