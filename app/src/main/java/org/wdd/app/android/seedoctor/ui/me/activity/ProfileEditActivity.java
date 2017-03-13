package org.wdd.app.android.seedoctor.ui.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.AppConfManager;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.utils.DensityUtils;

public class ProfileEditActivity extends BaseActivity {

    private static String SHARED_ELEMENT_NAME = "sharedview";

    public static void showForResult(Activity activity, View sharedView, int requestCode) {

        ViewCompat.setTransitionName(sharedView, SHARED_ELEMENT_NAME);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, SHARED_ELEMENT_NAME).toBundle();
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, bundle);
    }

    private RadioGroup sexGroup;
    private EditText nameEdit;

    private AppConfManager confManager;
    private String initNickname;
    private String initSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        initData();
        initTitle();
        initViews();
    }

    private void initData() {
        confManager = AppConfManager.getInstance(getBaseContext());

        initNickname = confManager.getNickname();
        initSex = confManager.getSex();
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_profie_edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);
        ViewCompat.setElevation(toolbar, DensityUtils.dip2px(this, 3));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(ProfileEditActivity.this);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_profile_edit_confirm:
                        String nickname = nameEdit.getText().toString();
                        confManager.saveNickname(nickname);
                        int id = sexGroup.getCheckedRadioButtonId();
                        String sex = "0";
                        switch (id) {
                            case R.id.activity_profie_edit_male:
                                sex = "0";
                                break;
                            case R.id.activity_profie_edit_female:
                                sex = "1";
                                break;
                        }
                        confManager.saveSex(sex);
                        if (!initNickname.equals(nickname) || !initSex.equals(sex)) {
                            setResult(RESULT_OK);
                        }
                        ActivityCompat.finishAfterTransition(ProfileEditActivity.this);
                        break;
                }
                return false;
            }
        });
    }

    private void initViews() {
        nameEdit = (EditText) findViewById(R.id.activity_profie_edit_name);
        final ImageView headimgView = (ImageView) findViewById(R.id.activity_profie_edit_headimg);
        ViewCompat.setTransitionName(headimgView, SHARED_ELEMENT_NAME);
        sexGroup = (RadioGroup) findViewById(R.id.activity_profie_edit_sex);
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.activity_profie_edit_male:
                        headimgView.setImageResource(R.drawable.ic_male_header);
                        break;
                    case R.id.activity_profie_edit_female:
                        headimgView.setImageResource(R.drawable.ic_female_header);
                        break;
                }
            }
        });

        if (!TextUtils.isEmpty(initNickname)) {
            nameEdit.setText(initNickname);
        }
        if (initSex.equals("0")) {
            sexGroup.check(R.id.activity_profie_edit_male);
        } else {
            sexGroup.check(R.id.activity_profie_edit_female);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }
}
