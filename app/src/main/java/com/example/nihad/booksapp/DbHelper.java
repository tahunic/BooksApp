package com.example.nihad.booksapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nihad on 12/01/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "booksApp";
    public static final String TABLE_CHAPTER = "chapter";
    public static final String TABLE_BOOKMARK = "bookmark";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHAPTER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CONTENT TEXT, BOOK TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_BOOKMARK + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PAGE INTEGER, CHAPTER TEXT, BOOK TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
        onCreate(sqLiteDatabase);
    }

    public void insertChapterData(String name, String content, String book) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("CONTENT", content);
        contentValues.put("BOOK", book);
        db.insert(TABLE_CHAPTER, null, contentValues);
    }

    public Cursor getChapterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return null;
        }

        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_CHAPTER, null);
        return result;
    }

    public void insertBookmarkData(Integer page, String chapter, String book) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("PAGE", page);
        contentValues.put("CHAPTER", chapter);
        contentValues.put("BOOK", book);
        db.insert(TABLE_BOOKMARK, null, contentValues);
    }

    public boolean isDatabaseEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return true;
        }

        Cursor result = db.rawQuery("SELECT ID FROM " + TABLE_CHAPTER, null);

        return result.getCount() == 0;
    }

    public Cursor getBookmarkData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return null;
        }

        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARK, null);

        return result;
    }

    public boolean bookmarkExists(String page) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return false;
        }

        Cursor result = db.rawQuery("SELECT ID FROM " + TABLE_BOOKMARK + " WHERE PAGE = ?", new String[] { page });

        return result.getCount() != 0;
    }

    public Integer deleteBookmarkData(String page) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return null;
        }

        return db.delete(TABLE_BOOKMARK, "PAGE = ?", new String[] { page });
    }
}
