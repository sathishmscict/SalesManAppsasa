package com.salesmanapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.R;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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
                                cv.put(dbhandler.SYNC_STATUS,"0");

                                sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, "" + dbhandler.FOLLOWUP_ID + "='" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "'", null);


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
                    cv.put(dbhandler.SYNC_STATUS,"0");

                    sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, "" + dbhandler.FOLLOWUP_ID + "='" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "'", null);

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
                String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + " where " + dbhandler.FOLLOWUP_ID + "='" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID) + "'";
                Log.d(TAG, "Query : " + query);


                final Cursor cur = sd.rawQuery(query, null);
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



                                Dexter.withActivity(FollowupResponseActivity.this)
                                        .withPermission(Manifest.permission.WRITE_CALENDAR)
                                        .withListener(new PermissionListener()
                                        {
                                            @Override
                                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                                try {

                                                    int calenderId = -1;
                                                    String calenderEmaillAddress = userDetails.get(SessionManager.KEY_EMP_EMAIL);
                                                    String[] projection = new String[]{
                                                            CalendarContract.Calendars._ID,
                                                            CalendarContract.Calendars.ACCOUNT_NAME};
                                                    ContentResolver cr = context.getContentResolver();
                                                    Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                                                            CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                                                                    CalendarContract.Calendars.NAME + "=? or " +
                                                                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                                                            new String[]{calenderEmaillAddress, calenderEmaillAddress,
                                                                    calenderEmaillAddress}, null);

                                                    if (cursor.moveToFirst()) {

                                                        if (cursor.getString(1).equals(calenderEmaillAddress)) {

                                                            calenderId = cursor.getInt(0);
                                                        }
                                                    }




                                                    String notifytime = edtFollowupTime.getText().toString();
                                                    Log.d(TAG, "Time Before: " + notifytime);
                                                    notifytime = notifytime.replace(" ", ":");
                                                    Log.d(TAG, "Time After: " + notifytime);

                                                    List<String> myTimeList = new ArrayList<String>(Arrays.asList(notifytime.split(":")));
                                                    Log.d(TAG, "Time In List " + myTimeList.toString());

                                                    Calendar calendar = Calendar.getInstance();

                                                    calendar.set(Calendar.MINUTE, Integer.parseInt(myTimeList.get(1)));
                                                    Log.d(TAG, "Alaram Minute :" + Integer.parseInt(myTimeList.get(1)));
                                                    //   calendar.set(Calendar.SECOND, 00);
                                                    if (myTimeList.get(2).toLowerCase().equals("am"))
                                                    {

                                                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(myTimeList.get(0)));
                                                        Log.d(TAG, "Alaram Hour Of Day : " + Integer.parseInt(myTimeList.get(0)));
                                                        // calendar.set(Calendar.AM_PM,Calendar.AM);
                                                        Log.d(TAG, "Meridian AM");
                                                        Log.d(TAG, "Meridian AM " + calendar.get(Calendar.AM_PM));
                                                    } else {

                                                        if (Integer.parseInt(myTimeList.get(0)) == 12) {
                                                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(myTimeList.get(0)));
                                                            Log.d(TAG, "Alaram Hour Of Day : " + Integer.parseInt(myTimeList.get(0)));
                                                        } else {
                                                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(myTimeList.get(0)) + 12);
                                                            Log.d(TAG, "Alaram Hour Of Day : " + Integer.parseInt(myTimeList.get(0)) + 12);
                                                        }

                                                        //   calendar.set(Calendar.AM_PM,Calendar.PM);
                                                        Log.d(TAG, "Meridian PM");
                                                        Log.d(TAG, "Meridian PM " + calendar.get(Calendar.AM_PM));
                                                    }
                                                    //calendar.set(Calendar.AM_PM,calendar.get(Calendar.AM_PM));
                                                    Log.d(TAG, "Meridian AMPM " + calendar.get(Calendar.AM_PM));

                                                    //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 50000, AlarmManager.INTERVAL_DAY , pendingIntent);  //set repeating every 24 hours

                                                    String selected_date = edtFollowupDate.getText().toString();
                                                    Log.d(TAG, "Date Before: " + selected_date);

                                                    List<String> myDateList = new ArrayList<String>(Arrays.asList(selected_date.split("-")));
                                                    Log.d(TAG, "Date In List " + myDateList.toString());


                                                    calendar.set(Calendar.YEAR, Integer.parseInt(myDateList.get(2)));
                                                    int month = Integer.parseInt(myDateList.get(1));
                                                    calendar.set(Calendar.MONTH, --month);
                                                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(myDateList.get(0)));




                                                    long start2 = calendar.getTimeInMillis(); // 2011-02-12 12h00
                                                    long end2 = calendar.getTimeInMillis() + (1 * 60 * 60 * 1000);   // 2011-02-12 13h00

                                                    String title = edtSchedule.getText().toString();

                                                    ContentValues cvEvent = new ContentValues();
                                                    cvEvent.put("calendar_id", calenderId);
                                                    cvEvent.put(CalendarContract.Events.TITLE, String.valueOf(list_client.get(spnClients.getSelectedItemPosition())));

                                                    cvEvent.put(CalendarContract.Events.DESCRIPTION, String.valueOf(edtSchedule.getText().toString()));
                                                    //cvEvent.put(CalendarContract.Events.EVENT_LOCATION, "Bhatar,Surat");
                                                    cvEvent.put("dtstart", start2);
                                                    cvEvent.put("hasAlarm", 1);
                                                    cvEvent.put("dtend", end2);
                                                    //cvEvent.put("","");
                                                    cvEvent.put("eventTimezone", TimeZone.getDefault().getID());


                                                    Uri uri = getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), cvEvent);


// get the event ID that is the last element in the Uri
                                                    long eventID = Long.parseLong(uri.getLastPathSegment());


                                                    ContentValues values = new ContentValues();

                                                    values.put(CalendarContract.Reminders.MINUTES, 2);
                                                    values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                                                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);
                                                    cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
                                                    //Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }

                                            @Override
                                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                                        }).check();



                                ContentValues cv = new ContentValues();

                                cv.put(dbhandler.FOLLOWUP_REASON, edtSchedule.getText().toString());
                                cv.put(dbhandler.FOLLOWUP_STATUS, AllKeys.SCHEDULE);
                                cv.put(dbhandler.SYNC_STATUS , "0");
                                Log.d(TAG, " Update Followup :  " + cv.toString());
                                sd.update(dbhandler.TABLE_FOLLOWUP_MASTER, cv, dbhandler.FOLLOWUP_ID + "='" + userDetails.get(SessionManager.KEY_FOLLOWUP_ID)+"'", null);
                                Snackbar.make(coordinateLayout, "Records has been updated ", Snackbar.LENGTH_SHORT).show();
                                Log.d(TAG, "Records has been updated");


                                String query_maxfollowupid ="SELECT * FROM " + dbhandler.TABLE_FOLLOWUP_MASTER+" where "+ dbhandler.FOLLOWUP_ID +" like '%"+AllKeys.KEYWORD_FOLLOWUP + userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE) +"%'";
                                Log.d(TAG , "Query for MAX Followup ID : "+query_maxfollowupid);
                                Cursor cur_max_followupid = sd.rawQuery(query_maxfollowupid, null);

                                //cur_max_clientid.moveToFirst();
                                int max_followupid = cur_max_followupid.getCount();
                                ++max_followupid;
                                Log.d("Max Id By Followupid : ", "" + max_followupid);




                                cv.put(dbhandler.FOLLOWUP_ID, AllKeys.KEYWORD_FOLLOWUP+userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE)+max_followupid);

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
                                Log.d(TAG, "Followup Data : "+cv.toString());
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


        edtFollowupDate.setText(str_day + "-" + mm + "-" + year);

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
