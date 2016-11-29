package org.wdd.app.android.seedoctor.ui.hospital.data;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.utils.AppToaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 11/28/16.
 */

public class HospitalListDataGetter implements PoiSearch.OnPoiSearchListener {

    private final int PAGEZISE = 20;
    private final int RADIUS = 30000;

    private int pageNum = 0;
    private boolean isRefreshing;

    private Context context;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private SearchCallback callback;

    public HospitalListDataGetter(Context context) {
        this.context = context;
        query = new PoiSearch.Query("医院", "", LocationHelper.getInstance(context).getCity_code());
        query.setPageSize(PAGEZISE);

        poiSearch = new PoiSearch(context, query);
        LocationHelper.Location location = LocationHelper.getInstance(context).getLocation();
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(location.latitude, location.longitude), RADIUS));
        poiSearch.setOnPoiSearchListener(this);
    }

    public void getNearbyHospitalList(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        if (isRefreshing) pageNum = 0;
        query.setPageNum(pageNum);
        poiSearch.searchPOIAsyn();
        pageNum++;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        List<Hospital> data = new ArrayList<>();
                        Hospital hospital;
                        for (PoiItem item : poiItems) {
                            hospital = new Hospital();
                            hospital.setName(item.getTitle());
                            hospital.setAddress(item.getSnippet());
                            LatLong latLong = new LatLong();
                            latLong.latitude = (float) item.getLatLonPoint().getLatitude();
                            latLong.longitude = (float) item.getLatLonPoint().getLongitude();
                            hospital.setLatLong(latLong);
                            hospital.setTelephone(item.getTel());
                            hospital.setWebsite(item.getWebsite());
                            hospital.setPostcode(item.getPostcode());
                            hospital.setEmail(item.getEmail());
                            hospital.setDistance(item.getDistance());
                            List<Photo> photos = item.getPhotos();
                            if (photos != null && photos.size() > 0) {
                                String[] imgUrls = new String[photos.size()];
                                Photo p;
                                for (int i = 0; i < photos.size(); i++) {
                                    p = photos.get(i);
                                    imgUrls[i] = p.getUrl();
                                }
                                hospital.setImgUrls(imgUrls);
                            }
                            data.add(hospital);
                        }
                        if (callback != null) callback.onSearchOk(isRefreshing, data);
                    } else {
                        AppToaster.show(R.string.no_search_result);
                        if (callback != null) callback.onSearchFailure(isRefreshing);
                    }
                }
            } else {
                AppToaster.show(R.string.no_search_result);
                if (callback != null) callback.onSearchFailure(isRefreshing);
            }
        } else {
            AppToaster.show(String.format(context.getString(R.string.search_data_error), rCode));
            if (callback != null) callback.onSearchFailure(isRefreshing);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void setSearchCallback(SearchCallback callback) {
        this.callback = callback;
    }

    public interface SearchCallback {

        void onSearchOk(boolean isRefreshing, List<Hospital> data);
        void onSearchFailure(boolean isRefreshing);

    }
}
