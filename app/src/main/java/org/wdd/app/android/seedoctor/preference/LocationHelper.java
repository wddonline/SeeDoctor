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

    private final String SAVED = "saved";//是否保存过位置信息

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
    private Location location;

    private LocationHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        initLocation();
    }

    public void initLocation() {
        if (!preferences.contains(SAVED)) return;
        location = new Location();
        location.latitude = preferences.getFloat(LATITUDE, 0);
        location.longitude = preferences.getFloat(LONGITUDE, 0);
        location.accuracy = preferences.getFloat(ACCURACY, 0);
        location.address = preferences.getString(ADDRESS, null);
        location.country = preferences.getString(COUNTRY, null);
        location.province = preferences.getString(PROVINCE, null);
        location.city = preferences.getString(CITY, null);
        location.district = preferences.getString(DISTRICT, null);
        location.street = preferences.getString(STREET, null);
        location.streetNum = preferences.getString(STREET_NUM, null);
        location.cityCode = preferences.getString(CITY_CODE, null);
        location.adCode = preferences.getString(AD_CODE, null);
        location.aoiName = preferences.getString(AOI_NAME, null);
    }

    public Location getLocation() {
        return location;
    }

    public void saveLocationData(Location location) {
        this.location = location;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SAVED, true);
        editor.putFloat(LATITUDE, location.latitude);
        editor.putFloat(LONGITUDE, location.longitude);
        editor.putFloat(ACCURACY, location.accuracy);
        editor.putString(ADDRESS, location.address);
        editor.putString(COUNTRY, location.country);
        editor.putString(PROVINCE, location.province);
        editor.putString(CITY, location.city);
        editor.putString(DISTRICT, location.district);
        editor.putString(STREET, location.street);
        editor.putString(STREET_NUM, location.streetNum);
        editor.putString(CITY_CODE, location.cityCode);
        editor.putString(AD_CODE, location.adCode);
        editor.putString(AOI_NAME, location.aoiName);
        editor.commit();
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }

    public double getAccuracy() {
        return location.accuracy;
    }

    public String getAddress() {
        return location.address;
    }

    public String getCountry() {
        return location.country;
    }

    public String getProvince() {
        return location.province;
    }

    public String getCity() {
        return location.city;
    }

    public String getDistrict() {
        return location.district;
    }

    public String getStreet() {
        return location.street;
    }

    public String getStreet_num() {
        return location.streetNum;
    }

    public String getCity_code() {
        return location.cityCode;
    }

    public String getAd_code() {
        return location.adCode;
    }

    public String getAoi_name() {
        return location.aoiName;
    }

    public static class Location {

        public float latitude;//纬度
        public float longitude;//经度
        public float accuracy;//精度信息
        public String address;//地址
        public String country;//国家信息
        public String province;//省信息
        public String city;//城市信息
        public String district;//城区信息
        public String street;//街道信息
        public String streetNum;//街道门牌号信息
        public String cityCode;//城市编码
        public String adCode;//地区编码
        public String aoiName;//定位点的AOI信息;

        public Location() {
        }

        public Location(float latitude, float longitude, float accuracy, String address, String country,
                        String province, String city, String district, String street, String streetNum,
                        String cityCode, String adCode, String aoiName) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracy = accuracy;
            this.address = address;
            this.country = country;
            this.province = province;
            this.city = city;
            this.district = district;
            this.street = street;
            this.streetNum = streetNum;
            this.cityCode = cityCode;
            this.adCode = adCode;
            this.aoiName = aoiName;
        }
    }
}
