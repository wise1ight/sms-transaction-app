package com.karlasa.smstransaction.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karlasa.smstransaction.ui.UIEventManager;

import static com.karlasa.smstransaction.database.AppDatabaseConst.*;

/**
 * Created by kuvh on 2016-12-26.
 */

public class AppDatabase {

    private static AppDatabase instance;
    private Context context;
    private SQLiteDatabase db;

    private AppDatabase(Context context) {
        this.context = context;
    }

    public synchronized static AppDatabase getInstance(Context context) {
        instance = new AppDatabase(context);
        if (!instance.open(context))
            instance = null;

        return instance;
    }

    private boolean open(Context context) {
        AppDatabaseHelper helper = new AppDatabaseHelper(context, AppDatabaseConst.DB_NAME, null, AppDatabaseConst.DB_VERSION);

        try {
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return db != null;
    }

    // TABLE SMS
    public Cursor selectSMS() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s DESC", TABLE_SMS, _ID);

        return db.rawQuery(sql, null);
    }

    public long insertSMS(String address, String body, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_BODY, body);
        values.put(COLUMN_COMPLETE, false);
        values.put(COLUMN_TIMESTAMP, timestamp);

        return db.insert(TABLE_SMS, null, values);
    }

    public Cursor getSMSList() {
        String sql = String.format("SELECT * FROM %s WHERE %s=0", TABLE_SMS, COLUMN_COMPLETE);
        return db.rawQuery(sql, null);
    }

    public long updateSMS(long idx, boolean bool) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETE, bool);

        return db.update(TABLE_SMS, values, "_id=?", new String[]{String.valueOf(idx)});
    }

    public void insertLog(long idx, TYPE type, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SMS_IDX, idx);
        values.put(COLUMN_TYPE, type.getValue());
        values.put(COLUMN_TIMESTAMP, timestamp);
        db.insert(TABLE_LOG, null, values);

        UIEventManager.getInstance().refreshUI();
    }

    public Cursor getSMSByIDX(long idx) {
        String sql = String.format("SELECT * FROM %s WHERE %s='" + idx + "'", TABLE_SMS, _ID);
        return db.rawQuery(sql, null);
    }

    public long getRecentSMSTimestamp(long idx) {
        String sql = String.format("SELECT * FROM %s WHERE %s='" + idx + "' ORDER BY %s DESC", TABLE_LOG, COLUMN_SMS_IDX, COLUMN_TIMESTAMP);

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            long timestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            cursor.close();
            return timestamp;
        } else {
            return 0;
        }
    }

    public void insertRule(String address) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, address);
        db.insert(TABLE_RULE, null, values);
    }

    public void removeRule(String address) {
        db.delete(TABLE_RULE, String.format("%s=?", COLUMN_ADDRESS), new String[]{address});
    }

    public Cursor selectRules() {
        String sql = String.format("SELECT * FROM %s", TABLE_RULE);

        return db.rawQuery(sql, null);
    }

    public Cursor getSMSLog(long idx) {
        String sql = String.format("SELECT * FROM %s WHERE %s='" + idx + "' ORDER BY %s DESC", TABLE_LOG, COLUMN_SMS_IDX, COLUMN_TIMESTAMP);

        return db.rawQuery(sql, null);
    }

    public boolean isAddressinRule(String address) {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_RULE, COLUMN_ADDRESS, address);

        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
}
