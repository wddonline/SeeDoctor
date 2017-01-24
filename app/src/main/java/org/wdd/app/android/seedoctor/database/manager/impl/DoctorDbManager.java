package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.database.model.DbHospital;
import org.wdd.app.android.seedoctor.database.table.DoctorTable;
import org.wdd.app.android.seedoctor.database.table.HospitalTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DoctorDbManager extends DbManager<DbDoctor> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DoctorTable.TABLE_NAME + "(" + DoctorTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + DoctorTable.FIELD_DOCTOR_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + DoctorTable.FIELD_DOCTOR_NAME + " VARCHAR2(10) NOT NULL, " + DoctorTable.FIELD_PHOTO_URL +
                " VARCHAR2(100));");
    }

    public DoctorDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbDoctor data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DoctorTable.FIELD_DOCTOR_ID, data.doctorid);
            values.put(DoctorTable.FIELD_DOCTOR_NAME, data.doctorname);
            values.put(DoctorTable.FIELD_PHOTO_URL, data.photourl);
            result = db.insert(DoctorTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DbDoctor> queryAll() {
        List<DbDoctor> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DoctorTable.FIELD_ID, DoctorTable.FIELD_ID, DoctorTable.FIELD_DOCTOR_ID,
                    DoctorTable.FIELD_DOCTOR_NAME, DoctorTable.FIELD_PHOTO_URL};
            String orderBy = DoctorTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(DoctorTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbDoctor doctor;
            while (cursor.moveToNext()) {
                doctor = new DbDoctor();
                doctor.id = cursor.getInt(cursor.getColumnIndex(DoctorTable.FIELD_ID));
                doctor.doctorid = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_ID));
                doctor.doctorname = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_NAME));
                doctor.photourl = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_PHOTO_URL));
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
    public DbDoctor queryById(int id) {
        DbDoctor result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DoctorTable.FIELD_ID, DoctorTable.FIELD_ID, DoctorTable.FIELD_DOCTOR_ID,
                    DoctorTable.FIELD_DOCTOR_NAME, DoctorTable.FIELD_PHOTO_URL};
            String selection = DoctorTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(DoctorTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDoctor();
                result.id = cursor.getInt(cursor.getColumnIndex(DoctorTable.FIELD_ID));
                result.doctorid = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_ID));
                result.doctorname = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_NAME));
                result.photourl = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_PHOTO_URL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DbDoctor getDoctorByDoctorid(String doctorid) {
        DbDoctor result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DoctorTable.FIELD_ID, DoctorTable.FIELD_ID, DoctorTable.FIELD_DOCTOR_ID,
                    DoctorTable.FIELD_DOCTOR_NAME, DoctorTable.FIELD_PHOTO_URL};
            String selection = DoctorTable.FIELD_DOCTOR_ID + "=?";
            String[] selectionArgs = {doctorid};
            Cursor cursor = db.query(DoctorTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDoctor();
                result.id = cursor.getInt(cursor.getColumnIndex(DoctorTable.FIELD_ID));
                result.doctorid = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_ID));
                result.doctorname = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_DOCTOR_NAME));
                result.photourl = cursor.getString(cursor.getColumnIndex(DoctorTable.FIELD_PHOTO_URL));
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
            affectedRows = db.delete(DoctorTable.TABLE_NAME, null, null);
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
            String whereClause = DoctorTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(DoctorTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByDoctorid(String doctorid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = DoctorTable.FIELD_DOCTOR_ID + "=?";
            String[] whereArgs = {doctorid};
            affectedRows = db.delete(DoctorTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteDoctors(List<DbDoctor> doctors) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = DoctorTable.FIELD_ID + " = ?";
            for (DbDoctor d : doctors) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(DoctorTable.TABLE_NAME, whereClause, whereArgs);
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
