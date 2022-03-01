package com.karlasa.smstransaction.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.database.AppDatabaseConst;
import com.karlasa.smstransaction.queue.SMSQueue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryListViewAdapter extends CursorAdapter {
    private AppDatabase db;

    public HistoryListViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        db = AppDatabase.getInstance(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView mIconView = (ImageView) view.findViewById(R.id.icon);
        final TextView mAddressView = (TextView) view.findViewById(R.id.address);
        final TextView mBodyView = (TextView) view.findViewById(R.id.body);
        final TextView mDateTime = (TextView) view.findViewById(R.id.datetime);

        if(SMSQueue.getInstance().isExist(cursor.getInt(cursor.getColumnIndex(AppDatabaseConst._ID)))) {
            mIconView.setBackgroundResource(R.drawable.oval_progress);
            mIconView.setImageResource(R.drawable.ic_progress);
        } else {
            if(cursor.getInt(cursor.getColumnIndex(AppDatabaseConst.COLUMN_COMPLETE)) == 0) {
                mIconView.setBackgroundResource(R.drawable.oval_failure);
                mIconView.setImageResource(R.drawable.ic_failure);
            } else {
                mIconView.setBackgroundResource(R.drawable.oval_success);
                mIconView.setImageResource(R.drawable.ic_success);
            }
        }

        String title = String.format(Locale.getDefault(),
                "#%d %s",
                cursor.getInt(cursor.getColumnIndex(AppDatabaseConst._ID)),
                cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_ADDRESS)));
        mAddressView.setText(title);
        mBodyView.setText(cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_BODY)));
        long timestamp = db.getRecentSMSTimestamp(cursor.getLong(cursor.getColumnIndex(AppDatabaseConst._ID)));
        String date = "";
        try {
            DateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.KOREAN);
            Date netDate = new Date(timestamp);
            date = sdf.format(netDate);
        } catch(Exception e){
            e.printStackTrace();
        }
        mDateTime.setText(date);
    }

}
