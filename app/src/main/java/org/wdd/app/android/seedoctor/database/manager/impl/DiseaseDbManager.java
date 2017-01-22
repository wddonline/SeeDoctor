package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDisease;
import org.wdd.app.android.seedoctor.database.table.DieaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DiseaseDbManager extends DbManager<DbDisease> {

    public static void createTable(SQLiteDatabase db) {
        String[] args = {DieaseTable.TABLE_NAME, DieaseTable.FIELD_ID, DieaseTable.FIELD_DISEASE_ID,
                DieaseTable.FIELD_DISEASE_NAME, DieaseTable.FIELD_DISEASE_PIC_URL};
        db.execSQL("CREATE TABLE IF NOT EXISTS ?(? INTEGER PRIMARYKEY AUTOINCREMENT, ? VARCHAR2(15) " +
                "NOT NULL UNIQUE, ? VARCHAR2(10) NOT NULL, ? VARCHAR2(100));", args);
    }

    public DiseaseDbManager(Context context) {
        super(context);
    }

    @Override
    protected long insert(DbDisease data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DieaseTable.FIELD_ID, data.id);
            values.put(DieaseTable.FIELD_DISEASE_ID, data.diseaseid);
            values.put(DieaseTable.FIELD_DISEASE_NAME, data.diseasename);
            values.put(DieaseTable.FIELD_DISEASE_PIC_URL, data.diseasepicurl);
            result = db.insert(DieaseTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    protected List<DbDisease> queryAll() {
        List<DbDisease> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DieaseTable.FIELD_ID, DieaseTable.FIELD_ID, DieaseTable.FIELD_DISEASE_ID,
                    DieaseTable.FIELD_DISEASE_NAME, DieaseTable.FIELD_DISEASE_PIC_URL};
            Cursor cursor = db.query(DieaseTable.TABLE_NAME, columns, null, null, null, null, null);
            result = new ArrayList<>();
            DbDisease disease;
            if (cursor.moveToNext()) {
                disease = new DbDisease();
                disease.id = cursor.getInt(cursor.getColumnIndex(DieaseTable.FIELD_ID));
                disease.diseaseid = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_ID));
                disease.diseasename = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_NAME));
                disease.diseasepicurl = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_PIC_URL));
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
    protected DbDisease queryById(int id) {
        DbDisease result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DieaseTable.FIELD_ID, DieaseTable.FIELD_ID, DieaseTable.FIELD_DISEASE_ID,
                    DieaseTable.FIELD_DISEASE_NAME, DieaseTable.FIELD_DISEASE_PIC_URL};
            String selection = DieaseTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(DieaseTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDisease();
                result.id = cursor.getInt(cursor.getColumnIndex(DieaseTable.FIELD_ID));
                result.diseaseid = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_ID));
                result.diseasename = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_NAME));
                result.diseasepicurl = cursor.getString(cursor.getColumnIndex(DieaseTable.FIELD_DISEASE_PIC_URL));
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
            affectedRows = db.delete(DieaseTable.TABLE_NAME, null, null);
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
            String whereClause = DieaseTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(DieaseTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

}
