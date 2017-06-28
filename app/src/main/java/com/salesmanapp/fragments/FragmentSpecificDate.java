package com.salesmanapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salesmanapp.R;
import com.salesmanapp.adapter.FollowupDataAdapterRecyclerView;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.FollowupData;
import com.salesmanapp.session.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class FragmentSpecificDate extends Fragment {


    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();

    private dbhandler db;
    private SQLiteDatabase sd;

    private TextView txtnodata;
    private RecyclerView rv_followup;
    private String TAG = FragmentSpecificDate.class.getSimpleName();
    private ArrayList<FollowupData> list_followups = new ArrayList<FollowupData>();
    private TextInputLayout edtDateWrapper;
    private static EditText edtDate;
    private Button btnGo;
    private static final int DATE_PICKER = 101;
    private LinearLayout llSpecificDate;


    public FragmentSpecificDate() {
        // Required empty public constructor
    }

	/*
     * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setHasOptionsMenu(true);
	 *
	 *
	 * }
	 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_followup, container,
                false);

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }


        sessionmanager = new SessionManager(getActivity());
        userDetails = sessionmanager.getSessionDetails();


        db = new dbhandler(getActivity());
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();

        txtnodata = (TextView) rootView.findViewById(R.id.txtnodata);
        rv_followup = (RecyclerView) rootView.findViewById(R.id.rv_followup);

        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(getActivity());
        rv_followup.setLayoutManager(lmanagr);
        rv_followup.setItemAnimator(new DefaultItemAnimator());

        llSpecificDate = (LinearLayout) rootView.findViewById(R.id.llSpecificDate);
        llSpecificDate.setVisibility(View.VISIBLE);
        edtDateWrapper = (TextInputLayout) rootView.findViewById(R.id.edtDateWrapper);
        edtDate = (EditText) rootView.findViewById(R.id.edtDate);
        btnGo = (Button) rootView.findViewById(R.id.btnGo);


        setCurrentDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        edtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {


                    DialogFragment newFragment = new SelectDateFragment();
                    newFragment.show(getFragmentManager(), "DatePicker");


                }
                return false;
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FillDataOnRecyclerView(edtDate.getText().toString());

            }
        });


        FillDataOnRecyclerView("");

        //registerForContextMenu(mRecyclerView_goal);

        // Inflate the layout for this fragment
        return rootView;
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm, dd);
        }

        public void populateSetDate(int year, int month, int day) {

            setCurrentDate(year, month, day);
            //edtDate.setText(day+"-"+month+"-"+year);

        }

    }

    private static void setCurrentDate(int year, int month, int day) {

        ++month;
        String str_month = "";
        if (month <= 9) {
            str_month = "0" + String.valueOf(month);

        } else {
            str_month = String.valueOf(month);
        }

        String str_day;
        if (day <= 9) {
            str_day = "0" + String.valueOf(day);

        } else {
            str_day = String.valueOf(day);
        }

        edtDate.setText(str_day + "-" + str_month + "-" + year);
    }


    private void FillDataOnRecyclerView(String date) {

        String query;
        if (date.equals("")) {
            query = "select *,fm." + dbhandler.CLIENT_DEVICE_TYPE + " as DevicType  from " + dbhandler.TABLE_FOLLOWUP_MASTER + " as fm," + dbhandler.TABLE_CLIENTMASTER + "  as cm where cm." + dbhandler.CLIENT_ID + " =fm." + dbhandler.CLIENT_ID;


        } else {
            query = "select *,fm." + dbhandler.CLIENT_DEVICE_TYPE + " as DevicType  from " + dbhandler.TABLE_FOLLOWUP_MASTER + " as fm," + dbhandler.TABLE_CLIENTMASTER + "  as cm where cm." + dbhandler.CLIENT_ID + " =fm." + dbhandler.CLIENT_ID + " and fm." + dbhandler.FOLLOWUP_DATE + "='" + date + "'";
        }


        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_followups.clear();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                //FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
                FollowupData followupData = new FollowupData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)), c.getString(c.getColumnIndex("DevicType")), c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)), c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                //ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                list_followups.add(followupData);


            }


            txtnodata.setVisibility(View.GONE);
            rv_followup.setVisibility(View.VISIBLE);

            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(getActivity(), list_followups, getActivity(), "dashboard");
            rv_followup.setAdapter(adapter);


        } else {
            Toast.makeText(getActivity(), "No client records found", Toast.LENGTH_SHORT).show();

            txtnodata.setVisibility(View.VISIBLE);
            rv_followup.setVisibility(View.GONE);
        }


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // pDialog.dismiss();
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    //Complet Getting Category Details From Server


}