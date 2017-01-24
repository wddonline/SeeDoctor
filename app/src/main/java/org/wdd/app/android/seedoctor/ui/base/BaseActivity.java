package org.wdd.app.android.seedoctor.ui.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.app.ActivityTaskStack;

/**
 * Created by wangdd on 16-11-26.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public ActivityFragmentAvaliable host = new ActivityFragmentAvaliable(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTaskStack.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskStack.getInstance().removeActivity(this);
    }

    public void showLoadingDialog(String msg) {
        showLoadingDialog(msg, null);
    }

    public void showLoadingDialog(int msg) {
        showLoadingDialog(getString(msg), null);
    }

    protected void showLoadingDialog(String msg, DialogInterface.OnCancelListener listener) {
        if (!host.isAvaliable()) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        if (listener != null) progressDialog.setOnCancelListener(listener);
        progressDialog.show();
    }

    protected void hideLoadingDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void showMessageDialog(String msg) {
        if (!host.isAvaliable()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.confirm, null);
        Dialog dialog = builder.create();
        dialog.show();
    }
}
