package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbDisease {

    public int id;
    public String diseasename;
    public String diseaseid;

    public DbDisease() {
    }

    public DbDisease(String diseaseid, String diseasename) {
        this.diseaseid = diseaseid;
        this.diseasename = diseasename;
    }
}
