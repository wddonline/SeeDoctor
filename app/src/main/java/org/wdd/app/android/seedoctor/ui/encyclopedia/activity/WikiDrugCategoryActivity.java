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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.Drug;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugCategory;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.WikiDrugCategoryPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

import java.util.List;
import java.util.Map;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_drug_category);
        intData();
        initTitles();
        initView();
    }

    private void initTitles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_wiki_drug_category_title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void intData() {
        presenter = new WikiDrugCategoryPresenter(this);
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
        DrugCategoryAdapter adapter = new DrugCategoryAdapter(getSupportFragmentManager());
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

        public DrugCategoryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
