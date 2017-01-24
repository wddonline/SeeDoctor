package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDisease;
import org.wdd.app.android.seedoctor.database.table.DiseaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DiseaseDbManager extends DbManager<DbDisease> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DiseaseTable.TABLE_NAME + "(" + DiseaseTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + DiseaseTable.FIELD_DISEASE_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + DiseaseTable.FIELD_DISEASE_NAME +  " VARCHAR2(10) NOT NULL);");
    }

    public DiseaseDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbDisease data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DiseaseTable.FIELD_DISEASE_ID, data.diseaseid);
            values.put(DiseaseTable.FIELD_DISEASE_NAME, data.diseasename);
            result = db.insert(DiseaseTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DbDisease> queryAll() {
        List<DbDisease> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DiseaseTable.FIELD_ID, DiseaseTable.FIELD_ID, DiseaseTable.FIELD_DISEASE_ID,
                    DiseaseTable.FIELD_DISEASE_NAME};
            String orderBy = DiseaseTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(DiseaseTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbDisease disease;
            while (cursor.moveToNext()) {
                disease = new DbDisease();
                disease.id = cursor.getInt(cursor.getColumnIndex(DiseaseTable.FIELD_ID));
                disease.diseaseid = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_ID));
                disease.diseasename = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_NAME));
                result.add(disease);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public DbDisease queryById(int id) {
        DbDisease result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DiseaseTable.FIELD_ID, DiseaseTable.FIELD_ID, DiseaseTable.FIELD_DISEASE_ID,
                    DiseaseTable.FIELD_DISEASE_NAME};
            String selection = DiseaseTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(DiseaseTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDisease();
                result.id = cursor.getInt(cursor.getColumnIndex(DiseaseTable.FIELD_ID));
                result.diseaseid = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_ID));
                result.diseasename = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DbDisease getDiseaseByDiseaseid(String diseaseid) {
        DbDisease result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DiseaseTable.FIELD_ID, DiseaseTable.FIELD_ID, DiseaseTable.FIELD_DISEASE_ID,
                    DiseaseTable.FIELD_DISEASE_NAME};
            String selection = DiseaseTable.FIELD_DISEASE_ID + "=?";
            String[] selectionArgs = {diseaseid};
            Cursor cursor = db.query(DiseaseTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDisease();
                result.id = cursor.getInt(cursor.getColumnIndex(DiseaseTable.FIELD_ID));
                result.diseaseid = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_ID));
                result.diseasename = cursor.getString(cursor.getColumnIndex(DiseaseTable.FIELD_DISEASE_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public int deleteAll() {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            affectedRows = db.delete(DiseaseTable.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    @Override
    public int deleteById(int id) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = DiseaseTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(DiseaseTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByDiseaseid(String diseaseid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = DiseaseTable.FIELD_DISEASE_ID + "=?";
            String[] whereArgs = {diseaseid};
            affectedRows = db.delete(DiseaseTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteDiseases(List<DbDisease> diseases) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = DiseaseTable.FIELD_ID + " = ?";
            for (DbDisease d : diseases) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(DiseaseTable.TABLE_NAME, whereClause, whereArgs);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
        return affectedRows;
    }
}
