package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbEmergency {

    public int id;
    public String emeid;
    public String eme;

    public DbEmergency() {
    }

    public DbEmergency(String emeid, String eme) {
        this.emeid = emeid;
        this.eme = eme;
    }
}
