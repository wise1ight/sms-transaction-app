package com.karlasa.smstransaction.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.adapter.RulesRecyclerViewAdapter;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.database.AppDatabaseConst;

public class RulesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private AppDatabase db;
    private RulesRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RulesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RulesFragment newInstance() {
        RulesFragment fragment = new RulesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules_list, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.navigation_menu_rules));

        setHasOptionsMenu(true);

        Context context = view.getContext();
        db = AppDatabase.getInstance(context);
        ListView listView = (ListView) view.findViewById(R.id.list);
        TextView emptyView = (TextView) view.findViewById(android.R.id.empty);
        adapter = new RulesRecyclerViewAdapter(context, db.selectRules(), 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_rules_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddRuleDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddRuleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("항목 추가");
        builder.setMessage("수신할 전화번호를 입력하세요.");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String address = input.getText().toString();
                if (address.isEmpty()) {
                    Toast.makeText(getContext(), "잘못된 규칙입니다.", Toast.LENGTH_SHORT).show();
                } else if (AppDatabase.getInstance(getContext()).isAddressinRule(address)) {
                    Toast.makeText(getContext(), "이미 존재하는 규칙입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AppDatabase.getInstance(getContext()).insertRule(address);
                    adapter.changeCursor(db.selectRules());
                }
            }
        });

        builder.setNegativeButton("취소", null);

        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final int pos = i;
        builder.setMessage("정말 규칙을 삭제하시겠습니까?").setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Cursor cursor = (Cursor) adapter.getItem(pos);
                        db.removeRule(cursor.getString(cursor.getColumnIndex(AppDatabaseConst.COLUMN_ADDRESS)));
                        adapter.changeCursor(db.selectRules());
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }
}
