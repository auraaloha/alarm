package com.aloha.alaram2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by seoseongho on 15. 8. 5..
 */
public class DBhelper {

    private static final String DATABASE_NAME = "aloha.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    public DBhelper(Context mContext) {
        this.mContext = mContext;
    }

    public DBhelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    // Insert DB
    public long insertColumn(int kind, int active, int day, int time, int repeat, int vibration, int sound, String source) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.KIND, kind);
        values.put(Database.CreateDB.ACTIVE, active);
        values.put(Database.CreateDB.DAY, day);
        values.put(Database.CreateDB.TIME, time);
        values.put(Database.CreateDB.REPEAT, repeat);
        values.put(Database.CreateDB.VIB, vibration);
        values.put(Database.CreateDB.SOUND, sound);
        values.put(Database.CreateDB.SOURCE, source);
        return mDB.insert(Database.TABLENAME, null, values);

    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Database.CreateDB.CREATEDB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /*
    public long modifyColumn(int id, int kind, int active, int day, int time, int repeat, int vibration, int sound, String source){

    }
    */
}
