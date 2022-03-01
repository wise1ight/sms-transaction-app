package com.karlasa.smstransaction.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.database.AppDatabaseConst;
import com.karlasa.smstransaction.queue.SMSQueue;

/**
 * Created by kuvh on 2016-11-18.
 */

public class SMSReceiver extends BroadcastReceiver {

    private AppDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                for(int i = 0; i < messages.length;i++)
                {
                    smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i], format);
                }
            } else {
                for(int i = 0; i < messages.length;i++)
                {
                    smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
                }
            }

            db = AppDatabase.getInstance(context);
            if (db.isAddressinRule(smsMessage[0].getOriginatingAddress())) {
                long idx = db.insertSMS(smsMessage[0].getOriginatingAddress(), smsMessage[0].getMessageBody(), smsMessage[0].getTimestampMillis());
                db.insertLog(idx, AppDatabaseConst.TYPE.RECEIVE, smsMessage[0].getTimestampMillis());
                SMSQueue.getInstance().push(idx);
            }
        }
    }
}
