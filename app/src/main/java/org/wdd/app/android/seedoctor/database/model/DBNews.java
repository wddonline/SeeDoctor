package org.wdd.app.android.seedoctor.database.model;

/**
 * Created by richard on 3/17/17.
 */

public class DBNews {

    public int id;
    public String news_id;
    public String image;
    public String title;

    public DBNews() {
    }

    public DBNews(String news_id, String image, String title) {
        this.news_id = news_id;
        this.image = image;
        this.title = title;
    }
}
