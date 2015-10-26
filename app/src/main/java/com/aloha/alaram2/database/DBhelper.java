package com.aloha.alaram2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aloha.alaram2.adapter.Alarm;
import com.aloha.alaram2.interfaces.AlarmChangeListener;

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
    private AlarmChangeListener alarmChangeListener;

    public DBhelper(Context mContext, AlarmChangeListener listener) {
        this.mContext = mContext;
        alarmChangeListener = listener;
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
    public long insertColumn(int kind, int active, int day, int time, int repeat, int vib, int sound, String source) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.KIND, kind);
        values.put(Database.CreateDB.ACTIVE, active);
        values.put(Database.CreateDB.DAY, day);
        values.put(Database.CreateDB.TIME, time);
        values.put(Database.CreateDB.REPEAT, repeat);
        values.put(Database.CreateDB.VIB, vib);
        values.put(Database.CreateDB.SOUND, sound);
        values.put(Database.CreateDB.SOURCE, source);

        long result = mDB.insert(Database.TABLENAME, null, values);
        alarmChangeListener.onAlarmDataCreated();

        return result;

    }

    public long modifyColumn(Alarm alarm) {

        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.KIND, alarm.getKind());
        values.put(Database.CreateDB.ACTIVE, alarm.getActive());
        values.put(Database.CreateDB.DAY, alarm.getDay());
        values.put(Database.CreateDB.TIME, alarm.getTime());
        values.put(Database.CreateDB.REPEAT, alarm.getRepeat());
        values.put(Database.CreateDB.VIB, alarm.getVib());
        values.put(Database.CreateDB.SOUND, alarm.getSound());
        values.put(Database.CreateDB.SOURCE, alarm.getSource());

        Log.v("ID", Integer.toString(alarm.getId()));
        Log.v("TAG", Integer.toString(alarm.getId()));

        long result = mDB.update(Database.TABLENAME, values, Database.CreateDB._ID + "=?", new String[]{Integer.toString(alarm.getId())});
        alarmChangeListener.onAlarmDataChanged();

        return result;

    }

    public long deleteColumn(int id) {
        long result = mDB.delete(Database.TABLENAME, Database.CreateDB._ID + "=?", new String[]{Integer.toString(id)});
        alarmChangeListener.onAlarmDataCreated();

        return result;
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


}
