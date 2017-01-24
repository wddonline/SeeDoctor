package org.wdd.app.android.seedoctor.ui.encyclopedia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
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
import org.wdd.app.android.seedoctor.ui.encyclopedia.model.DrugDetail;
import org.wdd.app.android.seedoctor.ui.encyclopedia.presenter.DrugDetailPresenter;
import org.wdd.app.android.seedoctor.utils.DensityUtils;
import org.wdd.app.android.seedoctor.views.LoadView;

public class DrugDetailActivity extends BaseActivity implements View.OnClickListener {

    public static void show(Context context, String drugid, String drugname) {
        Intent intent = new Intent(context, DrugDetailActivity.class);
        intent.putExtra("drugid", drugid);
        intent.putExtra("drugname", drugname);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showForResult(Activity activity, int position, String doctorid, String doctorname, int requsetCode) {
        Intent intent = new Intent(activity, DrugDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("drugid", doctorid);
        intent.putExtra("drugname", doctorname);
        activity.startActivityForResult(intent, requsetCode);
    }

    private LoadView loadView;
    private Toolbar toolbar;

    private ImageView[] arrowViews;
    private TextView[] labelViews;
    private TextView[] textViews;

    private DrugDetailPresenter presenter;
    private Animation openAnim;
    private Animation closeAnim;
    private String drugid;
    private String drugname;
    private boolean[] openStatus;

    private int position;
    private boolean initCollectStatus = false;
    private boolean currentCollectStatus = initCollectStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail);
        initData();
        initTitle();
        initView();
    }

    private void initData() {
        position = getIntent().getIntExtra("position" , -1);
        drugid = getIntent().getStringExtra("drugid");
        drugname = getIntent().getStringExtra("drugname");

        openStatus = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        presenter = new DrugDetailPresenter(host, this);

        openAnim = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        openAnim.setDuration(400);
        openAnim.setFillAfter(true);

        closeAnim = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        closeAnim.setDuration(400);
        closeAnim.setFillAfter(true);
    }

    private void initTitle() {
        toolbar = (Toolbar) findViewById(R.id.activity_drug_detail_toolbar);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                finish();
            }
        });
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_disease_detail_sale:
                        RelativeDiseaseListActivity.showFromDrug(getBaseContext(), drugid + "", drugname);
                        return true;
                    case R.id.menu_collection_do:
                        showLoadingDialog(R.string.doing_background);
                        presenter.collectDrug(drugid, drugname);
                        return true;
                    case R.id.menu_collection_undo:
                        showLoadingDialog(R.string.doing_background);
                        presenter.uncollectDrug(drugid);
                        return true;
                }
                return false;
            }
        });

        TextView titleView = (TextView) findViewById(R.id.activity_drug_detail_title);
        titleView.setText(drugname);
    }

    private void initView() {
        findViewById(R.id.activity_drug_detail_basic_info_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_usage_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_adr_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_contraindication_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_note_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_pregnantwomentaboo_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_childrentaboo_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_elderlytaboo_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_interaction_click).setOnClickListener(this);
        findViewById(R.id.activity_drug_detail_drugen_click).setOnClickListener(this);

        labelViews = new TextView[10];
        labelViews[0] = (TextView) findViewById(R.id.activity_drug_detail_basic_info_label);
        labelViews[1] = (TextView) findViewById(R.id.activity_drug_detail_usage_label);
        labelViews[2] = (TextView) findViewById(R.id.activity_drug_detail_adr_label);
        labelViews[3] = (TextView) findViewById(R.id.activity_drug_detail_contraindication_label);
        labelViews[4] = (TextView) findViewById(R.id.activity_drug_detail_note_label);
        labelViews[5] = (TextView) findViewById(R.id.activity_drug_detail_pregnantwomentaboo_label);
        labelViews[6] = (TextView) findViewById(R.id.activity_drug_detail_childrentaboo_label);
        labelViews[7] = (TextView) findViewById(R.id.activity_drug_detail_elderlytaboo);
        labelViews[8] = (TextView) findViewById(R.id.activity_drug_detail_interaction_label);
        labelViews[9] = (TextView) findViewById(R.id.activity_drug_detail_drugen_label);

        arrowViews = new ImageView[10];
        arrowViews[0] = (ImageView) findViewById(R.id.activity_drug_detail_basic_info_arrow);
        arrowViews[1] = (ImageView) findViewById(R.id.activity_drug_detail_usage_arrow);
        arrowViews[2] = (ImageView) findViewById(R.id.activity_drug_detail_adr_arrow);
        arrowViews[3] = (ImageView) findViewById(R.id.activity_drug_detail_contraindication_arrow);
        arrowViews[4] = (ImageView) findViewById(R.id.activity_drug_detail_note_arrow);
        arrowViews[5] = (ImageView) findViewById(R.id.activity_drug_detail_pregnantwomentaboo_arrow);
        arrowViews[6] = (ImageView) findViewById(R.id.activity_drug_detail_childrentaboo_arrow);
        arrowViews[7] = (ImageView) findViewById(R.id.activity_drug_detail_elderlytaboo_arrow);
        arrowViews[8] = (ImageView) findViewById(R.id.activity_drug_detail_interaction_arrow);
        arrowViews[9] = (ImageView) findViewById(R.id.activity_drug_detail_drugen_arrow);

        textViews = new TextView[10];
        textViews[0] = (TextView) findViewById(R.id.activity_drug_detail_basic_info);
        textViews[1] = (TextView) findViewById(R.id.activity_drug_detail_usage);
        textViews[2] = (TextView) findViewById(R.id.activity_drug_detail_adr);
        textViews[3] = (TextView) findViewById(R.id.activity_drug_detail_contraindication);
        textViews[4] = (TextView) findViewById(R.id.activity_drug_detail_note);
        textViews[5] = (TextView) findViewById(R.id.activity_drug_detail_pregnantwomentaboo);
        textViews[6] = (TextView) findViewById(R.id.activity_drug_detail_childrentaboo);
        textViews[7] = (TextView) findViewById(R.id.activity_drug_detail_elderlytaboo);
        textViews[8] = (TextView) findViewById(R.id.activity_drug_detail_interaction);
        textViews[9] = (TextView) findViewById(R.id.activity_drug_detail_drugen);

        loadView = (LoadView) findViewById(R.id.activity_drug_detail_loadview);
        loadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                presenter.getDrugDetailData(drugid);
            }
        });

        presenter.getDrugDetailData(drugid);
    }

    private void backAction() {
        if (currentCollectStatus != initCollectStatus) {
            Intent intent = new Intent();
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public void onBackPressed() {
        backAction();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drug_detail, menu);
        presenter.getCollectionStatus(drugid);
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

    public void showDrugDetalViews(DrugDetail data) {
        loadView.setStatus(LoadView.LoadStatus.Normal);
        findViewById(R.id.activity_drug_detail_content).setVisibility(View.VISIBLE);
        String introduction = getString(R.string.drug_detail_basic_info_value);
        textViews[0].setText(String.format(introduction, data.drugname, data.composition, data.drugattribute,
                data.indication, data.unit, data.companyname, data.storage, data.shelflife, data.codename));
        textViews[1].setText(data.usage);
        textViews[2].setText(data.drugcommonname);
        textViews[3].setText(data.contraindication);
        textViews[4].setText(data.note);
        textViews[5].setText(data.pregnantwomentaboo);
        textViews[6].setText(data.childrentaboo);
        textViews[7].setText(data.elderlytaboo);
        textViews[8].setText(data.interaction);
        textViews[9].setText(data.drugen);
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.activity_drug_detail_basic_info_click:
                index = 0;
                break;
            case R.id.activity_drug_detail_usage_click:
                index = 1;
                break;
            case R.id.activity_drug_detail_adr_click:
                index = 2;
                break;
            case R.id.activity_drug_detail_contraindication_click:
                index = 3;
                break;
            case R.id.activity_drug_detail_note_click:
                index = 4;
                break;
            case R.id.activity_drug_detail_pregnantwomentaboo_click:
                index = 5;
                break;
            case R.id.activity_drug_detail_childrentaboo_click:
                index = 6;
                break;
            case R.id.activity_drug_detail_elderlytaboo_click:
                index = 7;
                break;
            case R.id.activity_drug_detail_interaction_click:
                index = 8;
                break;
            case R.id.activity_drug_detail_drugen_click:
                index = 9;
                break;
        }
        openStatus[index] = !openStatus[index];
        labelViews[index].setSelected(openStatus[index]);
        textViews[index].setVisibility(openStatus[index] ? View.VISIBLE : View.GONE);

        arrowViews[index].clearAnimation();
        arrowViews[index].startAnimation(openStatus[index] ? openAnim : closeAnim);
    }

    public void setDrugCollectionViews(boolean isCollected) {
        initCollectStatus = isCollected;
        currentCollectStatus = isCollected;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(!isCollected);
        menu.findItem(R.id.menu_collection_undo).setVisible(isCollected);
    }

    public void updateDrugCollectedStatus(boolean success) {
        currentCollectStatus = true;
        hideLoadingDialog();
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(false);
        menu.findItem(R.id.menu_collection_undo).setVisible(true);
    }

    public void updateDrugUncollectedStatus(boolean success) {
        currentCollectStatus = false;
        hideLoadingDialog();
        if (!success) return;
        Menu menu = toolbar.getMenu();
        menu.findItem(R.id.menu_collection_do).setVisible(true);
        menu.findItem(R.id.menu_collection_undo).setVisible(false);
    }
}
