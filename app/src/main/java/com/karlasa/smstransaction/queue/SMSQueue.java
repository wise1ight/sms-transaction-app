package com.karlasa.smstransaction.queue;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.karlasa.smstransaction.Application;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.database.AppDatabaseConst;
import com.karlasa.smstransaction.service.BGService;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by kuvh on 2017-03-15.
 */

public class SMSQueue {
    private AppDatabase db;
    private static SMSQueue instance;

    private SMSQueue() {
        db = AppDatabase.getInstance(Application.getContext());
        initQueue();
    }

    public void initQueue() {
        Cursor cursor = db.getSMSList();
        while(cursor.moveToNext()) {
            Log.d("SMSQueue", cursor.getLong(cursor.getColumnIndex(AppDatabaseConst._ID)) + "번 메시지 큐에 추가");
            push(cursor.getLong(cursor.getColumnIndex(AppDatabaseConst._ID)));
        }
        cursor.close();
    }

    public static SMSQueue getInstance() {
        if (instance == null) {
            instance = new SMSQueue();
        }
        return instance;
    }

    private Queue<Long> queue = new LinkedList<>();

    public boolean push(long l) {
        Cursor cursor = db.getSMSByIDX(l);
        if(cursor.getCount() == 0) {
            return false;
        }

        cursor.moveToFirst();

        if(!queue.contains(l) && cursor.getInt(cursor.getColumnIndex(AppDatabaseConst.COLUMN_COMPLETE)) == 0) {
            queue.offer(l);

            Application.getContext().startService(new Intent(Application.getContext(), BGService.class));

            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean isExist(long l) {
        return queue.contains(l);
    }

    public long pull() {
        if(queue.peek() != null) {
            return queue.poll();
        } else {
            return -1;
        }
    }

    public int getWorkingCount() {
        return queue.size();
    }

    public void clearQueue() {
        queue.clear();
    }

}
