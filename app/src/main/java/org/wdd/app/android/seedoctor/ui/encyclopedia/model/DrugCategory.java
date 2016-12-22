package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

import java.io.Serializable;

/**
 * Created by richard on 12/21/16.
 */

public class DrugCategory implements Serializable {

    public int catid;
    public String catname;

    public DrugCategory() {
    }

    public DrugCategory(int catid, String catname) {
        this.catid = catid;
        this.catname = catname;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    @Override
    public String toString() {
        return "DrugCategory{" +
                "catid=" + catid +
                ", catname='" + catname + '\'' +
                '}';
    }
}
