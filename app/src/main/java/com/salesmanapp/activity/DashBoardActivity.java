package com.salesmanapp.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.CalendarDemoActivity;
import com.salesmanapp.MyFirebaseInstanceIDService;
import com.salesmanapp.R;
import com.salesmanapp.adapter.FollowupDataAdapterRecyclerView;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.fragments.FragmentClients;
import com.salesmanapp.fragments.FragmentFollowup;
import com.salesmanapp.fragments.FragmentHome;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.helper.CustomRequest;
import com.salesmanapp.helper.MyBroadcastReceiver;
import com.salesmanapp.pojo.FollowupData;
import com.salesmanapp.services.MyLocationService;
import com.salesmanapp.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private Context context = this;
    private SpotsDialog pDialog;
    private ActionBarDrawerToggle toggle;
    private TextView txtname;
    private TextView txtemail;
    private ImageView imgProfilePic;
    private Menu menu;
    private MenuItem action_checkin;
    private String TAG = DashBoardActivity.class.getSimpleName();
    private boolean CHECKIN_FLAG = false;
    private MenuItem action_sync;
    private ImageView ivSyncAnimation;
    private dbhandler db;
    private SQLiteDatabase sd;
    private boolean doubleBackToExitPressedOnce;

    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(context, AddClientActivity.class);
                startActivity(intent);
                finish();


            }
        });


        db = new dbhandler(context);
        sd = db.getWritableDatabase();
        sd = db.getReadableDatabase();

        //sd.delete(dbhandler.TABLE_FOLLOWUP_MASTER , null,null);


        sessionmanager = new SessionManager(getApplicationContext());
        userDetails = sessionmanager.getSessionDetails();

        pDialog = new SpotsDialog(context);


        if (userDetails.get(SessionManager.KEY_EMP_ID).equals("0")) {

            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (userDetails.get(SessionManager.KEY_VERSTATUS).equals("0") && !userDetails.get(SessionManager.KEY_EMP_ID).equals("0")) {
            Intent intent = new Intent(context, VerificationActivity.class);
            startActivity(intent);
            finish();
        } else {
            /**
             * Call required API's
             */
            //getAllCartItemDetailsFromServer();
            //UpdateFcmTokenDetailsToServer();
            //setAddToCartBadget();


            // Refer to the Java quickstart on how to setup the environment:
// https://developers.google.com/google-apps/calendar/quickstart/java
// Change the scope to CalendarScopes.CALENDAR and delete any stored
// credentials.

            try {
                Event event = new Event()
                        .setSummary("Google I/O 2015")
                        .setLocation("800 Howard St., San Francisco, CA 94103")
                        .setDescription("A chance to hear more about Google's developer products.");

                DateTime startDateTime = new DateTime("2017-07-05T09:00:00-07:00");
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setStart(start);

                DateTime endDateTime = new DateTime("2017-07-03T17:00:00-07:00");
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setEnd(end);

                String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
                event.setRecurrence(Arrays.asList(recurrence));

                EventAttendee[] attendees = new EventAttendee[]{
                        new EventAttendee().setEmail("zealtech9teen@gmail.com"),
                        new EventAttendee().setEmail("sathishtech9teen@gmail.com"),
                };
                event.setAttendees(Arrays.asList(attendees));

                EventReminder[] reminderOverrides = new EventReminder[]{
                        new EventReminder().setMethod("email").setMinutes(24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(10),
                };
                Event.Reminders reminders = new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminderOverrides));
                event.setReminders(reminders);

                String calendarId = "primary";

                // Initialize credentials and service object.
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff());
                com.google.api.client.http.HttpTransport transport = AndroidHttp.newCompatibleTransport();
                com.google.api.client.json.JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
                        transport, jsonFactory, credential)
                        .setApplicationName("Google Calendar API Android Quickstart")
                        .build();

                try {
                   // event = mService.events().insert(calendarId, event).execute();
                    System.out.printf("Event created: %s\n", event.getHtmlLink());
                } catch (Exception e) {
                    e.printStackTrace();
                }

              /*  // Initialize Calendar service with valid OAuth credentials
                Calendar service = new Calendar.Builder(transport, jsonFactory, credential)
                        .setApplicationName("applicationName").build();

// Create a new calendar
                com.google.api.services.calendar.model.Calendar calendar = new Calendar();
                calendar.setSummary("calendarSummary");
                calendar.setTimeZone("America/Los_Angeles");

// Insert the new calendar
                Calendar createdCalendar = service.calendars().insert(calendar).execute();

                System.out.println(createdCalendar.getId());*/


                Dexter.withActivity(DashBoardActivity.this)
                        .withPermission(Manifest.permission.WRITE_CALENDAR)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                try {
                                    long start2 =Calendar.getInstance().getTimeInMillis(); // 2011-02-12 12h00
                                    long end2 = Calendar.getInstance().getTimeInMillis()+(2*60*60);   // 2011-02-12 13h00

                                    String title = "TEST ENTRY - DELETE ME!!";

                                    ContentValues cvEvent = new ContentValues();
                                    cvEvent.put("calendar_id", 1);
                                    cvEvent.put("title", title);
                                    cvEvent.put("dtstart", start2 );
                                    //cvEvent.put("hasAlarm", 1);
                                    cvEvent.put("dtend", end2);
                                    cvEvent.put("","");


                                    getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), cvEvent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();




                /////////////////////////Open intent od calendat event /////////////////////

              /*  Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, "Learn Android");
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Home suit home");
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Download Examples");

// Setting dates
                GregorianCalendar calDate = new GregorianCalendar(2017, 07, 05);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis()+(1*60*60));
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis()+(1*60*60));

// make it a full day event
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

// make it a recurring Event
               // intent.putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

// Making it private and shown as busy
                intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
                intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);



                intent.setData(CalendarContract.Events.CONTENT_URI);
                startActivity(intent);*/

            } catch (Exception e) {
                e.printStackTrace();
            }


            Dexter.withActivity(DashBoardActivity.this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            backupDB();


                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();


            setupLocalNotifications();
            UpdateFcmTokenDetailsToServer();

        }


       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //app:headerLayout="@layout/nav_header_menu"
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_dashboard);

        try {


            txtname = (TextView) headerLayout.findViewById(R.id.txtname);
            txtemail = (TextView) headerLayout.findViewById(R.id.txtemail);
            imgProfilePic = (ImageView) headerLayout.findViewById(R.id.imgProfilePic);

            SetUserProfilePictireFromBase64EnodedString();


            imgProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getMenuInflater().inflate(R.menu.activity_dashboard_drawer, menu);
                    MenuItem mProfileFrag = menu.findItem(R.id.nav_profile);

                    onNavigationItemSelected(mProfileFrag);


                    /*MenuItem mDefaultFrag = (MenuItem) navigationView.findViewById(R.id.nav_profile);
                    onNavigationItemSelected(mDefaultFrag);*/


                }
            });


            txtemail.setText("" + userDetails.get(SessionManager.KEY_EMP_EMAIL));
            txtname.setText("" + userDetails.get(SessionManager.KEY_EMP_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }


        setupFragment(new FragmentHome(), getString(R.string.app_name));

    }

    private void UpdateFcmTokenDetailsToServer() {
        showDialog();


        //String url = AllKeys.WEBSITE + "updateSellerFCM?device_type=android&sellerid=" + userDetails.get(SessionManager.KEY_SELLER_ID) + "&fcm=" + fcm_tokenid + "";
        String url = AllKeys.WEBSITE + "InsertFCMToken";

        Log.d(TAG, "URL  InsertFCMToken : " + url);


        CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response InsertFCMToken : " + response.toString());
                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    UpdateFcmTokenDetailsToServer();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                String fcm_tokenid = "";
                try {
                    MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();
                    fcm_tokenid = mid.onTokenRefreshNew(context);

                } catch (Exception e) {
                    fcm_tokenid = "";
                    e.printStackTrace();
                }


                params.put("type", "fcmtoken");
                params.put("userid", userDetails.get(SessionManager.KEY_EMP_ID));
                params.put("fcmtoken", fcm_tokenid);

                Log.d(TAG, "Update FCM Params :" + params.toString());


                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);


    }

    private void setupLocalNotifications() {


        String query = "select *,fm." + dbhandler.CLIENT_DEVICE_TYPE + " as DevicType  from " + dbhandler.TABLE_FOLLOWUP_MASTER + " as fm," + dbhandler.TABLE_CLIENTMASTER + "  as cm where cm." + dbhandler.CLIENT_ID + " =fm." + dbhandler.CLIENT_ID;
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {


                String notifytime = c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME));
                Log.d(TAG, "Time Before: " + notifytime);
                notifytime = notifytime.replace(" ", ":");
                Log.d(TAG, "Time After: " + notifytime);

                List<String> myTimeList = new ArrayList<String>(Arrays.asList(notifytime.split(":")));
                Log.d(TAG, "Time In List " + myTimeList.toString());

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.MINUTE, Integer.parseInt(myTimeList.get(1)));
                Log.d(TAG, "Alaram Minute :" + Integer.parseInt(myTimeList.get(1)));
                //   calendar.set(Calendar.SECOND, 00);
                if (myTimeList.get(2).toLowerCase().equals("am")) {

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

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                //Log.d(TAG , "Alarm set in " + calendar.getTimeInMillis() + " seconds, Alaram ID : "+(c.getInt(c.getColumnIndex(dbhandler.SUBTASK_ID))));

                Intent myIntent = new Intent(context, MyBroadcastReceiver.class);
                //  myIntent = new Intent(getActivity() , NotificationPublisher.class);

                Log.d(TAG, "Notification Data : " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)) + "," + c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)));
                myIntent.putExtra("ALARAMID", String.valueOf(c.getInt(c.getColumnIndex(dbhandler.FOLLOWUP_ID))));

                long timeinMilliSeconds = calendar.getTimeInMillis() - (15 * 60 * 1000);
                Log.d(TAG, "Final Time in MilliSeconds  Before : " + calendar.getTimeInMillis());
                Log.d(TAG, "Final Time in MilliSeconds After : " + timeinMilliSeconds);
                myIntent.putExtra("SATISH", c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)) + ",Meeting Regards ,  " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)) + "," + timeinMilliSeconds);
                myIntent.putExtra("TITLE", c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)));
                myIntent.putExtra("MESSAGE", "Meeting Regards ,  " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)));
                myIntent.putExtra("NOTIFICATIONID", c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, c.getInt(c.getColumnIndex(dbhandler.FOLLOWUP_ID)), myIntent, 0);

                //Log.d(TAG , " Time in Milli Seconds  "+AlarmManager.INTERVAL_DAY );

                //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  //set repeating every 24 hours
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                                    /*long futureInMillis = SystemClock.elapsedRealtime() + calendar.getTimeInMillis();
                                    Log.d(TAG, " Future MilliSeconds : " + futureInMillis);*/

                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  //set repeating every 24 hours

                if (System.currentTimeMillis() <= timeinMilliSeconds) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeinMilliSeconds, AlarmManager.INTERVAL_DAY, pendingIntent);  //set repeating every 24 hours
                    Log.d(TAG, "Notification hasbeen set : ");
                    Log.d(TAG, "Notification has been set : " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)) + "," + c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)));

                }



                //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,pendingIntent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);  //set repeating every 24 hours

                Log.d(TAG, "Alarm set in " + calendar.getTimeInMillis() + " seconds");

                calendar.clear();


            }


        }


    }


    private void sendOrderDetailsToServer() {
        try {


            showDialog();

            final String url = AllKeys.WEBSITE + "InsertService";
            Log.d(TAG, "URL InsertOrderService : " + url);


            /*StringRequest req_goaldata_send = new StringRequest(StringRequest.Method.POST,
                    str_goalDetails, new Response.Listener<String>() {*/
            CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Log.d(TAG, "InsertOrderService Response : " + response.toString());


                    try {
                        String str_error = response.getString(AllKeys.TAG_MESSAGE);
                        String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                        boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                        boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                        if (error_status == false) {
                            Toast.makeText(context, "InsertOrderService details has been sync successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Sorry,try again...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (pDialog.isShowing()) {
                        hideDialog();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(TAG, "InsertClientFollowup  Error : " + error.getMessage());
                    if (pDialog.isShowing()) {
                        hideDialog();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    String sq = "select * from " + dbhandler.TABLE_ORDER_MASTER;


                    Cursor cur = sd.rawQuery(sq, null);


                    params.put("type", "insertservice");
                    params.put("RecordType", "multiple");

                    params.put("empid", userDetails.get(SessionManager.KEY_EMP_ID));

                    String json = "";
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {


                            try {
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.accumulate("orderid", cur.getString(cur.getColumnIndex(dbhandler.ORDER_ID)));
                                jsonObject.accumulate("serviceid", cur.getString(cur.getColumnIndex(dbhandler.SERVICE_ID)));
                                jsonObject.accumulate("qty", cur.getString(cur.getColumnIndex(dbhandler.ORDER_QUANTITY)));
                                jsonObject.accumulate("rate", cur.getString(cur.getColumnIndex(dbhandler.ORDER_RATE)));
                                jsonObject.accumulate("discountamt", cur.getString(cur.getColumnIndex(dbhandler.ORDER_DISCOUNT_AMOUNT)));
                                jsonObject.accumulate("netamt", cur.getString(cur.getColumnIndex(dbhandler.ORDER_NET_AMOUNT)));
                                jsonObject.accumulate("clientid", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ID)));
                                jsonObject.accumulate("devicetype", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)));
                                jsonObject.accumulate("orderdate", cur.getString(cur.getColumnIndex(dbhandler.ORDER_DATE)));


                                json = json + jsonObject.toString() + ",";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        json = json.substring(0, json.length() - 1);
                        json = "[" + json + "]";
                        Log.d(TAG, "InsertOrderService Data : " + json);
                        Log.d("Json Data : ", url + "?type=InsertOrderService&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "&Data=" + json);
                        params.put("Data", json);


                    }


                    return params;
                }

            };

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(request);


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        //Complete Sending GoalMst DEtails to server
    }


    private void sendFollowupDetailsToServer() {
        try {


            showDialog();

            final String url = AllKeys.WEBSITE + "InsertClientFollowup";


            /*StringRequest req_goaldata_send = new StringRequest(StringRequest.Method.POST,
                    str_goalDetails, new Response.Listener<String>() {*/
            CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Log.d(TAG, "InsertClientFollowup Res : " + response.toString());


                    try {
                        String str_error = response.getString(AllKeys.TAG_MESSAGE);
                        String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                        boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                        boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                        if (error_status == false) {
                            Toast.makeText(context, "Client details has been sync successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Sorry,try again...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (pDialog.isShowing()) {
                        hideDialog();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(TAG, "InsertClientFollowup  Error : " + error.getMessage());
                    if (pDialog.isShowing()) {
                        hideDialog();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    String sq = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER;


                    Cursor cur = sd.rawQuery(sq, null);


                    params.put("type", "insertfollowup");
                    params.put("RecordType", "multiple");

                    params.put("empid", userDetails.get(SessionManager.KEY_EMP_ID));

                    String json = "";
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {


                            try {
                                JSONObject jsonObject = new JSONObject();

                                jsonObject.accumulate("clientid", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ID)));
                                jsonObject.accumulate("devicetype", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE))));
                                jsonObject.accumulate("description", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_DESCR))));
                                jsonObject.accumulate("datetime", dbhandler.convertToJsonDateFormat(cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_DATE))) + " " + cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_TIME)));
                                jsonObject.accumulate("status", cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_STATUS)));
                                jsonObject.accumulate("reason", cur.getString(cur.getColumnIndex(dbhandler.FOLLOWUP_REASON)));


                                json = json + jsonObject.toString() + ",";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        json = json.substring(0, json.length() - 1);
                        json = "[" + json + "]";
                        Log.d(TAG, "InsertClientFollowup Data : " + json);
                        Log.d("Json Data : ", url + "?type=insertfollowup&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "&Data=" + json);
                        params.put("Data", json);


                    }


                    return params;
                }

            };

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(request);


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        //Complete Sending GoalMst DEtails to server
    }


    private void sendAllClientDetailsToServer() {
        try {


            showDialog();

            final String url = AllKeys.WEBSITE + "InsertClient";


            /*StringRequest req_goaldata_send = new StringRequest(StringRequest.Method.POST,
                    str_goalDetails, new Response.Listener<String>() {*/
            CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Log.d(TAG, "InsertClient Res : " + response.toString());


                    try {
                        String str_error = response.getString(AllKeys.TAG_MESSAGE);
                        String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                        boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                        boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                        if (error_status == false) {
                            Toast.makeText(context, "Client details has been sync successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Sorry,try again...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (pDialog.isShowing()) {
                        hideDialog();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(TAG, "InsertClient  Error : " + error.getMessage());
                    if (pDialog.isShowing()) {
                        hideDialog();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    String sq = "select * from " + dbhandler.TABLE_CLIENTMASTER;


                    Cursor cur = sd.rawQuery(sq, null);


                    params.put("type", "insertclient");
                    params.put("RecordType", "multiple");

                    params.put("empid", userDetails.get(SessionManager.KEY_EMP_ID));

                    String json = "";
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {


                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("clientid", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ID)));
                                jsonObject.accumulate("companyname", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_COMPANYNAME))));
                                jsonObject.accumulate("devicetype", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE))));
                                jsonObject.accumulate("mobile1", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_MOBILE1))));
                                jsonObject.accumulate("mobile2", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_MOBILE2))));
                                jsonObject.accumulate("landline", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_LANDLINE))));
                                jsonObject.accumulate("email", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_EMAIL))));
                                jsonObject.accumulate("business", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_BUSSINESS))));
                                jsonObject.accumulate("address", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ADDRESS))));
                                jsonObject.accumulate("contactperson", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_NAME))));
                                String v_front = cur.getString(cur.getColumnIndex(dbhandler.CLIENT_VISITING_CARD_FRONT));
                                v_front = v_front.replace("http://crm.tech9teen.com/", "");
                                jsonObject.accumulate("visitingfront", v_front);

                                String v_back = cur.getString(cur.getColumnIndex(dbhandler.CLIENT_VISITING_CARD_BACK));
                                v_back = v_back.replace("http://crm.tech9teen.com/", "");
                                jsonObject.accumulate("visitingback", v_back);
                                jsonObject.accumulate("createddate", cur.getString(cur.getColumnIndex(dbhandler.VISIT_DATE)));
                                jsonObject.accumulate("latitude", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_LATTITUDE)));
                                jsonObject.accumulate("longitude", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)));
                                jsonObject.accumulate("clienttype", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_TYPE)));
                                jsonObject.accumulate("website", cur.getString(cur.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                                jsonObject.accumulate("note", dbhandler.convertEncodedString(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_NOTE))));


                                json = json + jsonObject.toString() + ",";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        json = json.substring(0, json.length() - 1);
                        json = "[" + json + "]";
                        Log.d("InsertClient Data : ", json);
                        Log.d("Json Data : ", url + "?type=InsertClient&custid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "&data=" + json);
                        params.put("Data", json);


                    }


                    return params;
                }

            };

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(request);


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        //Complete Sending GoalMst DEtails to server
    }


    public void backupDB() {


        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + context.getPackageName() + "/databases/"
                + dbhandler.databasename;
        String backupDBPath = dbhandler.databasename;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //Toast.makeText(context, "DataBase Exported!",Toast.LENGTH_LONG).show();
            Log.d("DatabaseBackup : ", "DataBase Exported!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupFragment(Fragment fragment, String title) {
        setTitle(title);

        if (fragment != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();

            //  fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new FragmentHome();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }


    }

    private void SetUserProfilePictireFromBase64EnodedString() {
        try {
            userDetails = sessionmanager.getSessionDetails();
            String myBase64Image = userDetails.get(SessionManager.KEY_ENODEDED_STRING);
            if (!myBase64Image.equals("")) {


                Bitmap myBitmapAgain = dbhandler.decodeBase64(myBase64Image);

                imgProfilePic.setImageBitmap(myBitmapAgain);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Decode Img Exception : ", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    private void SetGpsCongiguration(final boolean status) {
        if (userDetails.get(SessionManager.KEY_IS_ENABLE_GPS).equals("1")) {


            Dexter.withActivity(DashBoardActivity.this)
                    .withPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {


                            action_checkin.setIcon(R.drawable.icon_checkout_man);
                            CHECKIN_FLAG = true;
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            // startService(new Intent(getBaseContext(), MyLocationService.class));
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                Log.d(TAG, " Start Location updates from Background Service");
                                startService(new Intent(getBaseContext(), MyLocationService.class));


                                if (status == true)

                                {


                                 /*   if (CHECKIN_FLAG == true) {
                                        action_checkin.setIcon(R.drawable.icon_checkout_man);

                                    } else {
                                        action_checkin.setIcon(R.drawable.icon_checkin_marker);

                                    }*/


                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setCancelable(false);
                                    builder.setTitle("Check In Information");
                                    builder.setMessage("Location service has been started");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();


                                        }
                                    });
                                    // builder.show();


                                }

                            } else {
                                if (status == true) {
                                    AlertForLocationServices();
                                }


                            }
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();


        } else {

            AlertForLocationServices();
            //buttonbackground1.setBackgroundColor(Color.parseColor("#3F51B5"));


            //builder.show();


        }
    }

    private void AlertForLocationServices() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);
        builder.setMessage("Would you like to turn ON location services?");
        builder.setCancelable(false);

        /*builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //Toast.makeText(context , "Please enable GPS",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Enable GPS");
                //Log.d(TAG , "Device not supported SIM, Please enable GPS ");

            }
        });

        try {
            AlertDialog alert11 = builder.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            //buttonbackground.setBackgroundColor(Color.BLUE);
            buttonbackground.setTextColor(Color.parseColor("#3F51B5"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#3F51B5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //buttonbackground1.setBackgroundColor(Color.parseColor("#3F51B5"));


        //builder.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        action_checkin = (MenuItem) menu.findItem(R.id.action_checkin);

        action_sync = (MenuItem) menu.findItem(R.id.action_sync);
        SetGpsCongiguration(true);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_checkin) {
            if (CHECKIN_FLAG == false) {
                CHECKIN_FLAG = true;
                SetGpsCongiguration(true);
                //action_checkin.setIcon(R.drawable.icon_checkout_man);

            } else {

                CHECKIN_FLAG = false;


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Check In Information");
                builder.setMessage("Location service has been stopped");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();


                    }
                });
                builder.show();


                Intent intent = new Intent(DashBoardActivity.this, MyLocationService.class);
                stopService(intent);
                action_checkin.setIcon(R.drawable.icon_checkin_marker);

            }
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
            finish();


        } else if (id == R.id.action_sync) {
            //GPS Tracking details
            //All client details
            Toast.makeText(context, "Send all client details to server", Toast.LENGTH_SHORT).show();

            sendAllClientDetailsToServer();
            sendFollowupDetailsToServer();

            sendOrderDetailsToServer();

     /*       // define the animation for rotation
            Animation animation = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);
            //animRotate = AnimationUtils.loadAnimation(this, R.anim.rotation);
```````````````````````
            animation.setRepeatCount(Animation.INFINITE);

            ivSyncAnimation = new ImageView(this);
            ivSyncAnimation.setImageDrawable(getResources().getDrawable(R.drawable.icon_sync_circuler));

            ivSyncAnimation.startAnimation(animation);
            item.setActionView(ivSyncAnimation);*/


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

            setupFragment(new FragmentHome(), getString(R.string.app_name));
        } else if (id == R.id.nav_followup) {
            setupFragment(new FragmentFollowup(), "Follow up");

        } else if (id == R.id.nav_import) {
            getAllClientDetailsFromServer();

            getAllClientFollowupDetailsFromServer();
            getAllOrderOrServiceDetailsFromServer();


        } else if (id == R.id.nav_clients) {

            FragmentClients fc = new FragmentClients();
            setupFragment(fc, "Clients");
        } else if (id == R.id.nav_logout) {


            Intent intent = new Intent(context, CalendarDemoActivity.class);
            startActivity(intent);
            finish();
          /*  context.deleteDatabase(dbhandler.databasename);
            sessionmanager.logoutUser();*/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getAllOrderOrServiceDetailsFromServer() {


        String url = AllKeys.WEBSITE + "ViewServiceMaster?type=service&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "";
        Log.d(TAG, "URL ViewServiceMaster " + url);

        JsonObjectRequest reuqest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ViewServiceMaster  : " + response);

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_LOGINDATA);
                            sd.delete(dbhandler.TABLE_ORDER_MASTER, null, null);

                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject c = arr.getJSONObject(i);


                                ContentValues cv = new ContentValues();
                                cv.put(dbhandler.ORDER_ID, AllKeys.TAG_ORDERID);
                                cv.put(dbhandler.ORDER_SERVICEID, AllKeys.TAG_SERVICEID);
                                cv.put(dbhandler.ORDER_QUANTITY, AllKeys.TAG_QUIANTITY);
                                cv.put(dbhandler.ORDER_RATE, AllKeys.TAG_RATE);
                                cv.put(dbhandler.ORDER_DISCOUNT_AMOUNT, AllKeys.TAG_DISCOUNT_AMT);
                                cv.put(dbhandler.ORDER_NET_AMOUNT, AllKeys.TAG_NET_AMT);
                                cv.put(dbhandler.ORDER_CLIENT_ID, AllKeys.TAG_CLIENT_ID);
                                cv.put(dbhandler.ORDER_EMPLOYEE_ID, AllKeys.TAG_EMPID);
                                cv.put(dbhandler.ORDER_DATE, AllKeys.TAG_DATE);
                                cv.put(dbhandler.CLIENT_DEVICE_TYPE, AllKeys.TAG_DEVICETYPE);

                                Log.d(TAG, "OrderMaster Insert Data : " + cv.toString());

                                sd.insert(dbhandler.TABLE_ORDER_MASTER, null, cv);


                            }


                            hideDialog();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "ViewServiceMaster Error " + error.getMessage());
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {

                    getAllOrderOrServiceDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(reuqest);


    }


    private void getAllClientFollowupDetailsFromServer() {


        String url = AllKeys.WEBSITE + "ViewFollowupData?type=followup&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "";
        Log.d(TAG, "URL ViewFollowupData " + url);

        JsonObjectRequest reuqest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ViewFollowupData  : " + response);

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_LOGINDATA);
                            sd.delete(dbhandler.TABLE_FOLLOWUP_MASTER, null, null);

                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject c = arr.getJSONObject(i);


                                ContentValues cv = new ContentValues();
                                cv.put(dbhandler.CLIENT_ID, c.getString(AllKeys.TAG_CLIENTID));
                                cv.put(dbhandler.FOLLOWUP_DATE, c.getString(AllKeys.TAG_DATE));
                                cv.put(dbhandler.FOLLOWUP_TIME, c.getString(AllKeys.TAG_TIME));
                                cv.put(dbhandler.FOLLOWUP_DESCR, c.getString(AllKeys.TAG_DESCRIPTION));
                                cv.put(dbhandler.EMPLOYEE_ID, c.getString(AllKeys.TAG_EMPID));

                                cv.put(dbhandler.CLIENT_DEVICE_TYPE, c.getString(AllKeys.TAG_DEVICETYPE));
                                cv.put(dbhandler.FOLLOWUP_STATUS, c.getString(AllKeys.TAG_FOLLOWUP_STATUS));

                                try {
                                    cv.put(dbhandler.FOLLOWUP_REASON, c.getString(AllKeys.TAG_FOLLOWUP_REASON));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    cv.put(dbhandler.FOLLOWUP_REASON, "");
                                }

                                sd.insert(dbhandler.TABLE_FOLLOWUP_MASTER, null, cv);


                            }


                            hideDialog();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "ViewFollowupData " + error.getMessage());
                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {

                    getAllClientFollowupDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(reuqest);


    }


    private void getAllClientDetailsFromServer() {


        String url = AllKeys.WEBSITE + "ViewClientMst?type=clientdata&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "";
        Log.d(TAG, "URL  " + url);

        JsonObjectRequest reuqest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response  : " + response);

                try {
                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);

                    if (error_status == false) {

                        if (record_status == true) {


                            JSONArray arr = response.getJSONArray(AllKeys.ARRAY_LOGINDATA);
                            sd.delete(dbhandler.TABLE_CLIENTMASTER, null, null);

                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject c = arr.getJSONObject(i);

                                ContentValues cv = new ContentValues();


                                cv.put(dbhandler.CLIENT_NAME, c.getString(AllKeys.TAG_CONTACT_PERSON_NAME));
                                cv.put(dbhandler.CLIENT_COMPANYNAME, c.getString(AllKeys.TAG_COMPANYNAME));
                                cv.put(dbhandler.CLIENT_MOBILE1, c.getString(AllKeys.TAG_MOBILE1));
                                cv.put(dbhandler.CLIENT_MOBILE2, c.getString(AllKeys.TAG_MOBILE2));
                                cv.put(dbhandler.CLIENT_LANDLINE, c.getString(AllKeys.TAG_LANDLINE));
                                cv.put(dbhandler.CLIENT_EMAIL, c.getString(AllKeys.TAG_EMAIL));
                                cv.put(dbhandler.CLIENT_WEBSITE, c.getString(AllKeys.TAG_CLIENT_WEBSITE));
                                cv.put(dbhandler.CLIENT_BUSSINESS, c.getString(AllKeys.TAG_BUSINESS));
                                cv.put(dbhandler.CLIENT_ADDRESS, c.getString(AllKeys.TAG_ADDRESS));
                                cv.put(dbhandler.VISIT_DATE, dbhandler.convertToJsonDateFormat(c.getString(AllKeys.TAG_CREATED_DATE)));
                                cv.put(dbhandler.CLIENT_NOTE, c.getString(AllKeys.TAG_NOTE));
                                cv.put(dbhandler.CLIENT_SYNC_STATUS, "1");
                                cv.put(dbhandler.CLIENT_DEVICE_TYPE, c.getString(AllKeys.TAG_DEVICETYPE));
                                cv.put(dbhandler.CLIENT_LATTITUDE, c.getString(AllKeys.TAG_LATTITUDE));
                                cv.put(dbhandler.CLIENT_LONGTITUDE, c.getString(AllKeys.TAG_LONGTIUDE));
                                cv.put(dbhandler.CLIENT_ID, c.getString(AllKeys.TAG_CLIENTID));
                                cv.put(dbhandler.CLIENT_TYPE, c.getString(AllKeys.TAG_CLIENT_TYPE));
                                cv.put(dbhandler.CLIENT_VISITING_CARD_FRONT, c.getString(AllKeys.TAG_VISITING_CARD_FRONT));
                                cv.put(dbhandler.CLIENT_VISITING_CARD_BACK, c.getString(AllKeys.TAG_VISITING_CARD_BACK));
                                cv.put(dbhandler.EMPLOYEE_ID, AllKeys.TAG_EMPID);

                                sd.insert(dbhandler.TABLE_CLIENTMASTER, null, cv);


                            }
                            Toast.makeText(context, "Client details has been sync successfully", Toast.LENGTH_SHORT).show();

                            hideDialog();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    hideDialog();
                } else {

                    getAllClientDetailsFromServer();
                }


            }
        });
        MyApplication.getInstance().addToRequestQueue(reuqest);


    }

    private void showDialog() {

        if (!pDialog.isShowing()) {
            pDialog.show();

        }
    }

    private void hideDialog() {

        if (pDialog.isShowing()) {
            pDialog.cancel();
            pDialog.dismiss();

        }
    }


}
