package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

/**
 * Created by richard on 1/12/17.
 */

public class Province {

    public String name;
    public String provinceid;
    public String cityid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }
}
