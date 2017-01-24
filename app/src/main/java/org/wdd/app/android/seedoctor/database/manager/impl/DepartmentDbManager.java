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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DepartmentTable.TABLE_NAME + "(" + DepartmentTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + DepartmentTable.FIELD_DEPARTMENT_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + DepartmentTable.FIELD_DEPARTMENT_NAME + " VARCHAR2(10) NOT NULL);");
    }

    public DepartmentDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DbDepartment data) {
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
    public List<DbDepartment> queryAll() {
        List<DbDepartment> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DepartmentTable.FIELD_ID, DepartmentTable.FIELD_DEPARTMENT_ID, DepartmentTable.FIELD_DEPARTMENT_NAME};
            String orderBy = DepartmentTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(DepartmentTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DbDepartment department;
            while (cursor.moveToNext()) {
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
    public DbDepartment queryById(int id) {
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

    public DbDepartment getDepartmentByDepartmentid(String departmentid) {
        DbDepartment result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {DepartmentTable.FIELD_ID, DepartmentTable.FIELD_DEPARTMENT_ID, DepartmentTable.FIELD_DEPARTMENT_NAME};
            String selection = DepartmentTable.FIELD_DEPARTMENT_ID + "=?";
            String[] selectionArgs = {departmentid};
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
    public int deleteAll() {
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
    public int deleteById(int id) {
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

    public long deleteByDepartmentid(String departmentid) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = DepartmentTable.FIELD_DEPARTMENT_ID + "=?";
            String[] whereArgs = {departmentid};
            affectedRows = db.delete(DepartmentTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteDepartments(List<DbDepartment> departments) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = DepartmentTable.FIELD_ID + " = ?";
            for (DbDepartment d : departments) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(DepartmentTable.TABLE_NAME, whereClause, whereArgs);
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
