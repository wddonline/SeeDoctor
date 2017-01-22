package org.wdd.app.android.seedoctor.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public abstract class DbManager<T> {

    private DatabaseConnecter connecter;
    private Context context;

    public DbManager(Context context) {
        this.context = context;
        connecter = DatabaseConnecter.getInstance(context);
    }

    protected SQLiteDatabase getReadableDatabase() {
        return connecter.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        return connecter.getWritableDatabase();
    }

    protected void closeDatabase() {
        connecter.closeDatabase();
    }

    protected abstract long insert(T data);

    protected abstract List<T> queryAll();

    protected abstract T queryById(int id);

    protected abstract int deleteAll();

    protected abstract int deleteById(int id);

}
