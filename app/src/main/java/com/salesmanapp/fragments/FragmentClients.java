package com.salesmanapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.salesmanapp.activity.AddClientActivity;
import com.salesmanapp.R;
import com.salesmanapp.adapter.ClientsDataAdapterRecyclerView;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.ClientData;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;


public class FragmentClients extends android.support.v4.app.Fragment {


    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private String TAG = FragmentClients.class.getSimpleName();
    private SpotsDialog pDialog;
    private FloatingActionButton fab;
    private RecyclerView rv_clients;
    private dbhandler db;
    private SQLiteDatabase sd;
    private ArrayList<ClientData> list_clients = new ArrayList<ClientData>();
    private TextView txtnodata;



    public FragmentClients() {
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
        View rootView = inflater.inflate(R.layout.fragment_clients, container,
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


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), AddClientActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });






        txtnodata = (TextView)rootView.findViewById(R.id.txtnodata);
        rv_clients = (RecyclerView) rootView.findViewById(R.id.rv_clients);

        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(context);
        rv_clients.setLayoutManager(lmanagr);
        rv_clients.setItemAnimator(new DefaultItemAnimator());


        FillDataOnRecyclerView();


        // Inflate the layout for this fragment
        return rootView;
    }


    //onCreateView Completed
    private void FillDataOnRecyclerView()
    {
        String query = "select * from " + dbhandler.TABLE_CLIENTMASTER+" order by "+ dbhandler.UNIQUE_ID +" desc";
        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_clients.clear();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {

                //ClientData(String clientid, String clientname, String moibleno, String bussiness, String address, String note,String email) {
                ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)), c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_VISITING_CARD_FRONT)), c.getString(c.getColumnIndex(dbhandler.CLIENT_VISITING_CARD_BACK)));
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