package org.wdd.app.android.seedoctor.ui.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.amap.api.services.core.PoiItem;

import org.wdd.app.android.seedoctor.R;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;

    private boolean inited = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) return;
        if (inited) return;
        inited = true;
        lazyLoad();
    }

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    protected void showLoadingDialog(DialogInterface.OnCancelListener listener) {
        if (getActivity() == null) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.locating));
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
        if (getActivity() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tip);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.confirm, null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    protected abstract void lazyLoad();



}
