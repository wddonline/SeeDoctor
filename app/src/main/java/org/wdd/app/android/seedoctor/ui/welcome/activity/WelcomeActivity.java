package org.wdd.app.android.seedoctor.ui.welcome.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.permission.PermissionManager;
import org.wdd.app.android.seedoctor.permission.PermissionListener;
import org.wdd.app.android.seedoctor.permission.Rationale;
import org.wdd.app.android.seedoctor.permission.RationaleListener;
import org.wdd.app.android.seedoctor.permission.SettingDialog;
import org.wdd.app.android.seedoctor.ui.base.BaseActivity;
import org.wdd.app.android.seedoctor.ui.main.activity.MainActivity;
import org.wdd.app.android.seedoctor.ui.welcome.presenter.WelcomePresenter;
import org.wdd.app.android.seedoctor.utils.Constants;

import java.util.List;

public class WelcomeActivity extends BaseActivity implements Runnable, PermissionListener, SplashADListener {

    private final int REQUEST_PERMISSION_CODE = 100;

    private RelativeLayout mAdsContainer;
    private SplashAD mSplashAD;
    private TextView mSkipView;

    private Handler mHandler = new Handler();
    private WelcomePresenter mPresenter;

    private boolean isCheckRequired = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();
        checkPermission();
    }

    private void initViews() {
        mAdsContainer = (RelativeLayout) findViewById(R.id.activity_welcome_ads_container);
        mSkipView = (TextView) findViewById(R.id.activity_welcome_skip);
    }

    private void checkPermission() {
        PermissionManager.with(this)
                .requestCode(REQUEST_PERMISSION_CODE)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        PermissionManager.rationaleDialog(WelcomeActivity.this, rationale).show();
                    }
                }).send();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCheckRequired) {
            checkPermission();
            isCheckRequired = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initData() {
        mPresenter = new WelcomePresenter(host, this);

        mPresenter.findLocation();
    }

    @Override
    public void run() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPresenter != null) {
            mPresenter.exitApp();
        }
        mHandler.removeCallbacks(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destory();
        }
    }

    public void showSplashAds() {
        mSplashAD = new SplashAD(this, mAdsContainer, mSkipView, Constants.TENCENT_APP_ID, Constants.SPLASH_AD_ID, this, 0);
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantedPermissions) {
        initData();
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        if (PermissionManager.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            PermissionManager.defaultSettingDialog(this)
                    .setSettingDialogListener(new SettingDialog.SettingDialogListener() {
                        @Override
                        public void onSettingClicked() {
                            isCheckRequired = true;
                        }

                        @Override
                        public void onCancelClicked() {
                            finish();
                        }
                    }).show();

            // 第二种：用自定义的提示语。
            // PermissionManager.defaultSettingDialog(this, REQUEST_CODE_SETTING)
            // .setTitle("权限申请失败")
            // .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
            // .setPositiveButton("好，去设置")
            // .show();

            // 第三种：自定义dialog样式。
            // SettingService settingService =
            //    PermissionManager.defineSettingDialog(this, REQUEST_CODE_SETTING);
            // 你的dialog点击了确定调用：
            // settingService.execute();
            // 你的dialog点击了取消调用：
            // settingService.cancel();
        } else {
            finish();
        }
    }

    @Override
    public void onADDismissed() {
        run();
    }

    @Override
    public void onNoAD(int i) {
        mHandler.postDelayed(this, 1000);
    }

    @Override
    public void onADPresent() {
        mSkipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long millisUntilFinished) {
        String skipText = getString(R.string.click_to_skip);
        mSkipView.setText(String.format(skipText, Math.round(millisUntilFinished / 1000f)));
    }
}
