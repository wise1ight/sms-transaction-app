package com.karlasa.smstransaction.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karlasa.smstransaction.database.AppDatabaseConst;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kuvh on 2017-03-15.
 */

public class LogListViewAdapter extends CursorAdapter {

    public LogListViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView mMessage = (TextView) view.findViewById(android.R.id.text1);
        final TextView mDatetime = (TextView) view.findViewById(android.R.id.text2);

        int type = cursor.getInt(cursor.getColumnIndex(AppDatabaseConst.COLUMN_TYPE));
        switch(type) {
            case 0:
                mMessage.setText("수신");
            break;
            case 1:
                mMessage.setText("전달 시작");
                break;
            case 2:
                mMessage.setText("전달 성공");
                break;
            case 3:
                mMessage.setText("전달 실패");
                break;
        }

        String date = "";
        try {
            DateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.KOREAN);
            Date netDate = new Date(cursor.getLong(cursor.getColumnIndex(AppDatabaseConst.COLUMN_TIMESTAMP)));
            date = sdf.format(netDate);
        } catch(Exception e){
            e.printStackTrace();
        }

        mDatetime.setText(date);
    }
}
