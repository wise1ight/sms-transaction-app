package com.karlasa.smstransaction.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import com.karlasa.smstransaction.Application;
import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.database.AppDatabase;
import com.karlasa.smstransaction.queue.SMSQueue;
import com.karlasa.smstransaction.service.BGService;
import com.karlasa.smstransaction.ui.UIEventListener;
import com.karlasa.smstransaction.ui.UIEventManager;
import com.karlasa.smstransaction.util.ReceiverEnabler;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements UIEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AppDatabase db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView successView, progressView, failureView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.navigation_menu_home));

        db = AppDatabase.getInstance(Application.getContext());

        successView = (TextView) view.findViewById(R.id.success_count);
        progressView = (TextView) view.findViewById(R.id.progress_count);
        failureView = (TextView) view.findViewById(R.id.failure_count);

        SwitchCompat enSwitch = (SwitchCompat) view.findViewById(R.id.enabler_switch);

        final ReceiverEnabler enabler = new ReceiverEnabler(getActivity().getApplicationContext());
        enSwitch.setChecked(enabler.isEnabled());

        enSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enabler.setEnable(isChecked);
                if(!isChecked) {
                    SMSQueue.getInstance().clearQueue();
                    updateStatus();
                } else {
                    SMSQueue.getInstance().initQueue();
                    Application.getContext().startService(new Intent(Application.getContext(), BGService.class));
                }
            }
        });

        updateStatus();

        UIEventManager.getInstance().setListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UIEventManager.getInstance().setListener(this);
    }

    private void updateStatus() {
        int workingCount = SMSQueue.getInstance().getWorkingCount();
        progressView.setText(String.valueOf(workingCount));

        Cursor cursor = db.selectSMS();
        int successCount = cursor.getCount();
        cursor.close();

        cursor = db.getSMSList();
        failureView.setText(String.valueOf(cursor.getCount() - workingCount));

        successCount -= cursor.getCount();
        successView.setText(String.valueOf(successCount));
        cursor.close();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onUIUpdate(int code, Object data) {

    }

    @Override
    public void onRefresh() {
        updateStatus();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
