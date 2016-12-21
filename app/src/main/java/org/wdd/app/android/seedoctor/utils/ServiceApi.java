package org.wdd.app.android.seedoctor.utils;

/**
 * Created by richard on 12/19/16.
 */

public class ServiceApi {

    private final static String BASE_URL = "http://api.niruoanhao.com/";
    private final static String BASE_URL_V4 = "http://api4.niruoanhao.com/";

    public final static String WIKI_DISEASE_LIST = BASE_URL + "wiki/diseaseList/";//疾病列表
    public final static String WIKI_DISEASE_DETAIL = BASE_URL_V4 + "disease/detail/";//疾病详情
    public final static String WIKI_DRUG_CATEGORY_LIST = BASE_URL + "wiki/drugCategory/";//药品分类
}
