package org.wdd.app.android.seedoctor.ui.base;

import android.support.v4.app.Fragment;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public abstract class BaseFragment extends Fragment {

    private boolean inited = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) return;
        if (inited) return;
        inited = true;
        lazyLoad();
    }

    protected abstract void lazyLoad();

}
