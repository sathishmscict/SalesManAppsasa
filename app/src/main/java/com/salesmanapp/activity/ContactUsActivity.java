package com.salesmanapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.R;
import com.salesmanapp.helper.NetConnectivity;
import com.salesmanapp.session.SessionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class ContactUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_MAP = 121;
    private CoordinatorLayout coordinatorLayout;
    private Context context=this;
  //  private TextView txtaddress;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private double latitude;
    private double longitude;
    private GoogleMap googleMap;
    private String TAG = ContactUsActivity.class.getSimpleName();
    private SpotsDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }





        setTitle("Contact Us");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinateLayout);


        pDialog = new SpotsDialog(context);
        pDialog.setCancelable(false);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        } catch (Exception e) {
            Log.d("Error Actionbar", "" + e.getMessage());

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //txtaddress = (TextView)findViewById(R.id.txtaddress);


        sessionManager = new SessionManager(getApplicationContext());
        userDetails = new HashMap<String, String>();
        userDetails = sessionManager.getSessionDetails();

        try {

            latitude = Double.parseDouble(userDetails
                    .get(SessionManager.KEY_LATTITUDE));
            longitude = Double.parseDouble(userDetails
                    .get(SessionManager.KEY_LONGTITUDE));
        } catch (NumberFormatException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        setTitle(userDetails.get(SessionManager.KEY_COMPANYNAME));
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        try {


           // String aa = userDetails.get(SessionManager.KEY_ADDRESS);
          //  aa = aa.replace(",,", "\n");
            //txtaddress.setText("" + aa);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {

            // Loading map
           // initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }






        if(NetConnectivity.isOnline(context))
        {

            // Get the SupportMapFragment and request notification
            // when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(ContactUsActivity.this);

            //GetContactUsDetailsFromServer();

        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactUsActivity.this);

            // set dialog message
            alertDialogBuilder
                    .setTitle("No Internet Connection")
                    .setMessage("Please Check Your Wi-Fi OR Mobile Network Connection And Try Again.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.stat_notify_error)

                    .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity

                            Intent ii=new Intent(context,DashBoardActivity.class);
                            startActivity(ii);
                            //finish();

                            try {
                                ConnectivityManager dataManager;
                                dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                                dataMtd.setAccessible(true);
                                dataMtd.invoke(dataManager, true);
                            } catch (NoSuchMethodException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }



                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            //Toast.makeText(getActivity(),"Please enable wifi or mobile data",Toast.LENGTH_SHORT).show();
        }





    }



    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(final GoogleMap googleMap) {



        Dexter.withActivity(ContactUsActivity.this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        // create marker
                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(latitude, longitude)).title(
                                userDetails.get(SessionManager.KEY_COMPANYNAME));

                        // Changing marker icon
                        MarkerOptions icon = marker.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        // adding marker

                        LatLng latLng = new LatLng(latitude, longitude);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                latLng, 15);
                        googleMap.animateCamera(cameraUpdate);
                        // locationManager.removeUpdates(this);

                        googleMap.addMarker(marker);
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ContactUsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_MAP);


                        } else {


                            googleMap.setMyLocationEnabled(true);


                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();










    }



    public void showDialog() {

        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hideDialog() {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void GetContactUsDetailsFromServer()

    {

        showDialog();

        String url =  AllKeys.WEBSITE+"ViewContactInfo?type=ContactInfo";
        Log.d(TAG, "URL "+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "Response ContactUs : "+response);


                try
                {

                    String str_error = response.getString(AllKeys.TAG_MESSAGE);
                    String str_error_original = response.getString(AllKeys.TAG_ERROR_ORIGINAL);
                    boolean error_status = response.getBoolean(AllKeys.TAG_ERROR_STATUS);
                    boolean record_status = response.getBoolean(AllKeys.TAG_IS_RECORDS);


                    if (error_status == false) {
                        if (record_status == true)
                        {


                            try
                            {

                                JSONArray arr= response.getJSONArray(AllKeys.ARRAY_CONTACTUS_DATA);

                                // looping through All Contacts
                                for (int i = 0; i < arr.length(); i++)
                                {
                                    JSONObject c = arr.getJSONObject(i);


                                    String Address = c.getString(AllKeys.TAG_CONTACT_ADDRESS) + " ,,Email : "
                                            + c.getString(AllKeys.TAG_CONTACT_EMAIL)+" ,,Phone : "+c.getString(AllKeys.TAG_CONTACT_PHONE);

                                    latitude = c.getDouble(AllKeys.TAG_CONTACT_LATTITUDE);
                                    longitude = c.getDouble(AllKeys.TAG_CONTACT_LONGTITUDE);



                                    String Email = c.getString("Email");
                                    String Phone = c.getString("Phone");


*//*
                                    sessionManager.StoreContactUsDetails("" + c.getString(AllKeys.TAG_CONTACT_LATTITUDE),
                                            "" + c.getString(AllKeys.TAG_CONTACT_LONGTITUDE),
                                            "" + c.getString(AllKeys.TAG_CONTACT_ADDRESS) + ",,Phone No : "
                                                    + c.getString(AllKeys.TAG_CONTACT_PHONE) + ",,Email : "
                                                    + c.getString(AllKeys.TAG_CONTACT_EMAIL), Email, Phone);*//*


                                    sessionManager.StoreContactUsDetails("" + c.getString(AllKeys.TAG_CONTACT_LATTITUDE),
                                            "" + c.getString(AllKeys.TAG_CONTACT_LONGTITUDE),
                                            "" + c.getString(AllKeys.TAG_CONTACT_ADDRESS) , Email, Phone);

                                    Log.d("Contact Us Data :", "Address :" + Address + " Lattitude " + latitude + " Longtitude " + longitude + " Email " + Email + " Phone " + Phone + "");






                                }


                                // Get the SupportMapFragment and request notification
                                // when the map is ready to be used.
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(ContactUsActivity.this);




                                try {

                                    userDetails =sessionManager.getSessionDetails();

                                    String aa = userDetails.get(SessionManager.KEY_CONTACT_ADDRESS);
                                    aa = aa.replace(",,", "\n");
                                    txtaddress.setText(Html.fromHtml(aa));

                                } catch (Exception e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }



                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }

                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof ServerError || error instanceof NetworkError)
                {

                    hideDialog();
                }
                else
                {
                    GetContactUsDetailsFromServer();
                }
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);


    }*/








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {

            Intent intent = new Intent(context , DashBoardActivity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context , DashBoardActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

    }
}
