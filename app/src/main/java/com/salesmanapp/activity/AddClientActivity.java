package com.salesmanapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;
import com.salesmanapp.R;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.helper.Camera;
import com.salesmanapp.helper.CustomRequest;
import com.salesmanapp.helper.ImageUtils;
import com.salesmanapp.helper.NetConnectivity;
import com.salesmanapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;

public class AddClientActivity extends AppCompatActivity {

    private static final int TIME_PICKER_ID = 122;
    private Context context = this;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private Button btnSave;
    private EditText edtName;
    private EditText edtMobile;
    private EditText edtEmail;
    private EditText edtAddress;
    private EditText edtBusiness;
    private EditText edtNote;
    private TextInputLayout edtNameWrapper;
    private TextInputLayout edtMobileWrapper;
    private TextInputLayout edtEmailWrapper;
    private TextInputLayout edtBusinessWrapper;
    private TextInputLayout edtAddressWrapper;
    private dbhandler db;
    private SQLiteDatabase sd;
    private String TAG = AddClientActivity.class.getSimpleName();
    private CoordinatorLayout coordinatelayout;
    private EditText edtCompanyname;
    private TextInputLayout edtCompanynameWrapper;
    private EditText edtFollowupDate;
    private EditText edtFollowupTime;
   // private int year, month, day;
    private String mm;
    private String startdate;
    private static final int DATE_PICKER_ID = 121;
    private int REQUEST_CAMERA = 100, SELECT_FILE = 121;
    private int hours, minutes;
    private EditText edtMobile2;
    private EditText edtLandline;
    private LinearLayout llFollwoup;
    private CheckBox chkFollowUp;
    private SpotsDialog pDialog;
    private TextView tvUploadVistingCardFrontSide;
    private TextView tvUploadVistingCardBackSide;
    private ImageView imgVisitingCrdFrontSide;
    private ImageView imgVisitingCrdBackSide;
    private Camera camera;
    private String userChoosenTask;
    private String DOCUMENT_TYPE = "";
    private Bitmap bitmap;
    private String BASE64STRING = "";
    private String IMAGE_NAME, IMAGE_URL;
    private String VISITING_CARD_FRONT = "", VISITING_CARD_BACK = "";
    private ImageView imgPicContact;
    private String MOBILENO,CONTACTNAME;
    private EditText edtWebsite;
    private TextInputLayout edtWebsiteWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.salesmanapp.R.layout.activity_add_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Client");

        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        sessionmanager = new SessionManager(context);
        userDetails = sessionmanager.getSessionDetails();

        try {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {
            Log.d("Error Actionbar", "" + e.getMessage());

        }

        coordinatelayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

        db = new dbhandler(context);
        sd = db.getReadableDatabase();
        sd = db.getWritableDatabase();


        edtNameWrapper = (TextInputLayout) findViewById(R.id.edtNameWrapper);
        edtCompanynameWrapper = (TextInputLayout) findViewById(R.id.edtCompanynameWrapper);
        edtMobileWrapper = (TextInputLayout) findViewById(R.id.edtMobileWrapper);
        edtEmailWrapper = (TextInputLayout) findViewById(R.id.edtEmailWrapper);
        edtBusinessWrapper = (TextInputLayout) findViewById(R.id.edtBussinessWrapper);
        edtAddressWrapper = (TextInputLayout) findViewById(R.id.edtAddressWrapper);
        edtWebsiteWrapper = (TextInputLayout)findViewById(R.id.edtWebsiteWrapper);


        edtName = (EditText) findViewById(R.id.edtName);
        edtCompanyname = (EditText) findViewById(R.id.edtCompanyname);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtMobile2 = (EditText) findViewById(R.id.edtMobile2);
        edtLandline = (EditText) findViewById(R.id.edtMobile3);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtBusiness = (EditText) findViewById(R.id.edtBussiness);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtNote = (EditText) findViewById(R.id.edtNote);
        edtFollowupDate = (EditText) findViewById(R.id.edtFollowupDate);
        edtFollowupTime = (EditText) findViewById(R.id.edtFollowupTime);
        edtWebsite = (EditText)findViewById(R.id.edtWebsite);
        imgPicContact = (ImageView)findViewById(R.id.imgPicContact);

        
        imgPicContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dexter.withActivity(AddClientActivity.this)
                        .withPermission(Manifest.permission.READ_CONTACTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                onClickSelectContact();

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                                onClickSelectContact();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                Toast.makeText(context, "Try again...    ", Toast.LENGTH_SHORT).show();
                            }
                        }).check();








            }
        });
        

        chkFollowUp = (CheckBox) findViewById(R.id.chkFollowUp);
        llFollwoup = (LinearLayout) findViewById(R.id.llFolloup);

        imgVisitingCrdFrontSide = (ImageView) findViewById(R.id.imgVisitingCrdFrontSide);
        imgVisitingCrdBackSide = (ImageView) findViewById(R.id.imgVisitingCrdBackSide);

        tvUploadVistingCardFrontSide = (TextView) findViewById(R.id.tvUploadVistingCardFrontSide);
        tvUploadVistingCardBackSide = (TextView) findViewById(R.id.tvUploadVistingCardBackSide);

        imgVisitingCrdFrontSide.setVisibility(View.GONE);
        imgVisitingCrdBackSide.setVisibility(View.GONE);

        //Set current date
        Calendar c = Calendar.getInstance();
       int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        ++month;

        if (month <= 9) {
            mm = "0" + month;


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
            str_day = String.valueOf(day);
        }

        startdate = str_day + "-" + mm + "-" + year;
        edtFollowupDate.setText(startdate);


        //Set Current Time
        hours = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        setCurrentTime(hours, minutes);

        edtFollowupTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDialog(TIME_PICKER_ID);

                }
                return false;
            }
        });


        edtFollowupDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDialog(DATE_PICKER_ID);


                }
                return false;
            }
        });


        btnSave = (Button) findViewById(R.id.btnsave);


        try
        {
            Intent ii = getIntent();


            if (ii.getStringExtra(dbhandler.CLIENT_MOBILE1).toString().length() == 10)
            {
                btnSave.setText("Update data");
                String name = ii.getStringExtra(dbhandler.CLIENT_NAME);
                String mobile = ii.getStringExtra(dbhandler.CLIENT_MOBILE1);
                String email = ii.getStringExtra(dbhandler.CLIENT_EMAIL);
                String bussiness = ii.getStringExtra(dbhandler.CLIENT_BUSSINESS);
                String address = ii.getStringExtra(dbhandler.CLIENT_ADDRESS);
                String note = ii.getStringExtra(dbhandler.CLIENT_NOTE);
                String companyname = ii.getStringExtra(dbhandler.CLIENT_COMPANYNAME);
                String mobile2 = ii.getStringExtra(dbhandler.CLIENT_MOBILE2);
                String landline = ii.getStringExtra(dbhandler.CLIENT_LANDLINE);
                String followupdate = ii.getStringExtra(dbhandler.FOLLOWUP_DATE);
                String followuptime = ii.getStringExtra(dbhandler.FOLLOWUP_TIME);
                String website = ii.getStringExtra(dbhandler.CLIENT_WEBSITE);






                /*edtFollowupDate.setVisibility(View.GONE);
                edtFollowupTime.setVisibility(View.GONE);
                */
                llFollwoup.setVisibility(View.GONE);
                chkFollowUp.setVisibility(View.GONE);


                edtAddress.setText(address);
                edtNote.setText(note);
                edtBusiness.setText(bussiness);
                edtMobile.setText(bussiness);
                edtName.setText(name);
                edtCompanyname.setText(companyname);
                edtMobile.setText(mobile);
                edtMobile2.setText(mobile2);
                edtLandline.setText(landline);
                edtEmail.setText(email);
                edtFollowupDate.setText(followupdate);
                edtFollowupTime.setText(followuptime);
                edtWebsite.setText(website);
                //Device Type
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Build the camera
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(Camera.REQUEST_TAKE_PHOTO)
                /*.setDirectory("BETA")*/
                .setName("Executive App" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(50)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);


        tvUploadVistingCardFrontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DOCUMENT_TYPE = "front";

                selectImage();


            }
        });

        tvUploadVistingCardBackSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DOCUMENT_TYPE = "back";

                selectImage();


            }
        });

        chkFollowUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    llFollwoup.setVisibility(View.VISIBLE);
                } else {
                    llFollwoup.setVisibility(View.GONE);

                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;

                if (edtName.getText().toString().equals("")) {
                    isError = true;
                    edtNameWrapper.setErrorEnabled(true);
                    edtNameWrapper.setError("Enter name");
                } else {
                    edtNameWrapper.setErrorEnabled(false);
                }


                if (edtCompanyname.getText().toString().equals("")) {
                    isError = true;
                    edtCompanynameWrapper.setErrorEnabled(true);
                    edtCompanynameWrapper.setError("Enter company name");
                } else {
                    edtCompanynameWrapper.setErrorEnabled(false);
                }





                if (edtMobile.getText().toString().equals("")) {
                    isError = true;
                    edtMobileWrapper.setErrorEnabled(true);
                    edtMobileWrapper.setError("Enter mobile");
                } else {
                    if (edtMobile.getText().toString().length() != 10) {
                        isError = true;

                        edtMobileWrapper.setError("Invalid mobile");
                        edtMobileWrapper.setErrorEnabled(true);
                    } else {
                        edtMobileWrapper.setErrorEnabled(false);
                    }

                }

              /*  if (edtEmail.getText().toString().equals("")) {
                    isError = true;
                    edtEmailWrapper.setErrorEnabled(true);
                    edtEmailWrapper.setError("Enter Email");
                } else {
                    if (AllKeys.checkEmail(edtEmail.getText().toString())) {
                        edtEmailWrapper.setErrorEnabled(false);
                    } else {
                        isError = true;
                        edtEmailWrapper.setErrorEnabled(true);
                        edtEmailWrapper.setError("Invalid Email");

                    }


                }*/


                if (isError == false) {

                    //Ask permission and write contact to mobile no

                    Dexter.withActivity(AddClientActivity.this)
                            .withPermission(Manifest.permission.WRITE_CONTACTS)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {


                                    saveData();

                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                    saveData();

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    Toast.makeText(context, "Try again...    ", Toast.LENGTH_SHORT).show();
                                }
                            }).check();


                }


            }
        });


    }

    private void setCurrentTime(int hourOfDay, int minute) {

        String MERIDIAN = "";
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            MERIDIAN = "PM";

        } else if (hourOfDay == 12) {

            MERIDIAN = "PM";


        } else if (hourOfDay == 0) {


            hourOfDay += 12;
            MERIDIAN = "AM";

        } else {
            MERIDIAN = "AM";
        }

        String minute_str = "";
        if (minute <= 9) {

            minute_str = "0" + minute;
        } else {
            minute_str = String.valueOf(minute);

        }
        edtFollowupTime.setText(hourOfDay + ":" + minute_str + " " + MERIDIAN);

    }


    private void saveData()
    {


        try {
            ContentValues cv = new ContentValues();


            cv.put(dbhandler.CLIENT_NAME, edtName.getText().toString());
            cv.put(dbhandler.CLIENT_COMPANYNAME, edtCompanyname.getText().toString());
            cv.put(dbhandler.CLIENT_MOBILE1, edtMobile.getText().toString());
            cv.put(dbhandler.CLIENT_MOBILE2, edtMobile2.getText().toString());
            cv.put(dbhandler.CLIENT_LANDLINE, edtLandline.getText().toString());
            cv.put(dbhandler.CLIENT_EMAIL, edtEmail.getText().toString());
            cv.put(dbhandler.CLIENT_BUSSINESS, edtBusiness.getText().toString());
            cv.put(dbhandler.CLIENT_ADDRESS, edtAddress.getText().toString());

            cv.put(dbhandler.CLIENT_WEBSITE, edtWebsite.getText().toString());


            cv.put(dbhandler.CLIENT_NOTE, edtNote.getText().toString());
           // cv.put(dbhandler.SYNC_STATUS, "0");
            cv.put(dbhandler.CLIENT_DEVICE_TYPE, "and");







            userDetails = sessionmanager.getSessionDetails();
            cv.put(dbhandler.CLIENT_LATTITUDE, userDetails.get(SessionManager.KEY_LATTITUDE));
            cv.put(dbhandler.CLIENT_LONGTITUDE, userDetails.get(SessionManager.KEY_LONGTITUDE));

            cv.put(dbhandler.EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));


            if (btnSave.getText().toString().toLowerCase().equals("update data")) {


                cv.put(dbhandler.SYNC_STATUS, "0");

                sd.update(dbhandler.TABLE_CLIENTMASTER, cv, "" + dbhandler.CLIENT_ID + "='" + getIntent().getStringExtra(dbhandler.CLIENT_ID) + "' and " + dbhandler.CLIENT_DEVICE_TYPE + "='" + getIntent().getStringExtra(dbhandler.CLIENT_DEVICE_TYPE) + "'", null);

                Snackbar.make(coordinatelayout, "Client details has been updated successfully", Snackbar.LENGTH_SHORT).show();

            } else {



                String time = dbhandler.getDateTime();
                Log.d("Data & Time : ", time);
                String CurrentDate = time.substring(0, 10);

                cv.put(dbhandler.VISIT_DATE, CurrentDate);

                Cursor cur_max_clientid = sd.rawQuery("SELECT * FROM " + dbhandler.TABLE_CLIENTMASTER+" where "+ dbhandler.CLIENT_ID +" like '%and"+ userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE) +"%'", null);

                //cur_max_clientid.moveToFirst();
                int max_clientid = cur_max_clientid.getCount();
                ++max_clientid;
                Log.d("Max Id By Goal : ", "" + max_clientid);


                cv.put(dbhandler.CLIENT_ID, "ANDCLIENT"+userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE)+max_clientid);
                cv.put(dbhandler.CLIENT_TYPE, "lead");

                cv.put(dbhandler.CLIENT_VISITING_CARD_FRONT,VISITING_CARD_FRONT);
                cv.put(dbhandler.CLIENT_VISITING_CARD_BACK,VISITING_CARD_BACK);

                cv.put(dbhandler.SYNC_STATUS,"0");
                Log.d(TAG, "Client Data : " + cv.toString());

                sd.insert(dbhandler.TABLE_CLIENTMASTER, null, cv);


                if (chkFollowUp.isChecked() == true)
                {

                    ContentValues cv_fallow = new ContentValues();
                    cv_fallow.put(dbhandler.FOLLOWUP_DESCR, edtNote.getText().toString());
                    cv_fallow.put(dbhandler.FOLLOWUP_DATE, edtFollowupDate.getText().toString());
                    cv_fallow.put(dbhandler.FOLLOWUP_TIME, edtFollowupTime.getText().toString());
                    cv_fallow.put(dbhandler.CLIENT_ID,"and"+userDetails.get(SessionManager.KEY_EMP_UNIQUE_CODE)+max_clientid );
                    cv_fallow.put(dbhandler.EMPLOYEE_ID, userDetails.get(SessionManager.KEY_EMP_ID));
                    cv_fallow.put(dbhandler.CLIENT_DEVICE_TYPE, "and");
                    cv_fallow.put(dbhandler.FOLLOWUP_STATUS, AllKeys.DEAFULT);
                    cv_fallow.put(dbhandler.FOLLOWUP_REASON, "");
                    cv_fallow.put(dbhandler.SYNC_STATUS, "0");

                    sd.insert(dbhandler.TABLE_FOLLOWUP_MASTER, null, cv_fallow);
                    cv_fallow.clear();



                    ///Insert Data into Google Calndar

                    Dexter.withActivity(AddClientActivity.this)
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

                                        String title = edtNote.getText().toString();

                                        ContentValues cvEvent = new ContentValues();
                                        cvEvent.put("calendar_id", calenderId);
                                        cvEvent.put(CalendarContract.Events.TITLE, title);

                                        cvEvent.put(CalendarContract.Events.DESCRIPTION, String.valueOf(edtNote.getText().toString()));
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

                                        values.put(CalendarContract.Reminders.MINUTES, 5);
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
                    //Complete Insert data in google calendar

                    Snackbar.make(coordinatelayout, "Client details has been stored", Snackbar.LENGTH_SHORT).show();
                }


                Snackbar.make(coordinatelayout, "Client details has been saved successfully", Snackbar.LENGTH_SHORT).show();


            }
            WritePhoneContact(edtName.getText().toString() + " SFC", "+91" + edtMobile.getText().toString(), context);


            edtName.setText("");
            edtEmail.setText("");
            edtBusiness.setText("");
            edtMobile.setText("");
            edtAddress.setText("");
            edtNote.setText("");
            edtMobile.setText("");
            edtMobile2.setText("");
            edtLandline.setText("");
            edtCompanyname.setText("");
            edtFollowupDate.setText("");
            edtFollowupTime.setText("");

            VISITING_CARD_BACK="";
            VISITING_CARD_FRONT="";
            imgVisitingCrdBackSide.setVisibility(View.GONE);
            imgVisitingCrdFrontSide.setVisibility(View.GONE);
            chkFollowUp.setChecked(false);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error in inserting Data :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error inserting data :" + e.getMessage());
            //Error inserting data :SugarContext has not been initialized properly. Call SugarContext.init(Context) in your Application.onCreate() method and SugarContext.terminate() in your Application.onTerminate() method.
        }

    }

    public Uri getImageUri(Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String dd = cursor.getString(column_index);
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "sa";

    }

    public void onClickSelectContact() {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was
        // selected.
        try {

            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Camera.REQUEST_TAKE_PHOTO)
        {
            bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                // imgPreview.setImageBitmap(bitmap);
                Uri tempUri = getImageUri(bitmap);


                String realPath = null;
                try {
                    Log.d("C: Realpath URI : ", "" + tempUri.toString());
                    realPath = getRealPathFromURI(tempUri);
                    Log.d("C: Realpath : ", realPath);
                    showDialog();
                    BASE64STRING = ImageBase64
                            .with(getApplicationContext())
                            .requestSize(1024, 1024)
                            .encodeFile(realPath);
                    hideDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (NetConnectivity.isOnline(context)) {

                    // new SendUserProfilePictureToServer().execute();


                    SendBase64ImageToServer();
                } else {

                    //   checkInternet();
                    Toast.makeText(context, "Please enable internet", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == SELECT_FILE){

            onSelectFromGalleryResult(data);

        }
        else
        {
            try {



                        System.out.println("in on ActivityResult");
                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null,
                                null);
                        if (c.moveToFirst()) {
                            String id = c
                                    .getString(c
                                            .getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            String hasPhone = c
                                    .getString(c
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver()
                                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = " + id, null,
                                                null);
                                phones.moveToFirst();
                                MOBILENO = phones.getString(phones
                                        .getColumnIndex("data1"));
                                CONTACTNAME = phones
                                        .getString(phones
                                                .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));


                                try {
                                    MOBILENO = MOBILENO.replace(" ","");
                                    MOBILENO = MOBILENO.substring(MOBILENO.length()-10,MOBILENO.length());
                                    edtName.setText(CONTACTNAME);
                                    edtMobile.setText(MOBILENO);




                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // here you can find out all the thing.
                                System.out.println("NAME:" + CONTACTNAME);

                                //MOBILENO = cNumber;
                               //Toast.makeText(context, "Length  : "+MOBILENO.length(),Toast.LENGTH_SHORT).show();



                            }
                        }






            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        showDialog();

        if (data != null) {
            try {


                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("Before Bitmap Size : ", "" + bitmapByteCount);

                Uri tempUri = getImageUri(bitmap);

                String realPath = null;
                try {
                    Log.d("CC: Realpath URI : ", "" + tempUri.toString());
                    realPath = getRealPathFromURI(tempUri);
                    BASE64STRING = ImageBase64
                            .with(getApplicationContext())
                            .requestSize(512, 512)
                            .encodeFile(realPath);
                    Log.d("CC: Realpath : ", realPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                bitmap = ImageUtils.getInstant().getCompressedBitmap(realPath);
                //imageView.setImageBitmap(bitmap);

                bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("After Bitmap Size : ", "" + bitmapByteCount);


                // getStringImage(bm);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {
            if (NetConnectivity.isOnline(context)) {

                //new SendUserProfilePictureToServer().execute();

                //SendClotheImageToServer();
                SendBase64ImageToServer();


            } else {
//                checkInternet();
                Toast.makeText(context, "Please enable internet", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Profile Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {

                    userChoosenTask = "Take Photo";

                    Dexter.withActivity(AddClientActivity.this)
                            .withPermission(Manifest.permission.CAMERA)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    try {
                                        camera.takePicture();

//                        cameraIntent();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                            }).check();


                } else if (items[item].equals("Choose from Library")) {

                    userChoosenTask = "Choose from Library";
                    Dexter.withActivity(AddClientActivity.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {

                                    try {


                                        galleryIntent();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                            }).check();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(context, pickerlistener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            case TIME_PICKER_ID:
                Calendar cal = Calendar.getInstance();
                return new TimePickerDialog(context, timepicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);

        }
        return null;

    }


    private void SendBase64ImageToServer() {
        showDialog();
        String url = AllKeys.WEBSITE + "InsertVisitingCardImage";
        Log.d(TAG, "URL " + url);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d(TAG, "Response   InsertVisitingCardImage : " + response.toString());

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    IMAGE_NAME = "";
                    IMAGE_URL = "";
                    if (error_status == false) {
                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.TAG_UPLOAD_IMAGES_ARRAY);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);


                                IMAGE_NAME = c.getString(AllKeys.TAG_UPLOAD_IMAGEURL);
                                IMAGE_URL = c.getString(AllKeys.TAG_UPLOAD_IMAGEURL);

                                if (DOCUMENT_TYPE.equals("front")) {
                                    VISITING_CARD_FRONT = IMAGE_URL;

                                    try {
                                        //Glide.with(context).load(IMAGE_URL).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade(R.anim.slide_left, 2000).into(imgVisitingCrdFrontSide);
                                        imgVisitingCrdFrontSide.setImageBitmap(bitmap);
                                        imgVisitingCrdFrontSide.setVisibility(View.VISIBLE);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    VISITING_CARD_BACK = IMAGE_URL;


                                    try {
                                        //Glide.with(context).load(IMAGE_URL).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade(R.anim.slide_left, 2000).into(imgVisitingCrdBackSide);
                                        imgVisitingCrdBackSide.setImageBitmap(bitmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    imgVisitingCrdBackSide.setVisibility(View.VISIBLE);


                                }


                            }
                            //Set Images Details To Adapter and close dialog


                            hideDialog();
                        } else {
                            hideDialog();
                            Toast.makeText(context, "Trg again...", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hideDialog();
                        Toast.makeText(context, "" + str_error, Toast.LENGTH_SHORT).show();
                        //   Snackbar.make(coordinatorLayout, str_error, Snackbar.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    hideDialog();

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error InsertVisitingCardImage : " + error.getMessage());

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {
                    SendBase64ImageToServer();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();


                params.put("visitingcardimg", BASE64STRING);
                params.put("Type", "front");

                Log.d(TAG, "InsertVisitingCardImage Sending PArams : " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(request);
    }

    private DatePickerDialog.OnDateSetListener pickerlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            ++month;
            if (month <= 9) {
                mm = "0" + month;
            } else {

                mm = "" + month;

            }
            String str_day ="";
            if(dayOfMonth <=9)

            {

                str_day ="0"+String.valueOf(dayOfMonth);
            }
            else
            {
                str_day =String.valueOf(dayOfMonth);

            }

            startdate = str_day + "-" + mm + "-" + year;


            edtFollowupDate.setText(startdate);

        }
    };
    private TimePickerDialog.OnTimeSetListener timepicker = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            setCurrentTime(hourOfDay, minute);


        }
    };

    public void WritePhoneContact(String displayName, String number, Context cntx) {
        try {
            Context contetx = cntx; //Application's context or Activity's context
            String strDisplayName = displayName; // Name of the Person to add
            String strNumber = number; //number of the person to add with the Contact

            ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
            int contactIndex = cntProOper.size();//ContactSize

            //Newly Inserted contact
            // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
            cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            //Display name will be inserted in ContactsContract.Data table
            cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                    .build());
            //Mobile number will be inserted in ContactsContract.Data table
            cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added

                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
            try {
                // We will do batch operation to insert all above data
                //Contains the output of the app of a ContentProviderOperation.
                //It is sure to have exactly one of uri or count set
                ContentProviderResult[] contentProresult = null;
                contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
            } catch (RemoteException exp) {
                //logs;
            } catch (OperationApplicationException exp) {
                //logs
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common, menu);

        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

        }
        return super.onOptionsItemSelected(item);
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.cancel();
            pDialog.dismiss();


        }
    }

    private void showDialog() {
        if (pDialog.isShowing()) {
            pDialog.show();

        }
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
