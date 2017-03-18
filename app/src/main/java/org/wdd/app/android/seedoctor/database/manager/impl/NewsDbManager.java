package org.wdd.app.android.seedoctor.database.manager.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.wdd.app.android.seedoctor.database.manager.DbManager;
import org.wdd.app.android.seedoctor.database.model.DBNews;
import org.wdd.app.android.seedoctor.database.table.NewsTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 1/22/17.
 */

public class NewsDbManager extends DbManager<DBNews> {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NewsTable.TABLE_NAME + "(" + NewsTable.FIELD_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + NewsTable.FIELD_NEWS_ID + " VARCHAR2(15) " +
                "NOT NULL UNIQUE, " + NewsTable.FIELD_IMAGE + " VARCHAR2(100) NOT NULL, " + NewsTable.FIELD_TITLE +
                " VARCHAR2(100));");
    }

    public NewsDbManager(Context context) {
        super(context);
    }

    @Override
    public long insert(DBNews data) {
        long result = -1;
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(NewsTable.FIELD_NEWS_ID, data.news_id);
            values.put(NewsTable.FIELD_IMAGE, data.image);
            values.put(NewsTable.FIELD_TITLE, data.title);
            result = db.insert(NewsTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public List<DBNews> queryAll() {
        List<DBNews> result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {NewsTable.FIELD_ID, NewsTable.FIELD_NEWS_ID, NewsTable.FIELD_IMAGE, NewsTable.FIELD_TITLE};
            String orderBy = NewsTable.FIELD_ID + " DESC";
            Cursor cursor = db.query(NewsTable.TABLE_NAME, columns, null, null, null, null, orderBy);
            result = new ArrayList<>();
            DBNews news;
            while (cursor.moveToNext()) {
                news = new DBNews();
                news.id = cursor.getInt(cursor.getColumnIndex(NewsTable.FIELD_ID));
                news.news_id = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_NEWS_ID));
                news.image = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_IMAGE));
                news.title = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_TITLE));
                result.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    @Override
    public DBNews queryById(int id) {
        DBNews result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {NewsTable.FIELD_ID, NewsTable.FIELD_NEWS_ID, NewsTable.FIELD_IMAGE, NewsTable.FIELD_TITLE};
            String selection = NewsTable.FIELD_ID + "=?";
            String[] selectionArgs = {id + ""};
            Cursor cursor = db.query(NewsTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DBNews();
                result.id = cursor.getInt(cursor.getColumnIndex(NewsTable.FIELD_ID));
                result.news_id = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_NEWS_ID));
                result.image = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_IMAGE));
                result.title = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_TITLE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return result;
    }

    public DBNews getNewsByNewsId(String newsId) {
        DBNews result = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] columns = {NewsTable.FIELD_ID, NewsTable.FIELD_NEWS_ID, NewsTable.FIELD_IMAGE, NewsTable.FIELD_TITLE};
            String selection = NewsTable.FIELD_NEWS_ID + "=?";
            String[] selectionArgs = {newsId};
            Cursor cursor = db.query(NewsTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                result = new DBNews();
                result.id = cursor.getInt(cursor.getColumnIndex(NewsTable.FIELD_ID));
                result.news_id = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_NEWS_ID));
                result.image = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_IMAGE));
                result.title = cursor.getString(cursor.getColumnIndex(NewsTable.FIELD_TITLE));
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
            affectedRows = db.delete(NewsTable.TABLE_NAME, null, null);
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
            String whereClause = NewsTable.FIELD_ID + "=?";
            String[] whereArgs = {id + ""};
            affectedRows = db.delete(NewsTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public long deleteByNewsId(String newsId) {
        int affectedRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = NewsTable.FIELD_NEWS_ID + "=?";
            String[] whereArgs = {newsId};
            affectedRows = db.delete(NewsTable.TABLE_NAME, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return affectedRows;
    }

    public int deleteNewses(List<DBNews> doctors) {
        int affectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            String whereClause = NewsTable.FIELD_ID + " = ?";
            for (DBNews d : doctors) {
                String[] whereArgs = {d.id + ""};
                affectedRows += db.delete(NewsTable.TABLE_NAME, whereClause, whereArgs);
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
