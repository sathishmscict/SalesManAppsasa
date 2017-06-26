package com.salesmanapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.salesmanapp.R;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.ClientData;
import com.salesmanapp.session.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class FragmentHome extends android.support.v4.app.Fragment {


    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private String TAG = FragmentHome.class.getSimpleName();
    private SpotsDialog pDialog;

   /* private RecyclerView rv_clients;*/
    private dbhandler db;
    private SQLiteDatabase sd;
    private ArrayList<ClientData> list_clients = new ArrayList<ClientData>();
    //private TextView txtnodata;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public FragmentHome() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,
                false);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);


        sessionmanager = new SessionManager(getActivity());
        userDetails = sessionmanager.getSessionDetails();


        db = new dbhandler(getActivity());
        sd = db.getWritableDatabase();
        sd = db.getReadableDatabase();



      /*   txtnodata = (TextView)rootView.findViewById(R.id.txtnodata);
       rv_clients = (RecyclerView) rootView.findViewById(R.id.rv_clients);

        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(context);
        rv_clients.setLayoutManager(lmanagr);
        rv_clients.setItemAnimator(new DefaultItemAnimator());*/


        //FillDataOnRecyclerView();


        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)rootView. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        // Inflate the layout for this fragment
        return rootView;
    }

  /*  private void FillDataOnRecyclerView()
    {
        String query = "select * from " + dbhandler.TABLE_CLIENTMASTER + "";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_clients.clear();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {

                //ClientData(String clientid, String clientname, String moibleno, String bussiness, String address, String note,String email) {
                ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)), c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)));
                list_clients.add(cd);


            }


            txtnodata.setVisibility(View.GONE);
            rv_clients.setVisibility(View.VISIBLE);

            ClientsDataAdapterRecyclerView adapter = new ClientsDataAdapterRecyclerView(getActivity(),list_clients,getActivity());
            rv_clients.setAdapter(adapter);


        }
        else
        {
            Toast.makeText(getActivity(), "No client records found", Toast.LENGTH_SHORT).show();

            txtnodata.setVisibility(View.VISIBLE);
            rv_clients.setVisibility(View.GONE);
        }


    }*/
    //onCreateView Completed

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        int yesterdaycount = getYesterFollowupcount();
        adapter.addFragment(new FragmentYesterday(), "Yesterday-"+ yesterdaycount +"");
        int todaycount = getTodayFollowupcount();
        adapter.addFragment(new FragmentToday(), "Today-"+ todaycount +"");

        int tommorrowcount = getTommorrowFollowupcount();
        adapter.addFragment(new FragmentTomorrow(), "Tommorrow-"+ tommorrowcount +"");
        adapter.addFragment(new FragmentSpecificDate(), "specificday");

        viewPager.setAdapter(adapter);
    }

    private int getTommorrowFollowupcount() {

        String date = null;
        try {
            final Calendar cal = Calendar.getInstance();
             cal.add(Calendar.DATE, 1);
            Date asdas = cal.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println(cal.getTime());


            date = format1.format(cal.getTime());
            System.out.println(date);



            System.out.println(format1.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + "";
        query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +" and fm."+ dbhandler.FOLLOWUP_DATE +"='"+ date +"'";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);


        return c.getCount();


    }

    private int getTodayFollowupcount() {

        String date = null;
        try {
            final Calendar cal = Calendar.getInstance();
           // cal.add(Calendar.DATE, -1);
            Date asdas = cal.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println(cal.getTime());


            date = format1.format(cal.getTime());
            System.out.println(date);



            System.out.println(format1.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String query = "select * from " + dbhandler.TABLE_FOLLOWUP_MASTER + "";
        query = "select *,fm."+ dbhandler.CLIENT_DEVICE_TYPE +" as DevicType  from "+ dbhandler.TABLE_FOLLOWUP_MASTER +" as fm,"+ dbhandler.TABLE_CLIENTMASTER +"  as cm where cm."+ dbhandler.CLIENT_ID +" =fm."+ dbhandler.CLIENT_ID +" and fm."+ dbhandler.FOLLOWUP_DATE +"='"+ date +"'";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);


        return c.getCount();
    }

    private int getYesterFollowupcount() {
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
        return c.getCount();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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



}