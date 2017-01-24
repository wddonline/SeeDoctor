package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbDoctor {

    public int id;
    public String doctorid;
    public String doctorname;
    public String photourl;

    public DbDoctor() {
    }

    public DbDoctor(String doctorid, String doctorname, String photourl) {
        this.doctorid = doctorid;
        this.doctorname = doctorname;
        this.photourl = photourl;
    }
}
