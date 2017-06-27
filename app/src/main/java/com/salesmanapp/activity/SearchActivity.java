package com.salesmanapp.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.salesmanapp.R;
import com.salesmanapp.adapter.ClientsDataAdapterRecyclerView;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.pojo.ClientData;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Context context=this;
    private TextView txtnodata;
    private RecyclerView rv_clients;
    private String TAG = SearchActivity.class.getSimpleName();
    private dbhandler db;
    private SQLiteDatabase sd;

    private ArrayList<ClientData> list_clients = new ArrayList<ClientData>();
    private MenuItem cutomSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        fab.setVisibility(View.GONE);


        db= new dbhandler(context);
        sd = db.getWritableDatabase();
        sd = db.getReadableDatabase();


        try {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {
            Log.d("Error Actionbar", "" + e.getMessage());

        }




        txtnodata = (TextView)findViewById(R.id.txtnodata);
        rv_clients = (RecyclerView) findViewById(R.id.rv_clients);

        RecyclerView.LayoutManager lmanagr = new LinearLayoutManager(context);
        rv_clients.setLayoutManager(lmanagr);
        rv_clients.setItemAnimator(new DefaultItemAnimator());

        FillDataOnRecyclerView("");



    }
    private void FillDataOnRecyclerView(String str)
    {
        String query;
        if(!str.equals("") && str.equals("null"))
        {
             query = "select * from " + dbhandler.TABLE_CLIENTMASTER;
        }
        else
        {
            query ="select * from "+ dbhandler.TABLE_CLIENTMASTER +" where "+ dbhandler.CLIENT_NAME+" like '%"+ str  +"%' or "+ dbhandler.CLIENT_EMAIL +" like '%"+ str +"%' or "+ dbhandler.CLIENT_MOBILE1 +" like '%"+ str +"%' or "+ dbhandler.CLIENT_ADDRESS +" like '%"+ str +"%' or "+ dbhandler.CLIENT_COMPANYNAME +" like '%"+ str +"%' or "+ dbhandler.CLIENT_BUSSINESS +" like '%"+ str +"%'";

        }



        Log.d(TAG, " Query : " + query);

        Cursor c = sd.rawQuery(query, null);

        Log.d(TAG, "Client Records : " + c.getCount() + "  found");

        list_clients.clear();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {

                //ClientData(String clientid, String clientname, String moibleno, String bussiness, String address, String note,String email) {
                ClientData cd = new ClientData(c.getString(c.getColumnIndex(dbhandler.CLIENT_ID)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE1)), c.getString(c.getColumnIndex(dbhandler.CLIENT_BUSSINESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_ADDRESS)), c.getString(c.getColumnIndex(dbhandler.CLIENT_NOTE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_EMAIL)), c.getString(c.getColumnIndex(dbhandler.CLIENT_MOBILE2)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LANDLINE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_COMPANYNAME)), c.getString(c.getColumnIndex(dbhandler.CLIENT_DEVICE_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_TYPE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LATTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_LONGTITUDE)), c.getString(c.getColumnIndex(dbhandler.CLIENT_WEBSITE)));
                list_clients.add(cd);


            }


            txtnodata.setVisibility(View.GONE);
            rv_clients.setVisibility(View.VISIBLE);

            ClientsDataAdapterRecyclerView adapter = new ClientsDataAdapterRecyclerView(context,list_clients,SearchActivity.this);
            rv_clients.setAdapter(adapter);


        }
        else
        {
            Toast.makeText(context, "No client records found", Toast.LENGTH_SHORT).show();

            txtnodata.setVisibility(View.VISIBLE);
            rv_clients.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        FillDataOnRecyclerView(query);


        return false;
    }



    @Override
    public boolean onQueryTextChange(String newText) {

        FillDataOnRecyclerView(newText);
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {




        getMenuInflater().inflate(R.menu.menu_search, menu);




        cutomSearchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(cutomSearchItem);
        searchView.setOnQueryTextListener(SearchActivity.this);



        return true;


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
