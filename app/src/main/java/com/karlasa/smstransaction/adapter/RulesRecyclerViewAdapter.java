package com.karlasa.smstransaction.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karlasa.smstransaction.database.AppDatabaseConst;

public class RulesRecyclerViewAdapter extends CursorAdapter {

    public RulesRecyclerViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView tv = (TextView) view.findViewById(android.R.id.text1);

        tv.setText(cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_ADDRESS)));
    }

}
