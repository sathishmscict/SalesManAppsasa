package com.salesmanapp.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.session.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Satish Gadde on 25-10-2016.
 */

public class MyLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private String TAG = MyLocationService.class.getSimpleName();
/*

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG,"onBind Called");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.d(TAG,"onStartCommand Called");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy Called");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

*/


    //private static final String TAG = MyLocationService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private int LOCATION_INTERVAL = 1000 * 10;
    private static final float LOCATION_DISTANCE = 0;//10
    private SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<String, String>();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder builder;
    private Geocoder geocoder;
    private dbhandler db;
    private SQLiteDatabase sd;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG, "onLocationChanged: " + location);

            mLastLocation.set(location);

            /**
             * Sending Current Location Details To server
             */


            String time = dbhandler.getDateTime();
            Log.d("Data & Time : ", time);
            String CurrentDate = time.substring(0, 10);
            Log.d("Current Date : ", CurrentDate);
            String CurrentTime = time.substring(11, time.length());
            Log.d("Current Time : ", CurrentTime);


            time = time.substring(time.length() - 8, time.length());
            Log.d("Time : ", time);


            List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                sessionManager.setGPSLocations(String.valueOf(location.getLatitude()) , String.valueOf(location.getLongitude()),"");
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Log.d(TAG, " Address : " + addresses.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Error in converting lattitude & Longtitude to address : " + e.getMessage());




            }


            if (addresses != null)
            {

                String address = "" + addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                Log.d(TAG, "Address 1: " + address);
                String city = "" + addresses.get(0).getLocality();
                String state = "" + addresses.get(0).getAdminArea();
                String country = "" + addresses.get(0).getCountryName();
                String postalCode = "" + addresses.get(0).getPostalCode();
                String knownName = "" + addresses.get(0).getFeatureName(); // Only if available else return NULL


                String FullAddress = "";
                if (!address.equals("")) {

                    FullAddress = FullAddress + address;

                }
                if (!city.equals("")) {

                    FullAddress = FullAddress + "," + city;
                }
                if (!state.equals("")) {

                    FullAddress = FullAddress + "," + state;
                }
                if (!country.equals("")) {

                    FullAddress = FullAddress + "," + country;

                }

                if (!postalCode.equals("")) {

                    FullAddress = FullAddress + "," + postalCode;
                }
                if (!knownName.equals("")) {

                    FullAddress = FullAddress + "," + knownName;
                }

                if (knownName.length() < 7) {
                    knownName = knownName + address;
                }
                String url_sendlocation = AllKeys.WEBSITE + "InsertStudentLocation?type=insertlocation&empid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "&lattitude=" + location.getLatitude() + "&longtitude=" + location.getLongitude() + "&date=" + CurrentDate + "&time=" + CurrentTime + "&knownname=" + dbhandler.convertEncodedString(knownName) + "&address=" + dbhandler.convertEncodedString(FullAddress) + "";
                //url_sendlocation = AllKeys.WEBSITE + "ServiceT.asmx/InsertFacultyLocation?type=insertlocation&facultyid=" + userDetails.get(SessionManager.KEY_EMP_ID) + "&lattitude=" + location.getLatitude() + "&longtitude=" + location.getLongitude() + "&date=" + CurrentDate + "&time=" + CurrentTime + "&address=" + dbhandler.convertEncodedString(FullAddress) + "&knownname=" + dbhandler.convertEncodedString(knownName) + "&clientid=0";
                url_sendlocation = AllKeys.WEBSITE+"InsertGprsData?type=gprs&empid="+ userDetails.get(SessionManager.KEY_EMP_ID) +"&datetime="+ CurrentDate+" "+CurrentTime +"&latitude="+ location.getLatitude() +"&longitude="+ location.getLongitude() +"";


                Log.d(TAG, "URL Send Faculty Location : " + url_sendlocation);


                StringRequest str_sendlocation = new StringRequest(Request.Method.GET, url_sendlocation, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d(TAG, "InsertFacultyLocation Response : " + response.toString());

                        ContentValues cv = new ContentValues();
                        cv.put(dbhandler.LOCATION_SYNC_STATUS, "1");
                        cv.put(dbhandler.LOCATION_LATTITUDE, location.getLatitude());
                        cv.put(dbhandler.LOCATION_LONGTITUDE, location.getLongitude());
                        cv.put(dbhandler.LOCATION_TIME, dbhandler.getDateTime());

                        sd.insert(dbhandler.TABLE_LOCATION_MASTER, null, cv);
                        cv.clear();

                        Log.d(TAG, "Inserted as 1");



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "InsertFacultyLocation Error : " + error.getMessage());

                        if(error instanceof NetworkError)
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(dbhandler.LOCATION_SYNC_STATUS, "0");
                            cv.put(dbhandler.LOCATION_LATTITUDE, location.getLatitude());
                            cv.put(dbhandler.LOCATION_LONGTITUDE, location.getLongitude());
                            cv.put(dbhandler.LOCATION_TIME, dbhandler.getDateTime());
                            sd.insert(dbhandler.TABLE_LOCATION_MASTER, null, cv);
                            cv.clear();
                            Log.d(TAG, "Inserted as 0");

                        }

                    }
                });
                MyApplication.getInstance().addToRequestQueue(str_sendlocation);

            }
            else
            {
                ContentValues cv = new ContentValues();
                cv.put(dbhandler.LOCATION_SYNC_STATUS, "0");
                cv.put(dbhandler.LOCATION_LATTITUDE, location.getLatitude());
                cv.put(dbhandler.LOCATION_LONGTITUDE, location.getLongitude());
                cv.put(dbhandler.LOCATION_TIME, dbhandler.getDateTime());
                sd.insert(dbhandler.TABLE_LOCATION_MASTER, null, cv);
                cv.clear();
                Log.d(TAG, "Inserted as 0");
            }


            /**
             * Complete Sending Location details to server
             */


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        geocoder = new Geocoder(this, Locale.getDefault());
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");


        try {
            //startService(new Intent(this, AppUsageCheckServices.class));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error in Calling Service");
        }

        sessionManager = new SessionManager(getApplicationContext());

        userDetails = sessionManager.getSessionDetails();

        db = new dbhandler(getApplicationContext());
        sd = db.getWritableDatabase();


        if (userDetails.get(SessionManager.KEY_VERSTATUS).equals("1")) {


            initializeLocationManager();
            try {
                LOCATION_INTERVAL = Integer.parseInt(userDetails.get(SessionManager.KEY_GPS_INTERVAL)) * 1000;

                Log.d(TAG, "GPS INTERVAL : " + LOCATION_INTERVAL);
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (SecurityException ex) {
                Log.d(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (SecurityException ex) {
                Log.d(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }

        }


    }


    private synchronized void buildGoogleApiClient() {
        Log.i("TAG", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(MyLocationService.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }

    private void createLocationRequest() {
        Log.i("TAG", "CreateLocationRequest");
        mLocationRequest = new LocationRequest();
        long UPDATE_INTERVAL = 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        long FASTEST_INTERVAL = 1000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {

                    mLocationManager.removeUpdates(mLocationListeners[i]);

                } catch (Exception ex) {
                    Log.d(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

}
