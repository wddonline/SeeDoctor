package org.wdd.app.android.seedoctor.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by richard on 1/12/17.
 */

public class AppConfManager {

    private static AppConfManager INSTANCE;

    public static AppConfManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppConfManager(context);
        }
        return INSTANCE;
    }

    private final String CONF_NAME = "app_conf";

    private final String WIKI_HOSPITAL_PROVINCE_ID = "wiki_hospital_province_id";
    private final String WIKI_HOSPITAL_LEVEL_ID = "wiki_hospital_level_id";

    private Context context;

    private AppConfManager(Context context) {
        this.context = context;
    }

    public void saveWikiHospitalProvinceId(String provinceId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(provinceId)) {
            editor.remove(WIKI_HOSPITAL_PROVINCE_ID);
        } else {
            editor.putString(WIKI_HOSPITAL_PROVINCE_ID, provinceId);
        }
        editor.commit();
    }

    public String getWikiHospitalProvinceId() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_HOSPITAL_PROVINCE_ID, "");
    }

    public void saveWikiHospitalLevelId(String levelId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelId)) {
            editor.remove(WIKI_HOSPITAL_LEVEL_ID);
        } else {
            editor.putString(WIKI_HOSPITAL_LEVEL_ID, levelId);
        }

        editor.commit();
    }

    public String getWikiHospitalLevelId() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_HOSPITAL_LEVEL_ID, "");
    }
}
