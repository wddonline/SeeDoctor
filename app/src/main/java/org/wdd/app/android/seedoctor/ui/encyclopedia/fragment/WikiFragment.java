package org.wdd.app.android.seedoctor.ui.encyclopedia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDepartmentActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDiseaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDoctorActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiDrugCategoryActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiEmergencyActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.activity.WikiHospitalActivity;
import org.wdd.app.android.seedoctor.utils.Constant;
import org.wdd.app.android.seedoctor.views.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RelativeLayout bannerContainer;
    private BannerView bannerView;

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
        bannerContainer = (RelativeLayout) rootView.findViewById(R.id.fragment_wiki_banner_container);
        bannerView = new BannerView(getActivity(), ADSize.BANNER, Constant.TENCENT_APP_ID, Constant.WIKI_HOME_BANNER_ADS);
        bannerView.setRefresh(30);
        bannerView.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {

            }

            @Override
            public void onADReceiv() {

            }
        });
        bannerContainer.addView(bannerView);
        bannerContainer.post(new Runnable() {
            @Override
            public void run() {
                bannerView.loadAD();
            }
        });

        rootView.findViewById(R.id.fragment_wiki_disease_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_drug_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_emergency_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_department_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_doctor_clickable).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_wiki_hospital_clickable).setOnClickListener(this);
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
