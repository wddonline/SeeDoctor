package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

/**
 * Created by richard on 12/21/16.
 */

public class Drug {

    public String drugid;
    public String drugname;
    public int catgory;
    public String indication;
    public String companyname;
    public String picurl;

    public Drug() {
    }

    public String getDrugid() {
        return drugid;
    }

    public void setDrugid(String drugid) {
        this.drugid = drugid;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public int getCatgory() {
        return catgory;
    }

    public void setCatgory(int catgory) {
        this.catgory = catgory;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugid=" + drugid +
                ", drugname='" + drugname + '\'' +
                ", catgory=" + catgory +
                ", indication='" + indication + '\'' +
                ", companyname='" + companyname + '\'' +
                ", picurl='" + picurl + '\'' +
                '}';
    }
}
