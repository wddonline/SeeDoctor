package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbDrug {

    public int id;
    public String drugid;
    public String drugname;

    public DbDrug() {
    }

    public DbDrug(String drugid, String drugname) {
        this.drugid = drugid;
        this.drugname = drugname;
    }
}
