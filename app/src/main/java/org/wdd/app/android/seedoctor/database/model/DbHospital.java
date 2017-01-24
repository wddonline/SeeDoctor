package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbHospital {

    public int id;
    public String hospitalid;
    public String hospitalname;
    public String picurl;

    public DbHospital() {
    }

    public DbHospital(String hospitalid, String hospitalname, String picurl) {
        this.hospitalid = hospitalid;
        this.hospitalname = hospitalname;
        this.picurl = picurl;
    }
}
