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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salesmanapp.R;
import com.salesmanapp.activity.AddClientActivity;
import com.salesmanapp.adapter.ClientsDataAdapterRecyclerView;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.ClientData;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;


public class FragmentClientData extends android.support.v4.app.Fragment {


    private Context context = getActivity();


    private SessionManager sessionmanager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private String TAG = FragmentClientData.class.getSimpleName();
    private SpotsDialog pDialog;
    private FloatingActionButton fab;
    private RecyclerView rv_clients;
    private dbhandler db;
    private SQLiteDatabase sd;
    private ArrayList<ClientData> list_clients = new ArrayList<ClientData>();
    private TextView txtnodata;



    public FragmentClientData() {
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
        View rootView = inflater.inflate(R.layout.fragment_client_data, container,
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

/*
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), AddClientActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });*/




        EditText edtName = (EditText)rootView. findViewById(R.id.edtName);
        EditText   edtCompanyname = (EditText)rootView. findViewById(R.id.edtCompanyname);
        EditText   edtMobile = (EditText)rootView. findViewById(R.id.edtMobile);
        EditText  edtMobile2 = (EditText)rootView. findViewById(R.id.edtMobile2);
        EditText  edtLandline = (EditText)rootView. findViewById(R.id.edtMobile3);
        EditText  edtEmail = (EditText) rootView.findViewById(R.id.edtEmail);
        EditText  edtWebsite = (EditText) rootView.findViewById(R.id.edtWebsite);
        EditText edtBusiness = (EditText) rootView.findViewById(R.id.edtBussiness);
        EditText edtAddress = (EditText)rootView. findViewById(R.id.edtAddress);
        EditText edtNote = (EditText) rootView.findViewById(R.id.edtNote);

        TextView tvClose = (TextView)rootView.findViewById(R.id.tvClose);
        tvClose.setVisibility(View.GONE);



        String query = "select * from "+ dbhandler.TABLE_CLIENTMASTER +" where "+  dbhandler.CLIENT_ID +"=  '"+ userDetails.get(SessionManager.KEY_CLIENTID) +"'";
                Log.d(TAG, "Query : "+query);
        Cursor cur = sd.rawQuery(query , null);
        if(cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {

                edtName.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_NAME)));
                edtCompanyname.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)));
                edtMobile.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_MOBILE1)));
                edtMobile2.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_MOBILE2)));
                edtLandline.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_LANDLINE)));
                edtEmail.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_EMAIL)));
                edtWebsite.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                edtBusiness.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_BUSSINESS)));
                edtAddress.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_ADDRESS)));
                edtNote.setText(cur.getString(cur.getColumnIndex(dbhandler.CLIENT_NOTE)));

            }

        }












        // Inflate the layout for this fragment
        return rootView;
    }


    //onCreateView Completed


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