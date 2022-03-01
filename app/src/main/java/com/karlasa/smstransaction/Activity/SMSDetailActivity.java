package com.karlasa.smstransaction.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.adapter.LogListViewAdapter;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.queue.SMSQueue;
import com.karlasa.smstransaction.ui.UIEventListener;
import com.karlasa.smstransaction.ui.UIEventManager;

public class SMSDetailActivity extends AppCompatActivity implements UIEventListener{

    private AppDatabase db;
    private ListView listView;
    private LogListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();

        UIEventManager.getInstance().setListener(this);

        db = AppDatabase.getInstance(this);
        Cursor cursor = db.getSMSLog(intent.getLongExtra("idx", -1));
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "정상적인 데이터가 아닙니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        adapter = new LogListViewAdapter(this, cursor, 0);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SMSQueue.getInstance().push(intent.getLongExtra("idx", -1))) {
                    Snackbar.make(view, "전송 큐에 넣었습니다.", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(SMSDetailActivity.this, "전송 중이거나, 이미 처리한 메시지 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUIUpdate(int code, Object data) {

    }

    @Override
    public void onRefresh() {
        adapter.changeCursor(db.getSMSLog(getIntent().getLongExtra("idx", -1)));
    }
}
