package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DiseaseDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DiseaseDetailPresenter;
import org.wdd.app.android.seedoctor.views.LoadView;

public class DiseaseDetailActivity extends BaseActivity implements View.OnClickListener {

    public static void show(Context context, int diseaseid, String diseasename) {
        Intent intent = new Intent(context, DiseaseDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("diseaseid", diseaseid);
        intent.putExtra("diseasename", diseasename);
        context.startActivity(intent);
    }

    private LoadView loadView;
    private ImageView[] arrowViews;
    private TextView[] labelViews;
    private TextView[] textViews;

    private DiseaseDetailPresenter presenter;
    private Animation openAnim;
    private Animation closeAnim;
    private int diseaseId;
    private String diseaseName;
    private boolean[] openStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        initData();
        initTitle();
        initView();
    }

    private void initData() {
        diseaseId = getIntent().getIntExtra("diseaseid", 0);
        diseaseName = getIntent().getStringExtra("diseasename");

        openStatus = new boolean[]{false, false, false, false, false, false, false, false};
        presenter = new DiseaseDetailPresenter(this);

        openAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        openAnim.setDuration(400);
        openAnim.setFillAfter(true);

        closeAnim = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        closeAnim.setDuration(400);
        closeAnim.setFillAfter(true);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_disease_detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                RelativeDrugListActivity.show(getBaseContext(), diseaseId + "", diseaseName);
                return true;
            }
        });

        TextView titleView = (TextView) findViewById(R.id.activity_disease_detail_title);
        titleView.setText(diseaseName);
    }

    private void initView() {
        findViewById(R.id.activity_disease_detail_brief_intro_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_pathogeny_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_symptom_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_check_method_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_distinguish_method_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_complication_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_prevention_click).setOnClickListener(this);
        findViewById(R.id.activity_disease_detail_treatment_method_click).setOnClickListener(this);

        labelViews = new TextView[8];
        labelViews[0] = (TextView) findViewById(R.id.activity_disease_detail_brief_intro_label);
        labelViews[1] = (TextView) findViewById(R.id.activity_disease_detail_pathogeny_label);
        labelViews[2] = (TextView) findViewById(R.id.activity_disease_detail_symptom_label);
        labelViews[3] = (TextView) findViewById(R.id.activity_disease_detail_check_method_label);
        labelViews[4] = (TextView) findViewById(R.id.activity_disease_detail_distinguish_method_label);
        labelViews[5] = (TextView) findViewById(R.id.activity_disease_detail_complication_label);
        labelViews[6] = (TextView) findViewById(R.id.activity_disease_detail_prevention_label);
        labelViews[7] = (TextView) findViewById(R.id.activity_disease_detail_treatment_method_label);

        arrowViews = new ImageView[8];
        arrowViews[0] = (ImageView) findViewById(R.id.activity_disease_detail_brief_intro_arrow);
        arrowViews[1] = (ImageView) findViewById(R.id.activity_disease_detail_pathogeny_arrow);
        arrowViews[2] = (ImageView) findViewById(R.id.activity_disease_detail_symptom_arrow);
        arrowViews[3] = (ImageView) findViewById(R.id.activity_disease_detail_check_method_arrow);
        arrowViews[4] = (ImageView) findViewById(R.id.activity_disease_detail_distinguish_method_arrow);
        arrowViews[5] = (ImageView) findViewById(R.id.activity_disease_detail_complication_arrow);
        arrowViews[6] = (ImageView) findViewById(R.id.activity_disease_detail_prevention_arrow);
        arrowViews[7] = (ImageView) findViewById(R.id.activity_disease_detail_treatment_method_arrow);

        textViews = new TextView[8];
        textViews[0] = (TextView) findViewById(R.id.activity_disease_detail_brief_intro);
        textViews[1] = (TextView) findViewById(R.id.activity_disease_detail_pathogeny);
        textViews[2] = (TextView) findViewById(R.id.activity_disease_detail_symptom);
        textViews[3] = (TextView) findViewById(R.id.activity_disease_detail_check_method);
        textViews[4] = (TextView) findViewById(R.id.activity_disease_detail_distinguish_method);
        textViews[5] = (TextView) findViewById(R.id.activity_disease_detail_complication);
        textViews[6] = (TextView) findViewById(R.id.activity_disease_detail_prevention);
        textViews[7] = (TextView) findViewById(R.id.activity_disease_detail_treatment_method);

        loadView = (LoadView) findViewById(R.id.activity_disease_detail_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDiseaseDetailData(diseaseId);
            }
        });

        presenter.getDiseaseDetailData(diseaseId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_disease_detail, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    public void showRequestErrorViews(String errorMsg) {
        loadView.setStatus(LoadView.LoadStatus.Request_Failure, errorMsg);
    }

    public void showNetworkErrorViews() {
        loadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showDiseaseDetalViews(DiseaseDetail data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_disease_detail_content).setVisibility(View.VISIBLE);
        textViews[0].setText(data.introduction);
        textViews[1].setText(data.cause);
        textViews[2].setText(data.clinical_manifestation);
        textViews[3].setText(data.lab_check);
        textViews[4].setText(data.differential_diagnosis);
        textViews[5].setText(data.complication);
        textViews[6].setText(data.prevention);
        textViews[7].setText(data.treatment);
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.activity_disease_detail_brief_intro_click:
                index = 0;
                break;
            case R.id.activity_disease_detail_pathogeny_click:
                index = 1;
                break;
            case R.id.activity_disease_detail_symptom_click:
                index = 2;
                break;
            case R.id.activity_disease_detail_check_method_click:
                index = 3;
                break;
            case R.id.activity_disease_detail_distinguish_method_click:
                index = 4;
                break;
            case R.id.activity_disease_detail_complication_click:
                index = 5;
                break;
            case R.id.activity_disease_detail_prevention_click:
                index = 6;
                break;
            case R.id.activity_disease_detail_treatment_method_click:
                index = 7;
                break;
        }
        openStatus[index] = !openStatus[index];
//        arrowViews[index].setImageResource(openStatus[index] ? R.mipmap.arrow_down : R.mipmap.arrow_right);
        labelViews[index].setSelected(openStatus[index]);
        textViews[index].setVisibility(openStatus[index] ? View.VISIBLE : View.GONE);

        arrowViews[index].clearAnimation();
        arrowViews[index].startAnimation(openStatus[index] ? openAnim : closeAnim);
    }
}