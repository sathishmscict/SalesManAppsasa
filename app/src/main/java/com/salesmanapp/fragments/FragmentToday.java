package com.salesmanapp.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.salesmanapp.helper.MyBroadcastReceiver;
import com.salesmanapp.R;
import com.salesmanapp.adapter.FollowupDataAdapterRecyclerView;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.FollowupData;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class FragmentToday extends android.support.v4.app.Fragment {



    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();

    private dbhandler db;
    private SQLiteDatabase sd;

    private TextView txtnodata;
    private RecyclerView rv_followup;
    private String TAG = FragmentToday.class.getSimpleName();
    private ArrayList<FollowupData> list_followups= new ArrayList<FollowupData>();



    public FragmentToday() {
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

        txtnodata = (TextView)rootView.findViewById(R.id.txtnodata);
        rv_followup = (RecyclerView) rootView.findViewById(R.id.rv_followup);

        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(getActivity());
        rv_followup.setLayoutManager(lmanagr);
        rv_followup.setItemAnimator(new DefaultItemAnimator());



        FillDataOnRecyclerView();

        //registerForContextMenu(mRecyclerView_goal);

        // Inflate the layout for this fragment
        return rootView;
    }



    private String setCurrentDate(int year, int month, int day) {

        String mm="";
        if(month <= 9)
        {
            mm ="0"+String.valueOf(month);

        }
        else
        {
            mm =String.valueOf(month);
        }

        return  day+"-"+mm+"-"+year;

    }

    private void FillDataOnRecyclerView()
    {


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        ++month;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String date = setCurrentDate(year,month,day);

        String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + "";
        query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +" and fm."+ dbhandler.FOLLOWUP_DATE +"='"+ date +"'";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_followups.clear();
        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                //FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
                FollowupData followupData = new FollowupData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)),c.getString(c.getColumnIndex("DevicType")),c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)),c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                //ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                list_followups.add(followupData);


            }


            txtnodata.setVisibility(View.GONE);
            rv_followup.setVisibility(View.VISIBLE);

            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(getActivity(),list_followups,getActivity(),"followup");
            rv_followup.setAdapter(adapter);


        }
        else
        {
            Toast.makeText(getActivity(), "No client records found", Toast.LENGTH_SHORT).show();

            txtnodata.setVisibility(View.VISIBLE);
            rv_followup.setVisibility(View.GONE);
        }


    }

  /*  private void FillDataOnRecyclerView()
    {


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        ++month;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String date = setCurrentDate(year,month,day);

        String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + "";
        query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +" and fm."+ dbhandler.FOLLOWUP_DATE +"='"+ date +"'";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_followups.clear();
        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                //FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
                FollowupData followupData = new FollowupData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)),c.getString(c.getColumnIndex("DevicType")),c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)),c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)));
                //ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                list_followups.add(followupData);



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

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                //Log.d(TAG , "Alarm set in " + calendar.getTimeInMillis() + " seconds, Alaram ID : "+(c.getInt(c.getColumnIndex(dbhandler.SUBTASK_ID))));

                Intent myIntent = new Intent(getActivity(), MyBroadcastReceiver.class);
                //  myIntent = new Intent(getActivity() , NotificationPublisher.class);

                Log.d(TAG, "Notification Data : " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)) + "," + c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)));
                myIntent.putExtra("ALARAMID", String.valueOf(c.getInt(c.getColumnIndex(dbhandler.FOLLOWUP_ID))));

                Log.d(TAG, "Final Time in MilliSeconds : " + calendar.getTimeInMillis());
                myIntent.putExtra("SATISH", c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)) + ",Meeting Regards ,  " + c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)) + "," + calendar.getTimeInMillis());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), c.getInt(c.getColumnIndex(dbhandler.FOLLOWUP_ID)), myIntent, 0);

                //Log.d(TAG , " Time in Milli Seconds  "+AlarmManager.INTERVAL_DAY );

                //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  //set repeating every 24 hours
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                                    *//*long futureInMillis = SystemClock.elapsedRealtime() + calendar.getTimeInMillis();
                                    Log.d(TAG, " Future MilliSeconds : " + futureInMillis);*//*

                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  //set repeating every 24 hours

                if(System.currentTimeMillis()  <= calendar.getTimeInMillis())
                {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);  //set repeating every 24 hours

                }

                //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,pendingIntent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);  //set repeating every 24 hours

                Log.d(TAG, "Alarm set in " + calendar.getTimeInMillis() + " seconds");

                calendar.clear();







            }


            txtnodata.setVisibility(View.GONE);
            rv_followup.setVisibility(View.VISIBLE);

            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(getActivity(),list_followups,getActivity());
            rv_followup.setAdapter(adapter);


        }
        else
        {
            Toast.makeText(getActivity(), "No client records found", Toast.LENGTH_SHORT).show();

            txtnodata.setVisibility(View.VISIBLE);
            rv_followup.setVisibility(View.GONE);
        }


    }*/



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