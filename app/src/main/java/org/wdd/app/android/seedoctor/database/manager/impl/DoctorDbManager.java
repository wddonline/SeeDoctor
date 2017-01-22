package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDoctor;
import org.wdd.app.android.seedoctor.database.table.DoctorTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DoctorDbManager extends DbManager<DbDoctor> {

    public static void createTable(SQLiteDatabase db) {
        String[] args = {DoctorTable.TABLE_NAME, DoctorTable.FIELD_ID, DoctorTable.FIELD_DOCTOR_ID,
                DoctorTable.FIELD_DOCTOR_NAME, DoctorTable.FIELD_PHOTO_URL};
        db.execSQL("CREATE TABLE IF NOT EXISTS ?(? INTEGER PRIMARYKEY AUTOINCREMENT, ? VARCHAR2(15) " +
                "NOT NULL UNIQUE, ? VARCHAR2(10) NOT NULL, ? VARCHAR2(100));", args);
    }

    public DoctorDbManager(Context context) {
        super(context);
    }

    @Override
    protected long insert(DbDoctor data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DoctorTable.FIELD_ID, data.id);
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
    protected List<DbDoctor> queryAll() {
        List<DbDoctor> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DoctorTable.FIELD_ID, DoctorTable.FIELD_ID, DoctorTable.FIELD_DOCTOR_ID,
                    DoctorTable.FIELD_DOCTOR_NAME, DoctorTable.FIELD_PHOTO_URL};
            Cursor cursor = db.query(DoctorTable.TABLE_NAME, columns, null, null, null, null, null);
            result = new ArrayList<>();
            DbDoctor doctor;
            if (cursor.moveToNext()) {
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
    protected DbDoctor queryById(int id) {
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

    @Override
    protected int deleteAll() {
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
    protected int deleteById(int id) {
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

}
