package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDrug;
import org.wdd.app.android.seedoctor.database.table.DrugTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DrugDbManager extends DbManager<DbDrug> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DrugTable.TABLE_NAME + "(" + DrugTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + DrugTable.FIELD_DRUG_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + DrugTable.FIELD_DRUG_NAME + " VARCHAR2(10) NOT NULL);");
    }

    public DrugDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbDrug data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DrugTable.FIELD_DRUG_ID, data.drugid);
            values.put(DrugTable.FIELD_DRUG_NAME, data.drugname);
            result = db.insert(DrugTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DbDrug> queryAll() {
        List<DbDrug> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DrugTable.FIELD_ID, DrugTable.FIELD_DRUG_ID,
                    DrugTable.FIELD_DRUG_NAME};
            String orderBy = DrugTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(DrugTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbDrug doctor;
            while (cursor.moveToNext()) {
                doctor = new DbDrug();
                doctor.id = cursor.getInt(cursor.getColumnIndex(DrugTable.FIELD_ID));
                doctor.drugid = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_ID));
                doctor.drugname = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_NAME));
                result.add(doctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public DbDrug queryById(int id) {
        DbDrug result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DrugTable.FIELD_ID, DrugTable.FIELD_DRUG_ID,
                    DrugTable.FIELD_DRUG_NAME};
            String selection = DrugTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(DrugTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDrug();
                result.id = cursor.getInt(cursor.getColumnIndex(DrugTable.FIELD_ID));
                result = new DbDrug();
                result.id = cursor.getInt(cursor.getColumnIndex(DrugTable.FIELD_ID));
                result.drugid = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_ID));
                result.drugname = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DbDrug getDrugByDrugid(String drugid) {
        DbDrug result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DrugTable.FIELD_ID, DrugTable.FIELD_DRUG_ID,
                    DrugTable.FIELD_DRUG_NAME};
            String selection = DrugTable.FIELD_DRUG_ID + "=?";
            String[] selectionArgs = {drugid + ""};
            Cursor cursor = db.query(DrugTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDrug();
                result.id = cursor.getInt(cursor.getColumnIndex(DrugTable.FIELD_ID));
                result = new DbDrug();
                result.id = cursor.getInt(cursor.getColumnIndex(DrugTable.FIELD_ID));
                result.drugid = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_ID));
                result.drugname = cursor.getString(cursor.getColumnIndex(DrugTable.FIELD_DRUG_NAME));
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
            affectedRows = db.delete(DrugTable.TABLE_NAME, null, null);
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
            String whereClause = DrugTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(DrugTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByDrugid(String drugid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = DrugTable.FIELD_DRUG_ID + "=?";
            String[] whereArgs = {drugid};
            affectedRows = db.delete(DrugTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteDrugs(List<DbDrug> drugs) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = DrugTable.FIELD_ID + " = ?";
            for (DbDrug d : drugs) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(DrugTable.TABLE_NAME, whereClause, whereArgs);
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
