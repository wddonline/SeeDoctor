package org.wdd.app.android.seedoctor.ui.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.seedoctor.R;

/**
 * Created by richard on 11/28/16.
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    public ActivityFragmentAvaliable host = new ActivityFragmentAvaliable(this);

    private boolean dataLoaded = false;
    private boolean viewIninted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        viewIninted = true;
        if (dataLoaded) return view;
        if (!getUserVisibleHint()) return view;
        lazyLoad();
        dataLoaded = true;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) return;
        if (!viewIninted) return;
        if (dataLoaded) return;
        lazyLoad();
        dataLoaded = true;
    }

    public void showLoadingDialog(String msg) {
        showLoadingDialog(msg, null);
    }

    public void showLoadingDialog(int msg) {
        showLoadingDialog(getString(msg), null);
    }

    protected void showLoadingDialog(String msg, DialogInterface.OnCancelListener listener) {
        if (getActivity() == null) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        progressDialog = new ProgressDialog(getActivity());
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
        if (getActivity() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tip);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.confirm, null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void lazyLoad();

}
