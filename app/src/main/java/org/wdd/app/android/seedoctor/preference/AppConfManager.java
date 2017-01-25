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
    private final String WIKI_HOSPITAL_PROVINCE_NAME = "wiki_hospital_province_name";
    private final String WIKI_HOSPITAL_LEVEL_ID = "wiki_hospital_level_id";
    private final String WIKI_HOSPITAL_LEVEL_NAME = "wiki_hospital_level_name";
    private final String WIKI_DOCTOR_PROVINCE_ID = "wiki_doctor_province_id";
    private final String WIKI_DOCTOR_PROVINCE_NAME = "wiki_doctor_province_name";
    private final String WIKI_DOCTOR_LEVEL_ID = "wiki_doctor_level_id";
    private final String WIKI_DOCTOR_LEVEL_NAME = "wiki_doctor_level_name";
    private final String WIKI_DOCTOR_JOB_LEVEL_ID = "wiki_doctor_job_level_id";
    private final String WIKI_DOCTOR_JOB_LEVEL_NAME = "wiki_doctor_job_level_name";
    private final String NAVIGATION_VOICE_SETTING = "navigation_voice_setting";
    private final String NICKNAME = "nickname";
    private final String SEX = "sex";

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

    public void saveWikiHospitalProvinceName(String provinceName) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(provinceName)) {
            editor.remove(WIKI_HOSPITAL_PROVINCE_NAME);
        } else {
            editor.putString(WIKI_HOSPITAL_PROVINCE_NAME, provinceName);
        }
        editor.commit();
    }

    public String getWikiHospitalProvinceName() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_HOSPITAL_PROVINCE_NAME, "");
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

    public void saveWikiHospitalLevelName(String levelName) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelName)) {
            editor.remove(WIKI_HOSPITAL_LEVEL_NAME);
        } else {
            editor.putString(WIKI_HOSPITAL_LEVEL_NAME, levelName);
        }

        editor.commit();
    }

    public String getWikiHospitalLevelName() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_HOSPITAL_LEVEL_NAME, "");
    }

    public void saveWikiDoctorProvinceId(String provinceId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(provinceId)) {
            editor.remove(WIKI_DOCTOR_PROVINCE_ID);
        } else {
            editor.putString(WIKI_DOCTOR_PROVINCE_ID, provinceId);
        }

        editor.commit();
    }

    public String getWikiDoctorProvinceId() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_PROVINCE_ID, "");
    }

    public void saveWikiDoctorProvinceName(String provinceName) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(provinceName)) {
            editor.remove(WIKI_DOCTOR_PROVINCE_NAME);
        } else {
            editor.putString(WIKI_DOCTOR_PROVINCE_NAME, provinceName);
        }

        editor.commit();
    }

    public String getWikiDoctorProvinceName() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_PROVINCE_NAME, "");
    }

    public void saveWikiDoctorLevelId(String levelId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelId)) {
            editor.remove(WIKI_DOCTOR_LEVEL_ID);
        } else {
            editor.putString(WIKI_DOCTOR_LEVEL_ID, levelId);
        }

        editor.commit();
    }

    public String getWikiDoctorLevelId() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_LEVEL_ID, "");
    }

    public void saveWikiDoctorLevelName(String levelId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelId)) {
            editor.remove(WIKI_DOCTOR_LEVEL_NAME);
        } else {
            editor.putString(WIKI_DOCTOR_LEVEL_NAME, levelId);
        }

        editor.commit();
    }

    public String getWikiDoctorLevelName() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_LEVEL_NAME, "");
    }

    public void saveWikiDoctorJobLevelId(String levelId) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelId)) {
            editor.remove(WIKI_DOCTOR_JOB_LEVEL_ID);
        } else {
            editor.putString(WIKI_DOCTOR_JOB_LEVEL_ID, levelId);
        }

        editor.commit();
    }

    public String getWikiDoctorJobLevelId() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_JOB_LEVEL_ID, "");
    }

    public void saveWikiDoctorJobLevelName(String levelName) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(levelName)) {
            editor.remove(WIKI_DOCTOR_JOB_LEVEL_NAME);
        } else {
            editor.putString(WIKI_DOCTOR_JOB_LEVEL_NAME, levelName);
        }

        editor.commit();
    }

    public String getWikiDoctorJobLevelName() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(WIKI_DOCTOR_JOB_LEVEL_NAME, "");
    }

    public void saveNavigationVoiceSetting(String setting) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(setting)) {
            editor.remove(NAVIGATION_VOICE_SETTING);
        } else {
            editor.putString(NAVIGATION_VOICE_SETTING, setting);
        }

        editor.commit();
    }

    public String getNavigationVoiceSetting() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(NAVIGATION_VOICE_SETTING, "");
    }

    public void saveNickname(String nickname) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(nickname)) {
            editor.remove(NICKNAME);
        } else {
            editor.putString(NICKNAME, nickname);
        }

        editor.commit();
    }

    public String getNickname() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(NICKNAME, "");
    }

    public void saveSex(String sex) {
        SharedPreferences preference = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        if (TextUtils.isEmpty(sex)) {
            editor.remove(SEX);
        } else {
            editor.putString(SEX, sex);
        }

        editor.commit();
    }

    public String getSex() {
        SharedPreferences preferences = context.getSharedPreferences(CONF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SEX, "0");
    }
}
