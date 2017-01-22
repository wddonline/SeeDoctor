package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DbDepartment;
import org.wdd.app.android.seedoctor.database.table.DepartmentTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class DepartmentDbManager extends DbManager<DbDepartment> {

    public static void createTable(SQLiteDatabase db) {
        String[] args = {DepartmentTable.TABLE_NAME, DepartmentTable.FIELD_ID, DepartmentTable.FIELD_DEPARTMENT_ID,
                DepartmentTable.FIELD_DEPARTMENT_NAME};
        db.execSQL("CREATE TABLE IF NOT EXISTS ?(? INTEGER PRIMARYKEY AUTOINCREMENT, ? VARCHAR2(15) " +
                "NOT NULL UNIQUE, ? VARCHAR2(10) NOT NULL);", args);
    }

    public DepartmentDbManager(Context context) {
        super(context);
    }

    @Override
    protected long insert(DbDepartment data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DepartmentTable.FIELD_DEPARTMENT_ID, data.departmentid);
            values.put(DepartmentTable.FIELD_DEPARTMENT_NAME, data.departmentname);
            result = db.insert(DepartmentTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    protected List<DbDepartment> queryAll() {
        List<DbDepartment> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DepartmentTable.FIELD_ID, DepartmentTable.FIELD_DEPARTMENT_ID, DepartmentTable.FIELD_DEPARTMENT_NAME};
            Cursor cursor = db.query(DepartmentTable.TABLE_NAME, columns, null, null, null, null, null);
            result = new ArrayList<>();
            DbDepartment department;
            if (cursor.moveToNext()) {
                department = new DbDepartment();
                department.id = cursor.getInt(cursor.getColumnIndex(DepartmentTable.FIELD_ID));
                department.departmentid = cursor.getString(cursor.getColumnIndex(DepartmentTable.FIELD_DEPARTMENT_ID));
                department.departmentname = cursor.getString(cursor.getColumnIndex(DepartmentTable.FIELD_DEPARTMENT_NAME));
                result.add(department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    protected DbDepartment queryById(int id) {
        DbDepartment result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DepartmentTable.FIELD_ID, DepartmentTable.FIELD_DEPARTMENT_ID, DepartmentTable.FIELD_DEPARTMENT_NAME};
            String selection = DepartmentTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(DepartmentTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DbDepartment();
                result.id = cursor.getInt(cursor.getColumnIndex(DepartmentTable.FIELD_ID));
                result.departmentid = cursor.getString(cursor.getColumnIndex(DepartmentTable.FIELD_DEPARTMENT_ID));
                result.departmentname = cursor.getString(cursor.getColumnIndex(DepartmentTable.FIELD_DEPARTMENT_NAME));
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
            affectedRows = db.delete(DepartmentTable.TABLE_NAME, null, null);
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
            String whereClause = DepartmentTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(DepartmentTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

}
