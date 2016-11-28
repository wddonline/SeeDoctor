package org.wdd.app.android.seedoctor.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.wdd.app.android.seedoctor.R;
import org.wdd.app.android.seedoctor.preference.LocationHelper;
import org.wdd.app.android.seedoctor.utils.AppToaster;

/**
 * Created by wangdd on 16-11-27.
 */

public class LocationFinder {

    private final String tag = "LocationFinder";
    private final int MAX_TRY_NUM = 5;

    private Context context;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private LocationListener locationListener;

    private boolean locating = false;
    private int count = 0;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {

                    LocationHelper.Location location = new LocationHelper.Location();
//                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    location.latitude = (float) amapLocation.getLatitude();//获取纬度
                    location.longitude = (float) amapLocation.getLongitude();//获取经度
                    location.accuracy = (float) amapLocation.getAccuracy();//获取精度信息
                    location.address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    location.country = amapLocation.getCountry();//国家信息
                    location.province = amapLocation.getProvince();//省信息
                    location.city = amapLocation.getCity();//城市信息
                    location.district = amapLocation.getDistrict();//城区信息
                    location.street = amapLocation.getStreet();//街道信息
                    location.streetNum = amapLocation.getStreetNum();//街道门牌号信息
                    location.cityCode = amapLocation.getCityCode();//城市编码
                    location.adCode = amapLocation.getAdCode();//地区编码
                    location.aoiName = amapLocation.getAoiName();//获取当前定位点的AOI信息
                    LocationHelper.getInstance(context).saveLocationData(location);

                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                    mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                    if (locationListener != null) locationListener.onLocationGeted(amapLocation);
                }else {
                    if (count > MAX_TRY_NUM) {
                        mLocationClient.stopLocation();
                        mLocationClient.onDestroy();
                        count = 0;
                        return;
                    }
                    count++;
                    String err = amapLocation.getErrorInfo() + "[" + amapLocation.getErrorCode() + "]";
                    Log.e(tag, err);
                    AppToaster.show(R.string.find_loc_failure);
                    if (locationListener != null) locationListener.onLocationError(err);
                }
            }
        }
    };

    public LocationFinder(Context context) {
        this.context = context;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);

        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(5000);
//        mLocationOption.setOnceLocation(false);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        mLocationClient.setLocationOption(mLocationOption);
    }

    public void start(boolean once) {
        locating = true;
        mLocationOption.setOnceLocation(once);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationClient.startLocation();
    }

    public void start() {
        start(false);
    }

    public void stop() {
        locating = false;
        mLocationClient.stopLocation();
        mLocationClient.unRegisterLocationListener(mLocationListener);
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public boolean isLocating() {
        return locating;
    }

    public interface LocationListener {

        void onLocationGeted(AMapLocation location);
        void onLocationError(String error);

    }
}
