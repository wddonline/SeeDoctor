package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbEmergency;
import org.wdd.app.android.seedoctor.database.table.EmergencyTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class EmergencyDbManager extends DbManager<DbEmergency> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + EmergencyTable.TABLE_NAME + "(" + EmergencyTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + EmergencyTable.FIELD_EMEID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + EmergencyTable.FIELD_EME + " VARCHAR2(10) NOT NULL);");
    }

    public EmergencyDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbEmergency data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmergencyTable.FIELD_EMEID, data.emeid);
            values.put(EmergencyTable.FIELD_EME, data.eme);
            result = db.insert(EmergencyTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DbEmergency> queryAll() {
        List<DbEmergency> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {EmergencyTable.FIELD_ID, EmergencyTable.FIELD_EMEID, EmergencyTable.FIELD_EME};
            String orderBy = EmergencyTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(EmergencyTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbEmergency doctor;
            while (cursor.moveToNext()) {
                doctor = new DbEmergency();
                doctor.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.FIELD_ID));
                doctor.emeid = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EMEID));
                doctor.eme = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EME));
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
    public DbEmergency queryById(int id) {
        DbEmergency result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {EmergencyTable.FIELD_ID, EmergencyTable.FIELD_EMEID, EmergencyTable.FIELD_EME};
            String selection = EmergencyTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(EmergencyTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbEmergency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.FIELD_ID));
                result = new DbEmergency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.FIELD_ID));
                result.emeid = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EMEID));
                result.eme = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DbEmergency getEmergencyByEmeid(String emeid) {
        DbEmergency result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {EmergencyTable.FIELD_ID, EmergencyTable.FIELD_EMEID, EmergencyTable.FIELD_EME};
            String selection = EmergencyTable.FIELD_EMEID + "=?";
            String[] selectionArgs = {emeid};
            Cursor cursor = db.query(EmergencyTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbEmergency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.FIELD_ID));
                result = new DbEmergency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.FIELD_ID));
                result.emeid = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EMEID));
                result.eme = cursor.getString(cursor.getColumnIndex(EmergencyTable.FIELD_EME));
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
            affectedRows = db.delete(EmergencyTable.TABLE_NAME, null, null);
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
            String whereClause = EmergencyTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(EmergencyTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByEmeid(String emeid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = EmergencyTable.FIELD_EMEID + "=?";
            String[] whereArgs = {emeid};
            affectedRows = db.delete(EmergencyTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteEmergencys(List<DbEmergency> emergencys) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = EmergencyTable.FIELD_ID + " = ?";
            for (DbEmergency d : emergencys) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(EmergencyTable.TABLE_NAME, whereClause, whereArgs);
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
