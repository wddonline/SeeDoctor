package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.database.table.HospitalTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class HospitalDbManager extends DbManager<DbHospital> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + HospitalTable.TABLE_NAME + "(" + HospitalTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + HospitalTable.FIELD_HOSPITAL_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + HospitalTable.FIELD_HOSPITAL_NAME + " VARCHAR2(10) NOT NULL, " +
                HospitalTable.FIELD_PICURL + " VARCHAR2(100));");
    }

    public HospitalDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbHospital data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(HospitalTable.FIELD_HOSPITAL_ID, data.hospitalid);
            values.put(HospitalTable.FIELD_HOSPITAL_NAME, data.hospitalname);
            values.put(HospitalTable.FIELD_PICURL, data.picurl);
            result = db.insert(HospitalTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DbHospital> queryAll() {
        List<DbHospital> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {HospitalTable.FIELD_ID, HospitalTable.FIELD_HOSPITAL_ID, HospitalTable.FIELD_HOSPITAL_NAME,
                    HospitalTable.FIELD_PICURL};
            String orderBy = HospitalTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(HospitalTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbHospital doctor;
            while (cursor.moveToNext()) {
                doctor = new DbHospital();
                doctor.id = cursor.getInt(cursor.getColumnIndex(HospitalTable.FIELD_ID));
                doctor.hospitalid = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_ID));
                doctor.hospitalname = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_NAME));
                doctor.picurl = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_PICURL));
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
    public DbHospital queryById(int id) {
        DbHospital result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {HospitalTable.FIELD_ID, HospitalTable.FIELD_HOSPITAL_ID, HospitalTable.FIELD_HOSPITAL_NAME,
                    HospitalTable.FIELD_PICURL};
            String selection = HospitalTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(HospitalTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbHospital();
                result.id = cursor.getInt(cursor.getColumnIndex(HospitalTable.FIELD_ID));
                result.hospitalid = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_ID));
                result.hospitalname = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_NAME));
                result.picurl = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_PICURL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DbHospital getHospitaByHospitaid(String hospitalid) {
        DbHospital result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {HospitalTable.FIELD_ID, HospitalTable.FIELD_HOSPITAL_ID, HospitalTable.FIELD_HOSPITAL_NAME,
                    HospitalTable.FIELD_PICURL};
            String selection = HospitalTable.FIELD_HOSPITAL_ID + "=?";
            String[] selectionArgs = {hospitalid};
            Cursor cursor = db.query(HospitalTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbHospital();
                result.id = cursor.getInt(cursor.getColumnIndex(HospitalTable.FIELD_ID));
                result.hospitalid = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_ID));
                result.hospitalname = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_HOSPITAL_NAME));
                result.picurl = cursor.getString(cursor.getColumnIndex(HospitalTable.FIELD_PICURL));
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
            affectedRows = db.delete(HospitalTable.TABLE_NAME, null, null);
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
            String whereClause = HospitalTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(HospitalTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByHospitalid(String hospitalid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = HospitalTable.FIELD_HOSPITAL_ID + "=?";
            String[] whereArgs = {hospitalid};
            affectedRows = db.delete(HospitalTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteHospitals(List<DbHospital> hospitals) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = HospitalTable.FIELD_ID + " = ?";
            for (DbHospital h : hospitals) {
                String[] whereArgs = {h.id + ""};
                affectedRows += db.delete(HospitalTable.TABLE_NAME, whereClause, whereArgs);
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
