package org.wdd.app.android.seedoctor.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wdd.app.android.seedoctor.database.manager.impl.DepartmentDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DiseaseDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DoctorDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.DrugDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.EmergencyDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.HospitalDbManager;
import org.wdd.app.android.seedoctor.database.manager.impl.NewsDbManager;

/**
 * Created by richard on 1/22/17.
 */

class SDSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "see_doctor.db";
    private static final int DB_VERSION = 2;

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
            EmergencyDbManager.createTable(db);
            HospitalDbManager.createTable(db);
            NewsDbManager.createTable(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion <= oldVersion) return;
        NewsDbManager.createTable(db);
    }
}
