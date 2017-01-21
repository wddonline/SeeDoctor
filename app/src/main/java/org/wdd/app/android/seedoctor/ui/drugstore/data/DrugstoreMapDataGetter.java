package org.wdd.app.android.seedoctor.ui.drugstore.data;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.utils.AppToaster;

import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class DrugstoreMapDataGetter implements PoiSearch.OnPoiSearchListener {

    private Context context;
    private ActivityFragmentAvaliable host;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private SearchCallback callback;

    public DrugstoreMapDataGetter(ActivityFragmentAvaliable host, Context context) {
        this.host = host;
        this.context = context;
        query = new PoiSearch.Query("药店", "", LocationHelper.getInstance(context).getCity_code());
        query.setCityLimit(true);
        query.setPageSize(Integer.MAX_VALUE);

        poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(this);
    }

    public void getNearbyHospitalList(LatLonPoint centerPoint, int radius) {
        poiSearch.setBound(new PoiSearch.SearchBound(centerPoint, radius));
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (!host.isAvaliable()) return;
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        if (callback != null) callback.onSearchOk(poiItems);
                    } else {
                        AppToaster.show(R.string.no_search_result);
                        if (callback != null) callback.onSearchFailure();
                    }
                }
            } else {
                AppToaster.show(R.string.no_search_result);
                if (callback != null) callback.onSearchFailure();
            }
        } else {
            AppToaster.show(String.format(context.getString(R.string.search_data_error), rCode));
            if (callback != null) callback.onSearchFailure();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void setSearchCallback(SearchCallback callback) {
        this.callback = callback;
    }

    public interface SearchCallback {

        void onSearchOk(List<PoiItem> data);
        void onSearchFailure();

    }
}
