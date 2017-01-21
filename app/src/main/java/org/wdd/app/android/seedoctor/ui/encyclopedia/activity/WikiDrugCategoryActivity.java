package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.DrugSecondCategaryFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.fragment.DrugThirdCategoryFragment;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiDrugCategoryPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WikiDrugCategoryActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, WikiDrugCategoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoadView loadView;

    private WikiDrugCategoryPresenter presenter;
    private List<Drug> drugs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_drug_category);
        intData();
        initTitles();
        initView();
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_drug_category_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void intData() {
        presenter = new WikiDrugCategoryPresenter(host, this);
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.activity_wiki_drug_category_tablayout);
        viewPager = (ViewPager) findViewById(R.id.activity_wiki_drug_category_viewpager);
        loadView = (LoadView) findViewById(R.id.activity_wiki_drug_category_loadview);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDrugCategoryListData();
            }
        });

        loadView.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getDrugCategoryListData();
            }
        }, 500);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showDiseaseListData(Map<String, Map<String, List<DrugCategory>>> data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);

        DrugCategoryAdapter adapter = new DrugCategoryAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(adapter);
    }

    public void showRequetErrorView(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorView() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showNoDiseaseListResult() {
        loadView.setStatus(LoadView.LoadStatus.No_Data);
    }

    private class DrugCategoryAdapter extends FragmentPagerAdapter {

        private List<String> names;
        private Map<String, Map<String, List<DrugCategory>>> data;

        public DrugCategoryAdapter(FragmentManager fm, Map<String, Map<String, List<DrugCategory>>> data) {
            super(fm);
            names = new ArrayList<>();
            Set<String> set = data.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                names.add(iterator.next());
            }
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            String firstCategory = names.get(position);
            Map<String, List<DrugCategory>> secondCategories = data.get(firstCategory);
            if (secondCategories.size() == 1 && secondCategories.keySet().iterator().next().equals(firstCategory)) {
                DrugThirdCategoryFragment thirdCategoryFragment = new DrugThirdCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("categories", (ArrayList)secondCategories.get(secondCategories.keySet().iterator().next()));
                thirdCategoryFragment.setArguments(bundle);
                fragment = thirdCategoryFragment;
            } else {
                DrugSecondCategaryFragment secondCategaryFragment = new DrugSecondCategaryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("categories", (LinkedHashMap)data.get(firstCategory));
                secondCategaryFragment.setArguments(bundle);
                fragment = secondCategaryFragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names.get(position);
        }
    }
}
