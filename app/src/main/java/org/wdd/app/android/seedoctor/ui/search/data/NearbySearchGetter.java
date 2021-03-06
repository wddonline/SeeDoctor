package org.wdd.app.android.seedoctor.ui.search.data;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.wdd.app.android.seedoctor.location.LatLong;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.seedoctor.ui.nearby.model.Mark;
import org.wdd.app.android.seedoctor.utils.LogUtils;
import org.wdd.app.android.seedoctor.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 12/5/16.
 */

public class NearbySearchGetter implements PoiSearch.OnPoiSearchListener {

    private final String tag = "DrugstoreSearchGetter";
    public static final int PAGEZISE = 50;
    private int pageNum = 0;

    private Context context;
    private PoiSearch poiSearch;
    private ActivityFragmentAvaliable host;
    private PoiSearch.Query query;
    private SearchCallback callback;

    public NearbySearchGetter(ActivityFragmentAvaliable host, Context context, SearchCallback callback) {
        this.host = host;
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
        if (!host.isAvaliable()) return;
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        List<Mark> data = new ArrayList<>();
                        Mark mark;
                        for (PoiItem item : poiItems) {
                            mark = new Mark();
                            mark.setName(item.getTitle());
                            mark.setAddress(item.getSnippet());
                            LatLong latLong = new LatLong();
                            latLong.latitude = (float) item.getLatLonPoint().getLatitude();
                            latLong.longitude = (float) item.getLatLonPoint().getLongitude();
                            mark.setLatLong(latLong);
                            mark.setTelephone(item.getTel());
                            mark.setWebsite(item.getWebsite());
                            mark.setPostcode(item.getPostcode());
                            mark.setEmail(item.getEmail());
                            mark.setDistance(item.getDistance());
                            mark.setTypeDes(item.getTypeDes());
                            List<Photo> photos = item.getPhotos();
                            if (photos != null && photos.size() > 0) {
                                String[] imgUrls = new String[photos.size()];
                                Photo p;
                                for (int i = 0; i < photos.size(); i++) {
                                    p = photos.get(i);
                                    imgUrls[i] = p.getUrl();
                                }
                                mark.setImgUrls(imgUrls);
                            }
                            data.add(mark);
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

        void onSearchOk(List<Mark> data);
        void onNetworkError();
        void onSearchFailure();
        void onSearchNoData();

    }
}
