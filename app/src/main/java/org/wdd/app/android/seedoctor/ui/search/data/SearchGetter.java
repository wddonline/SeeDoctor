package org.wdd.app.android.seedoctor.ui.search.data;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.hospital.model.Hospital;
import org.wdd.app.android.seedoctor.utils.LogUtils;
import org.wdd.app.android.seedoctor.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class SearchGetter implements PoiSearch.OnPoiSearchListener {

    private final String tag = "DrugstoreSearchGetter";
    public static final int PAGEZISE = 50;
    private int pageNum = 0;

    private Context context;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private SearchCallback callback;

    public SearchGetter(Context context, SearchCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void getHospitalByName(String name, boolean append) {
        if (!NetworkUtils.isNetworkEnabled(context)) {
            if (callback != null) callback.onNetworkError();
            return;
        }
        if (!append) {
            pageNum = 0;
        }
        query = new PoiSearch.Query(name, "医疗保健服务", LocationHelper.getInstance(context).getCity_code());
        query.setPageSize(PAGEZISE);
        query.setPageNum(pageNum);
        pageNum++;

        poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
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
                            hospital.setTypeDes(item.getTypeDes());
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
                        if (callback != null) callback.onSearchOk(data);
                    } else {
                        LogUtils.d(tag, "没有搜索到任何结果");
                        if (callback != null) callback.onSearchNoData();
                    }
                }
            } else {
                LogUtils.d(tag, "没有搜索到任何结果");
                if (callback != null) callback.onSearchNoData();
            }
        } else {
            LogUtils.d(tag, "搜索失败，错误码[" + rCode + "]");
            if (callback != null) callback.onSearchFailure();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public interface SearchCallback {

        void onSearchOk(List<Hospital> data);
        void onNetworkError();
        void onSearchFailure();
        void onSearchNoData();

    }
}
