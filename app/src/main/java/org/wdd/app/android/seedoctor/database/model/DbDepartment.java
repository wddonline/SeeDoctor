package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 1/22/17.
 */

public class DbDepartment {

    public int id;
    public String departmentid;
    public String departmentname;

    public DbDepartment() {
    }

    public DbDepartment(String departmentid, String departmentname) {
        this.departmentid = departmentid;
        this.departmentname = departmentname;
    }
}
