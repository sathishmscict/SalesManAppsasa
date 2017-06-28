package com.salesmanapp;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.salesmanapp.session.SessionManager;


import java.util.HashMap;


/**
 * Created by Satish Gadde on 02-09-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        Context context = this;


    }

    public String onTokenRefreshNew(Context context) {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token new : " + refreshedToken);


        return refreshedToken;
        //sendRegistrationToServer(refreshedToken, context);

    }



}
