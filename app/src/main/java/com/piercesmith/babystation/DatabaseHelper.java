package com.piercesmith.babystation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "logs.db";
    public static final String TABLE_NAME = "logsTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "CHILD";
    public static final String COL_4 = "CATEGORY";
    public static final String COL_5 = "COMMENTS";
    public static final String COL_6 = "TIME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2+" TEXT, "+COL_3+" TEXT, "+COL_4+" TEXT, "+COL_5+" TEXT, "+COL_6+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String child, String category, String comments, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put(COL_2, title);
        data.put(COL_3, child);
        data.put(COL_4, category);
        data.put(COL_5, comments);
        data.put(COL_6, time);

        long result = db.insert(TABLE_NAME, null, data);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor recentLogs() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC LIMIT 25", null);
        return c;
    }

    public Cursor sortCat(String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE CATEGORY=? ORDER BY ID DESC", new String[] {category});
        return c;
    }

    public Cursor sortChild(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE CHILD=? ORDER BY ID DESC", new String[] {name});
        return c;
    }

    public Cursor searchData(String search) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TITLE LIKE ? ORDER BY ID DESC", new String[] {"%" + search + "%"});
        return c;
    }

    public boolean updateData(String id, String title, String child, String category, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put(COL_2, title);
        data.put(COL_3, child);
        data.put(COL_4, category);
        data.put(COL_5, comments);

        db.update(TABLE_NAME, data, "ID = ?", new String[] {id});
        return true;
    }

    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }
}
