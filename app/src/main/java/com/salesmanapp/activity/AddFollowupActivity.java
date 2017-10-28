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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddFollowupActivity extends AppCompatActivity {

    private Button btnsave;
    private Spinner spnClients;
    private EditText edtFollowupDate;
    private EditText edtFollowupTime;
    private EditText edtDescr;
    private SessionManager sessionManager;
    private Context context = this;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private CoordinatorLayout coordinateLayout;
    private dbhandler db;
    private SQLiteDatabase sd;
    private String TAG = AddFollowupActivity.class.getSimpleName();
    private ArrayList<String> list_client = new ArrayList<String>();

    private ArrayList<String> list_devicetype = new ArrayList<String>();


    private ArrayList<String> list_client_id = new ArrayList<String>();
    private int year,month,day;
    public static final int DATE_PICKER=120;
    private int minute,hour;
    public static final  int TIME_PICKER=121;
    private String FOLLOWUPID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_followup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle("Add Follow Up Details");

        try {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {
            Log.d("Error Actionbar", "" + e.getMessage());

        }



        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        db = new dbhandler(context);
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();


        spnClients = (Spinner) findViewById(R.id.spnClients);
        edtFollowupDate = (EditText) findViewById(R.id.edtFollowupDate);
        edtFollowupTime = (EditText) findViewById(R.id.edtFollowupTime);
        edtDescr = (EditText) findViewById(R.id.edtNote);

        btnsave = (Button) findViewById(R.id.btnsave);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        //++month;
        day = cal.get(Calendar.DAY_OF_MONTH);

        setCurrentDate(year,month,day);


        hour =cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        setCurrentTime(hour,minute);
        FillDataOnSpinner();

        edtFollowupDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_DOWN == event.getAction())
                {

                    showDialog(DATE_PICKER);
                }
                return false;
            }
        });

        edtFollowupTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction())
                {
                    showDialog(TIME_PICKER);

                }
                return false;
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isError = false;
                if (spnClients.getSelectedItemPosition() == 0) {
                    isError = true;


                }


             /*   Event event = new Event()
                        .setSummary("Google I/O 2015")
                        .setLocation("800 Howard St., San Francisco, CA 94103")
                        .setDescription("A chance to hear more about Google's developer products.");


*/
                if (isError == false)
                {

                    ContentValues cv = new ContentValues();





                    cv.put(dbhandler.FOLLOWUP_DATE, edtFollowupDate.getText().toString());
                    cv.put(dbhandler.FOLLOWUP_TIME, edtFollowupTime.getText().toString());
                    cv.put(dbhandler.FOLLOWUP_DESCR, edtDescr.getText().toString());
                    cv.put(dbhandler.EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));


                    if (btnsave.getText().toString().toLowerCase().equals("update data")) {

                        try {
                            cv.put(dbhandler.FOLLOWUP_DESCR, edtDescr.getText().toString());
                            cv.put(dbhandler.FOLLOWUP_REASON, getIntent().getStringExtra(dbhandler.FOLLOWUP_REASON));
                            cv.put(dbhandler.CLIENT_DEVICE_TYPE, getIntent().getStringExtra(dbhandler.CLIENT_DEVICE_TYPE));
                            cv.put(dbhandler.SYNC_STATUS, "0");
                            Log.d(TAG, " Update Followup :  "+cv.toString());
                            Log.d(TAG , "Fields : "+dbhandler.FOLLOWUP_ID +"='"+ FOLLOWUPID +"' and "+ dbhandler.CLIENT_ID +"='"+ getIntent().getStringExtra(dbhandler.CLIENT_ID) +"'");
                            sd.update(dbhandler.TABLE_FOLLOWUP_MASTER ,cv, dbhandler.FOLLOWUP_ID +"='"+ FOLLOWUPID +"' and "+ dbhandler.CLIENT_ID +"='"+ getIntent().getStringExtra(dbhandler.CLIENT_ID) +"'",null);
                            Snackbar.make(coordinateLayout, "Records has been updated ", Snackbar.LENGTH_SHORT).show();
                            btnsave.setText("Add Followup");
                        } catch (Exception e) {
                            Log.d(TAG , "Error message : "+e.getMessage());
                            e.printStackTrace();
                        }


                    }
                    else
                    {


                        Cursor cur_max_followupid = sd.rawQuery("SELECT * FROM " + dbhandler.TABLE_FOLLOWUP_MASTER+" where "+ dbhandler.FOLLOWUP_ID +" like '%"+ AllKeys.KEYWORD_FOLLOWUP + userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE) +"%'", null);
                        Log.d(TAG , "Query for MAX Followup ID : "+cur_max_followupid);

                        //cur_max_clientid.moveToFirst();
                        int max_followupid = cur_max_followupid.getCount();
                        ++max_followupid;
                        Log.d("Max Id By Goal : ", "" + max_followupid);


                        cv.put(dbhandler.FOLLOWUP_ID, AllKeys.KEYWORD_FOLLOWUP+userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE)+max_followupid);
                        cv.put(dbhandler.CLIENT_ID, list_client_id.get(spnClients.getSelectedItemPosition()));

                        cv.put(dbhandler.FOLLOWUP_STATUS, "0");
                        cv.put(dbhandler.CLIENT_DEVICE_TYPE, "and");
                        cv.put(dbhandler.FOLLOWUP_REASON, "");
                        cv.put(dbhandler.SYNC_STATUS, "0");
                        Log.d(TAG, " Insert Followup :  "+cv.toString());
                        sd.insert(dbhandler.TABLE_FOLLOWUP_MASTER ,null,cv);
                        Snackbar.make(coordinateLayout, "Records has been saved", Snackbar.LENGTH_SHORT).show();





                    }




                    Dexter.withActivity(AddFollowupActivity.this)
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

                                        String title = edtDescr.getText().toString();

                                        ContentValues cvEvent = new ContentValues();
                                        cvEvent.put("calendar_id", calenderId);
                                        cvEvent.put(CalendarContract.Events.TITLE, title);

                                        cvEvent.put(CalendarContract.Events.DESCRIPTION, String.valueOf(list_client.get(spnClients.getSelectedItemPosition()))+"\n"+String.valueOf(edtDescr.getText().toString()));
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




                    edtDescr.setText("");
                    edtFollowupDate.setText("");
                    edtFollowupTime.setText("");
                    spnClients.setSelection(0);


                }

            }
        });


        try
        {
            Intent ii = getIntent();



            FOLLOWUPID =ii.getStringExtra(dbhandler.FOLLOWUP_ID) ;

            if ( FOLLOWUPID.length() > 0)
            {
                btnsave.setText("Update data");



                String companyname = ii.getStringExtra(dbhandler.CLIENT_COMPANYNAME);



                String descr = ii.getStringExtra(dbhandler.FOLLOWUP_DESCR);
                String followupdate = ii.getStringExtra(dbhandler.FOLLOWUP_DATE);
                String followuptime = ii.getStringExtra(dbhandler.FOLLOWUP_TIME);




                try {
                    String clientid = ii.getStringExtra(dbhandler.CLIENT_ID);
                    int setSelection = list_client_id.indexOf(clientid);
                    spnClients.setSelection(setSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error in selection", Toast.LENGTH_SHORT).show();
                }

                edtFollowupDate.setText(followupdate);
                edtFollowupTime.setText(followuptime);
                edtDescr.setText(descr);
                //Device Type
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private void setCurrentTime(int hour, int minute)
    {

        String MERIDIAN="";
        if(hour > 12)
        {
            hour -=12;
            MERIDIAN = "PM";

        }
        else if(hour == 0)
        {
            hour = 12;

            MERIDIAN="AM";

        }
        else if(hour == 12)
        {
            MERIDIAN = "PM";

        }
        else
        {
            MERIDIAN="AM";

        }


        String min;
        if(minute <=9)
        {

            min  = "0"+String.valueOf(minute);
        }
        else
        {
            min  =String.valueOf(minute);

        }

        edtFollowupTime.setText(hour+":"+min+" "+MERIDIAN);

    }


    protected Dialog onCreateDialog(int id)
    {
        switch (id){

            case  DATE_PICKER:
                return  new DatePickerDialog(context , datepickerlistener , year,month,day);
            case  TIME_PICKER:
                return  new TimePickerDialog(context,timepickerlistener, hour,minute,false
                );

        }
        return null;


    }
    private DatePickerDialog.OnDateSetListener datepickerlistener= new DatePickerDialog.OnDateSetListener()
    {


        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setCurrentDate(year,month,dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener timepickerlistener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            setCurrentTime(hourOfDay,minute);
        }
    };


    private void setCurrentDate(int year, int month, int day) {

        String mm="";
        ++month;
        if(month <= 9)
        {
            mm ="0"+String.valueOf(month);

        }
        else
        {
            mm =String.valueOf(month);
        }

        String  str_day = "";
        if(day <= 9 )
        {


            str_day = "0"+String.valueOf(day);
        }
        else
        {
            str_day = String.valueOf(day);
        }

        edtFollowupDate.setText(str_day+"-"+mm+"-"+year);

    }

    private void FillDataOnSpinner()
    {
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
        ArrayAdapter<String> adater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , list_client);
        spnClients.setAdapter(adater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

    }


}
