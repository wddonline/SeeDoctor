package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

import org.wdd.app.android.seedoctor.views.index_bar.bean.BaseIndexPinyinBean;

/**
 * Created by richard on 1/3/17.
 */

public class Department extends BaseIndexPinyinBean {

    public String hospitalid;
    public String hospitalname;
    public String departmentid;
    public String departmentname;
    public String introduction;
    public String hospdeptid;

    public String getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHospdeptid() {
        return hospdeptid;
    }

    public void setHospdeptid(String hospdeptid) {
        this.hospdeptid = hospdeptid;
    }

    @Override
    public String getTarget() {
        return departmentname;
    }
}
