package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbEmerency;
import org.wdd.app.android.seedoctor.database.table.EmerencyTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class EmerencyDbManager extends DbManager<DbEmerency> {

    public static void createTable(SQLiteDatabase db) {
        String[] args = {EmerencyTable.TABLE_NAME, EmerencyTable.FIELD_ID, EmerencyTable.FIELD_EMEID,
                EmerencyTable.FIELD_EME};
        db.execSQL("CREATE TABLE IF NOT EXISTS ?(? INTEGER PRIMARYKEY AUTOINCREMENT, ? VARCHAR2(15) " +
                "NOT NULL UNIQUE, ? VARCHAR2(10) NOT NULL);", args);
    }

    public EmerencyDbManager(Context context) {
        super(context);
    }

    @Override
    protected long insert(DbEmerency data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmerencyTable.FIELD_ID, data.id);
            values.put(EmerencyTable.FIELD_EMEID, data.emeid);
            values.put(EmerencyTable.FIELD_EME, data.eme);
            result = db.insert(EmerencyTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    protected List<DbEmerency> queryAll() {
        List<DbEmerency> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {EmerencyTable.FIELD_ID, EmerencyTable.FIELD_EMEID, EmerencyTable.FIELD_EME};
            Cursor cursor = db.query(EmerencyTable.TABLE_NAME, columns, null, null, null, null, null);
            result = new ArrayList<>();
            DbEmerency doctor;
            if (cursor.moveToNext()) {
                doctor = new DbEmerency();
                doctor.id = cursor.getInt(cursor.getColumnIndex(EmerencyTable.FIELD_ID));
                doctor.emeid = cursor.getString(cursor.getColumnIndex(EmerencyTable.FIELD_EMEID));
                doctor.eme = cursor.getString(cursor.getColumnIndex(EmerencyTable.FIELD_EME));
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
    protected DbEmerency queryById(int id) {
        DbEmerency result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {EmerencyTable.FIELD_ID, EmerencyTable.FIELD_EMEID, EmerencyTable.FIELD_EME};
            String selection = EmerencyTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(EmerencyTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbEmerency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmerencyTable.FIELD_ID));
                result = new DbEmerency();
                result.id = cursor.getInt(cursor.getColumnIndex(EmerencyTable.FIELD_ID));
                result.emeid = cursor.getString(cursor.getColumnIndex(EmerencyTable.FIELD_EMEID));
                result.eme = cursor.getString(cursor.getColumnIndex(EmerencyTable.FIELD_EME));
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
            affectedRows = db.delete(EmerencyTable.TABLE_NAME, null, null);
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
            String whereClause = EmerencyTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(EmerencyTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }
}
