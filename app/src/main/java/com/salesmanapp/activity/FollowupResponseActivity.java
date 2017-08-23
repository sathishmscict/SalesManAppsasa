package com.salesmanapp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.StyleableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.salesmanapp.R;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FollowupResponseActivity extends AppCompatActivity {

    private Button btreschedule;
    private Button btnyes;
    private Button btnno;
    private Context context = this;
    private dbhandler db;
    private SQLiteDatabase sd;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = FollowupResponseActivity.class.getSimpleName();
    private LinearLayout llcancel, llschedule;
    private EditText edtSchedule;
    public EditText edtNote;
    public EditText edtFollowupTime;
    public EditText edtFollowupDate;
    public Spinner spnClients;

    public static final int DATE_PICKER = 120;

    public static final int TIME_PICKER = 121;


    private ArrayList<String> list_client = new ArrayList<String>();

    private ArrayList<String> list_devicetype = new ArrayList<String>();


    private ArrayList<String> list_client_id = new ArrayList<String>();
    private CoordinatorLayout coordinateLayout;
    private TextInputLayout edtscheduleReasonWrapper;
    private TextInputLayout edtNoteWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle("Followup Status ");

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        db = new dbhandler(context);
        sd = db.getWritableDatabase();
        sd = db.getWritableDatabase();

        btreschedule = (Button) findViewById(R.id.btreschedule);
        btnyes = (Button) findViewById(R.id.btnyes);
        btnno = (Button) findViewById(R.id.btnno);

        llcancel = (LinearLayout) findViewById(R.id.llCancel);
        llschedule = (LinearLayout) findViewById(R.id.llSchedule);


        llcancel.setVisibility(View.GONE);
        llschedule.setVisibility(View.GONE);


        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);


        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llcancel.setVisibility(View.VISIBLE);
                llschedule.setVisibility(View.GONE);


                final EditText edtCancelReason = (EditText) findViewById(R.id.edtCancelReason);
                final TextInputLayout edtCancelReasonWrapper = (TextInputLayout) findViewById(R.id.edtCancelReasonWrapper);

                Button btnsave = (Button) findViewById(R.id.btnsavereason);


                Toast.makeText(FollowupResponseActivity.this, "Ask Reason dialog", Toast.LENGTH_SHORT).show();


                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            if (edtCancelReason.getText().toString().equals("")) {
                                edtCancelReasonWrapper.setError("Please enter reason");
                                edtCancelReasonWrapper.setErrorEnabled(true);


                            } else {

                                edtCancelReasonWrapper.setErrorEnabled(false);

                                ContentValues cv = new ContentValues();
                                cv.put(dbhandler.FOLLOWUP_REASON, edtCancelReason.getText().toString());
                                cv.put(dbhandler.FOLLOWUP_STATUS, AllKeys.CANCEL);

                                sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, "" + dbhandler.FOLLOWUP_ID + "=" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "", null);


                                Intent intent = new Intent(context, DashBoardActivity.class);
                                startActivity(intent);
                                finish();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error in update cancel reason : " + e.getMessage());
                        }
                    }
                });


            }
        });

        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(FollowupResponseActivity.this, "Followup done", Toast.LENGTH_SHORT).show();

                try {
                    ContentValues cv = new ContentValues();

                    cv.put(dbhandler.FOLLOWUP_STATUS, AllKeys.YES);
                    cv.put(dbhandler.FOLLOWUP_REASON, "");

                    sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, "" + dbhandler.FOLLOWUP_ID + "=" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "", null);

                    Intent intent = new Intent(context, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, "Status has been updated", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error in update cancel reason : " + e.getMessage());
                }

            }
        });

        btreschedule.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                llcancel.setVisibility(View.GONE);
                llschedule.setVisibility(View.VISIBLE);


                Toast.makeText(FollowupResponseActivity.this, "Ask for reschedule and update status of current followup", Toast.LENGTH_SHORT).show();


                edtNoteWrapper = (TextInputLayout) findViewById(R.id.edtNoteWrapper);
                edtscheduleReasonWrapper = (TextInputLayout) findViewById(R.id.edtscheduleReasonWrapper);


                spnClients = (Spinner) findViewById(R.id.spnClients);
                edtFollowupDate = (EditText) findViewById(R.id.edtFollowupDate);
                edtFollowupTime = (EditText) findViewById(R.id.edtFollowupTime);
                edtNote = (EditText) findViewById(R.id.edtNote);

                spnClients.setEnabled(false);
                edtSchedule = (EditText) findViewById(R.id.edtSchedule);


                Button btnsave = (Button) findViewById(R.id.btnsave);


                int hour = Calendar.getInstance().get(Calendar.HOUR);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);

                setCurrentTime(hour, minute);
                FillDataOnSpinner();

                //Get All Followup details by followupid
                String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + " where " + dbhandler.FOLLOWUP_ID + "=" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "";
                Log.d(TAG, "Query : " + query);


                Cursor cur = sd.rawQuery(query, null);
                Log.d(TAG, "Total " + cur.getCount() + " Records found");

                cur.moveToNext();

                edtNote.setText(cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_DESCR)));

                edtFollowupDate.setText(cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                edtFollowupTime.setText(cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_TIME)));

                try {
                    String clientid = cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ID));
                    int setSelection = list_client_id.indexOf(clientid);
                    spnClients.setSelection(setSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error in selection", Toast.LENGTH_SHORT).show();
                }


                edtFollowupDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (MotionEvent.ACTION_DOWN == event.getAction()) {

                            showDialog(DATE_PICKER);
                        }
                        return false;
                    }
                });

                edtFollowupTime.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (MotionEvent.ACTION_DOWN == event.getAction()) {
                            showDialog(TIME_PICKER);

                        }
                        return false;
                    }
                });


                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            boolean isError = false;
                            if (spnClients.getSelectedItemPosition() == 0) {
                                isError = true;


                            }

                            if (edtNote.getText().toString().equals("")) {
                                isError = true;

                                edtNoteWrapper.setErrorEnabled(true);
                                edtNoteWrapper.setError("Enter note");


                            } else {

                                edtNoteWrapper.setErrorEnabled(false);
                            }

                            if (edtSchedule.getText().toString().equals("")) {

                                isError = true;

                                edtscheduleReasonWrapper.setErrorEnabled(true);
                                edtscheduleReasonWrapper.setError("Enter schedule reason");

                            } else {
                                edtscheduleReasonWrapper.setErrorEnabled(false);

                            }


                            if (isError == false) {

                                ContentValues cv = new ContentValues();

                                cv.put(dbhandler.FOLLOWUP_REASON, edtSchedule.getText().toString());
                                cv.put(dbhandler.FOLLOWUP_STATUS, AllKeys.SCHEDULE);
                                Log.d(TAG, " Update Followup :  " + cv.toString());
                                sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, dbhandler.FOLLOWUP_ID + "=" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID), null);
                                Snackbar.make(coordinateLayout, "Records has been updated ", Snackbar.LENGTH_SHORT).show();
                                Log.d(TAG, "Records has been updated");

                                cv.put(dbhandler.FOLLOWUP_STATUS, AllKeys.DEAFULT);
                                cv.put(dbhandler.CLIENT_ID, list_client_id.get(spnClients.getSelectedItemPosition()));
                                cv.put(dbhandler.FOLLOWUP_DATE, edtFollowupDate.getText().toString());
                                cv.put(dbhandler.FOLLOWUP_TIME, edtFollowupTime.getText().toString());
                                cv.put(dbhandler.FOLLOWUP_DESCR, edtNote.getText().toString());
                                cv.put(dbhandler.EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));
                                cv.put(dbhandler.CLIENT_DEVICE_TYPE, "and");
                                cv.put(dbhandler.FOLLOWUP_REASON, "");
                                cv.put(dbhandler.SYNC_STATUS, "0");
                                sd.insert(dbhandler.TABLE_FOLLOWUP_MASTER, null, cv);

                                Snackbar.make(coordinateLayout, "Records has been updated ", Snackbar.LENGTH_SHORT).show();
                                Log.d(TAG, "Records has been inserted");


                                edtNote.setText("");
                                edtSchedule.setText("");
                                edtFollowupDate.setText("");
                                edtFollowupTime.setText("");
                                spnClients.setSelection(0);

                                Intent intent = new Intent(context, DashBoardActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error in Update Schedule FollowupDAta : " + e.getMessage());
                        }


                    }
                });


            }
        });

    }
    //onCreate Completed


    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_PICKER:
                return new DatePickerDialog(context, datepickerlistener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            case TIME_PICKER:
                return new TimePickerDialog(context, timepickerlistener, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), false
                );

        }
        return null;


    }

    private DatePickerDialog.OnDateSetListener datepickerlistener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setCurrentDate(year, month, dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener timepickerlistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            setCurrentTime(hourOfDay, minute);
        }
    };

    private void setCurrentDate(int year, int month, int day) {

        String mm = "";
        ++month;
        if (month <= 9) {
            mm = "0" + String.valueOf(month);

        } else {
            mm = String.valueOf(month);
        }

        String  str_day = "";
        if(day <= 9 )
        {


            str_day = "0"+String.valueOf(day);
        }
        else
        {
            str_day = String.valueOf(str_day);
        }


        edtFollowupDate.setText(day + "-" + mm + "-" + year);

    }


    private void FillDataOnSpinner() {
        String query = "select * from " + dbhandler.TABLE_CLIENTMASTER + "";
        Log.d(TAG, "Query  : ");

        Cursor cc = sd.rawQuery(query, null);
        Log.d(TAG, "Total 0 Records fouund");
        list_client_id.clear();
        list_client.clear();
        list_devicetype.clear();
        list_client_id.add("0");
        list_client.add("Select Company");
        list_devicetype.add("testing");


        if (cc.getCount() > 0) {
            while (cc.moveToNext()) {
                list_client.add(cc.getString(cc.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)));

                list_client_id.add(cc.getString(cc.getColumnIndex(dbhandler.CLIENT_ID)));
                list_devicetype.add(cc.getString(cc.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)));
            }

        }


        //Set Client details to adapter
        ArrayAdapter<String> adater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_client);
        spnClients.setAdapter(adater);
    }

    private void setCurrentTime(int hour, int minute) {

        String MERIDIAN = "";
        if (hour > 12) {
            hour -= 12;
            MERIDIAN = "PM";

        } else if (hour == 0) {
            hour = 12;

            MERIDIAN = "AM";

        } else if (hour == 12) {
            MERIDIAN = "PM";

        } else {
            MERIDIAN = "AM";

        }


        String min;
        if (minute <= 9) {

            min = "0" + String.valueOf(minute);
        } else {
            min = String.valueOf(minute);

        }

        edtFollowupTime.setText(hour + ":" + min + " " + MERIDIAN);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, FollowupResponseActivity.class);
        startActivity(intent);
        finish();
    }
}
