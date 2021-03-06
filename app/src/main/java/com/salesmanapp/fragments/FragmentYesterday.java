package com.salesmanapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FragmentYesterday extends Fragment {



    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();

    private dbhandler db;
    private SQLiteDatabase sd;

    private TextView txtnodata;
    private RecyclerView rv_followup;
    private String TAG = FragmentYesterday.class.getSimpleName();
    private ArrayList<FollowupData> list_followups= new ArrayList<FollowupData>();



    public FragmentYesterday() {
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





    private void FillDataOnRecyclerView()
    {





        String date = null;
        try {
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date asdas = cal.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println(cal.getTime());




             date = format1.format(cal.getTime());
            System.out.println(date);



            System.out.println(format1.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        String query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +" and fm."+ dbhandler.FOLLOWUP_DATE +"='"+ date +"'";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_followups.clear();
        if (c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                //FollowupData(String followupid, String followupdate, String followuptime, String followupnote, String clientid, String devicetype, String clientname, String moibleno1, String bussiness, String address, String note, String email, String moibleno2, String landline, String companyname, String clienttype, String lattitude, String longtitude) {
                FollowupData followupData = new FollowupData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_ID)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_TIME)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)),c.getString(c.getColumnIndex("DevicType")),c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)),c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)),c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)),c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)),c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)),c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_STATUS)),c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_REASON)));
                //ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DESCR)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)), c.getString(c.getColumnIndex(dbhandler.FOLLOWUP_DATE)));
                list_followups.add(followupData);


            }


            txtnodata.setVisibility(View.GONE);
            rv_followup.setVisibility(View.VISIBLE);

            FollowupDataAdapterRecyclerView adapter = new FollowupDataAdapterRecyclerView(getActivity(),list_followups,getActivity(),"dashboard");
            rv_followup.setAdapter(adapter);


        }
        else
        {
           // Toast.makeText(getActivity(), "No client records found", Toast.LENGTH_SHORT).show();

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