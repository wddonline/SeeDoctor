package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

/**
 * Created by richard on 12/19/16.
 */

public class Disease {

    public String diseasename;
    public String introduction;
    public String diseasepicurl;
    public String diseasepic;
    public String diseaseid;

    public Disease() {
    }

    public Disease(String diseasename, String introduction, String diseasepicurl, String diseasepic, String diseaseid) {
        this.diseasename = diseasename;
        this.introduction = introduction;
        this.diseasepicurl = diseasepicurl;
        this.diseasepic = diseasepic;
        this.diseaseid = diseaseid;
    }

    public String getDiseasename() {
        return diseasename;
    }

    public void setDiseasename(String diseasename) {
        this.diseasename = diseasename;
    }

    public String getDiseaseid() {
        return diseaseid;
    }

    public void setDiseaseid(String diseaseid) {
        this.diseaseid = diseaseid;
    }

    public String getDiseasepic() {
        return diseasepic;
    }

    public void setDiseasepic(String diseasepic) {
        this.diseasepic = diseasepic;
    }

    public String getDiseasepicurl() {
        return diseasepicurl;
    }

    public void setDiseasepicurl(String diseasepicurl) {
        this.diseasepicurl = diseasepicurl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "diseasename='" + diseasename + '\'' +
                ", introduction='" + introduction + '\'' +
                ", diseasepicurl='" + diseasepicurl + '\'' +
                ", diseasepic='" + diseasepic + '\'' +
                ", diseaseid=" + diseaseid +
                '}';
    }
}
