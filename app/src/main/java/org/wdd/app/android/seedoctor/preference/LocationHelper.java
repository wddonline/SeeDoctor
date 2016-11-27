package org.wdd.app.android.seedoctor.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wangdd on 16-11-27.
 */

public class LocationHelper {

    private static LocationHelper INSTANCE;

    public static LocationHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocationHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocationHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    private final String LATITUDE = "Latitude";//获取纬度
    private final String LONGITUDE = "Longitude";//获取经度
    private final String ACCURACY = "Accuracy";//获取精度信息
    private final String ADDRESS = "Address";//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    private final String COUNTRY = "Country";//国家信息
    private final String PROVINCE = "Province";//省信息
    private final String CITY = "City";//城市信息
    private final String DISTRICT = "District";//城区信息
    private final String STREET = "Street";//街道信息
    private final String STREET_NUM = "StreetNum";//街道门牌号信息
    private final String CITY_CODE = "CityCode";//城市编码
    private final String AD_CODE = "AdCode";//地区编码
    private final String AOI_NAME = "AoiName";//获取当前定位点的AOI信息;

    private Context context;
    private SharedPreferences preferences;

    private LocationHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
    }

    public void setLatitude() {
    }

    public double getLatitude() {
        return preferences.getFloat(LATITUDE, 0);
    }

    public void setLongitude() {
        preferences.getFloat(LONGITUDE, 0);
    }

    public double getLongitude() {
        return preferences.getFloat(LONGITUDE, 0);
    }

    public void setAccuracy() {
        preferences.getFloat(ACCURACY, 0);
    }

    public double getAccuracy() {
        return preferences.getFloat(ACCURACY, 0);
    }

    public void setAddress() {
    }

    public String getAddress() {
        return preferences.getString(ADDRESS, null);
    }

    public void setCountry() {
        preferences.getString(COUNTRY, null);
    }

    public String getCountry() {
        return preferences.getString(COUNTRY, null);
    }

    public void setProvince() {
        preferences.getString(PROVINCE, null);
    }

    public String getProvince() {
        return preferences.getString(PROVINCE, null);
    }

    public void setCity() {
        preferences.getString(CITY, null);
    }

    public String getCity() {
        return preferences.getString(CITY, null);
    }

    public void setDistrict() {
        preferences.getString(DISTRICT, null);
    }

    public String getDistrict() {
        return preferences.getString(DISTRICT, null);
    }

    public void setStreet() {
        preferences.getString(STREET, null);
    }

    public String getStreet() {
        return preferences.getString(STREET, null);
    }

    public void setStreet_num() {
        preferences.getString(STREET_NUM, null);
    }

    public String getStreet_num() {
        return preferences.getString(STREET_NUM, null);
    }

    public void setCity_code() {
        preferences.getString(CITY_CODE, null);
    }

    public String getCity_code() {
        return preferences.getString(CITY_CODE, null);
    }

    public void setAd_code() {
        preferences.getString(AD_CODE, null);
    }

    public String getAd_code() {
        return preferences.getString(AD_CODE, null);
    }

    public void setAoi_name() {
        preferences.getString(AOI_NAME, null);
    }

    public String getAoi_name() {
        return preferences.getString(AOI_NAME, null);
    }
}
