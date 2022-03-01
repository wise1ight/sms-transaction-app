package com.karlasa.smstransaction.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.karlasa.smstransaction.Application;
import com.karlasa.smstransaction.api.RemoteServerService;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.database.AppDatabaseConst;
import com.karlasa.smstransaction.queue.SMSQueue;
import com.karlasa.smstransaction.ui.UIEventManager;
import com.karlasa.smstransaction.util.UniqueFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by kuvh on 2017-03-15.
 */

public class BGService extends Service {
    private AppDatabase db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        db = AppDatabase.getInstance(Application.getContext());

        Thread thread = new Thread(new APICall());
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private class APICall implements Runnable {

        private Handler handler = new Handler();
        private SMSQueue queue = SMSQueue.getInstance();

        @Override
        public void run() {
            while(queue.getWorkingCount() != 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        UIEventManager.getInstance().refreshUI();
                    }
                });

                final long l = queue.pull();

                if(l == -1) {
                    return ;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        db.insertLog(l, AppDatabaseConst.TYPE.RELAY, System.currentTimeMillis());
                    }
                });

                Cursor cursor = db.getSMSByIDX(l);
                cursor.moveToFirst();

                RemoteServerService remote = RemoteServerService.retrofit.create(RemoteServerService.class);
                final Call<ResponseBody> call = remote.getResponse(cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_ADDRESS))
                        ,cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_BODY)), cursor.getLong(cursor.getColumnIndex(AppDatabaseConst.COLUMN_TIMESTAMP)) / 1000L, UniqueFactory.build());
                cursor.close();

                boolean isSuccess = false;
                try {
                    isSuccess = call.execute().isSuccessful();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(isSuccess) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            db.insertLog(l, AppDatabaseConst.TYPE.SUCCESS, System.currentTimeMillis());
                            db.updateSMS(l, true);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            db.insertLog(l, AppDatabaseConst.TYPE.FAILURE, System.currentTimeMillis());
                            queue.push(l);
                        }
                    });
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        UIEventManager.getInstance().refreshUI();
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            stopSelf();
        }
    }
}
