package org.wdd.app.android.seedoctor.utils;

/**
 * Created by richard on 12/19/16.
 */

public class ServiceApi {

    private final static String BASE_URL = "http://api.niruoanhao.com/";
    private final static String BASE_URL_V4 = "http://api4.niruoanhao.com/";
    private final static String BASE_NEWS_URL = "https://api.chunyuyisheng.com/";

    public final static String WIKI_DISEASE_LIST = BASE_URL + "wiki/diseaseList/";//疾病列表
    public final static String WIKI_DISEASE_DETAIL = BASE_URL_V4 + "disease/detail/";//疾病详情
    public final static String WIKI_DRUG_CATEGORY_LIST = BASE_URL + "wiki/drugCategory/";//药品分类
    public final static String WIKI_DRUG_LIST = BASE_URL + "wiki/drug/";//药品列表
    public final static String WIKI_DRUG_DETAIL = BASE_URL_V4 + "drug/detail/";//药品详情
    public final static String NEW_WIKI_DISEASE_LIST = BASE_URL_V4 + "disease";
    public final static String NEW_WIKI_DRUG_LIST = BASE_URL_V4 + "drug/";//药品列表
    public final static String WIKI_EMERGENCY_LIST = BASE_URL_V4 + "symptom/eme/";//急诊列表
    public final static String EMERGENCY_DETAIL = BASE_URL_V4 + "symptom/emedetail/";//急诊详情
    public final static String WIKI_DEPARTMENT_LIST = BASE_URL_V4 + "department/";//科室列表
    public final static String DEPARTMENT_DETAIL = BASE_URL_V4 + "department/detail/";//科室列表
    public final static String DOCTOR_LIST = BASE_URL + "doctor/";//医生列表
    public final static String HOSPITAL_LIST = BASE_URL + "wiki/hospital/";//医院列表
    public final static String HOSPITAL_DETAIL = BASE_URL + "wiki/hospitalDetailInfo/";//医院详情
    public final static String HOSPITAL_DEPARTMENT = BASE_URL + "wiki/hospitalDepartment/";//医院科室列表
    public final static String DOCTOR_DETAIL = BASE_URL + "doctor/detailInfo";//医生详情
    public final static String PROVINCE_LIST = BASE_URL + "wiki/area";//医院地区列表

    public final static String NEWS_LIST = BASE_NEWS_URL + "community/v2/channel/detail/";//热门资讯
    public final static String NEWS_DETAIL = BASE_NEWS_URL + "webapp/news/%1$s/detail/?w=%2$d&app=0&platform=android";//资讯详情

}
