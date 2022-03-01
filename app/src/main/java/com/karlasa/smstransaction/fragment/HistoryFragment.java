package com.karlasa.smstransaction.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.karlasa.smstransaction.activity.SMSDetailActivity;
import com.karlasa.smstransaction.adapter.HistoryListViewAdapter;
import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.ui.UIEventListener;
import com.karlasa.smstransaction.ui.UIEventManager;

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener, UIEventListener {

    private AppDatabase db;
    private HistoryListViewAdapter adapter;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.navigation_menu_history));

        Context context = view.getContext();
        db = AppDatabase.getInstance(context);
        ListView listView = (ListView) view.findViewById(R.id.list);
        adapter = new HistoryListViewAdapter(context, db.selectSMS(), 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(view.findViewById(android.R.id.empty));

        UIEventManager.getInstance().setListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), SMSDetailActivity.class);
        intent.putExtra("idx", l);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        UIEventManager.getInstance().setListener(this);
    }

    @Override
    public void onUIUpdate(int code, Object data) {

    }

    @Override
    public void onRefresh() {
        adapter.changeCursor(db.selectSMS());
    }
}
