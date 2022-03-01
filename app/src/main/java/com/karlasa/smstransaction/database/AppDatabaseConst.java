package com.karlasa.smstransaction.database;

import android.provider.BaseColumns;

/**
 * Created by kuvh on 2017-01-01.
 */

public class AppDatabaseConst implements BaseColumns {

    public static final String DB_NAME = "AppDB.sqlite";
    public static final int DB_VERSION = 1;
    public static final String TABLE_SMS = "sms";
    public static final String TABLE_LOG = "log";
    public static final String TABLE_RULE = "rule";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_COMPLETE = "complete";
    public static final String COLUMN_SMS_IDX = "sms_idx";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public enum TYPE {
        RECEIVE(0), RELAY(1), SUCCESS(2), FAILURE(3);

        private final int value;

        TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
