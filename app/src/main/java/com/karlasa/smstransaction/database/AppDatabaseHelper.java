package com.karlasa.smstransaction.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import static com.karlasa.smstransaction.database.AppDatabaseConst.*;

/**
 * Created by kuvh on 2016-12-26.
 */

public class AppDatabaseHelper extends SQLiteOpenHelper {

    public AppDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sms_table_sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s NUMERIC, %s NUMERIC) ", TABLE_SMS, _ID, COLUMN_ADDRESS, COLUMN_BODY, COLUMN_TIMESTAMP, COLUMN_COMPLETE);
        String log_table_sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s NUMERIC) ", TABLE_LOG, _ID, COLUMN_SMS_IDX, COLUMN_TYPE, COLUMN_TIMESTAMP);
        String rule_table_sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT) ", TABLE_RULE, _ID, COLUMN_ADDRESS);
        db.execSQL(sms_table_sql);
        db.execSQL(log_table_sql);
        db.execSQL(rule_table_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
