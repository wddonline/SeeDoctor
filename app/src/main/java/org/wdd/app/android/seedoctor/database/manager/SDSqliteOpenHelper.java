package org.wdd.app.android.seedoctor.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wdd.app.android.seedoctor.database.manager.impl.DepartmentDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DiseaseDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DoctorDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DrugDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.EmerencyDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.HospitalDbManager;

/**
 * Created by richard on 1/22/17.
 */

class SDSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "see_doctor.db";
    private static final int DB_VERSION = 1;

    public SDSqliteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();

            DepartmentDbManager.createTable(db);
            DiseaseDbManager.createTable(db);
            DoctorDbManager.createTable(db);
            DrugDbManager.createTable(db);
            EmerencyDbManager.createTable(db);
            HospitalDbManager.createTable(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
