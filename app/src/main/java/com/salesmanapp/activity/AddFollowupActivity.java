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

import com.salesmanapp.R;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    private int FOLLOWUPID=0;


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


                if (isError == false) {

                    ContentValues cv = new ContentValues();
                    cv.put(dbhandler.CLIENT_ID, list_client_id.get(spnClients.getSelectedItemPosition()));
                    cv.put(dbhandler.FOLLOWUP_DATE, edtFollowupDate.getText().toString());
                    cv.put(dbhandler.FOLLOWUP_TIME, edtFollowupTime.getText().toString());
                    cv.put(dbhandler.FOLLOWUP_DESCR, edtDescr.getText().toString());
                    cv.put(dbhandler.EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));

                    if (btnsave.getText().toString().toLowerCase().equals("update data")) {


                        cv.put(dbhandler.CLIENT_DEVICE_TYPE, getIntent().getStringExtra(dbhandler.CLIENT_DEVICE_TYPE));
                        sd.update(dbhandler.TABLE_FOLLOWUP_MASTER ,cv, dbhandler.FOLLOWUP_ID +"="+ FOLLOWUPID +" and "+ dbhandler.CLIENT_ID +"='"+ getIntent().getStringExtra(dbhandler.CLIENT_ID) +"'",null);
                        Snackbar.make(coordinateLayout, "Records has been updated ", Snackbar.LENGTH_SHORT).show();



                    }
                    else
                    {

                        cv.put(dbhandler.CLIENT_DEVICE_TYPE, "and");
                        sd.insert(dbhandler.TABLE_FOLLOWUP_MASTER ,null,cv);
                        Snackbar.make(coordinateLayout, "Records has been saved", Snackbar.LENGTH_SHORT).show();





                    }


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



            FOLLOWUPID =ii.getIntExtra(dbhandler.FOLLOWUP_ID,0) ;

            if ( FOLLOWUPID > 0)
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

    private void setCurrentTime(int hour, int minute) {

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

        edtFollowupDate.setText(day+"-"+mm+"-"+year);

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
