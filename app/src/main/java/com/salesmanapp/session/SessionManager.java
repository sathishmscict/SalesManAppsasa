package com.salesmanapp.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.salesmanapp.activity.DashBoardActivity;
import com.salesmanapp.activity.LoginActivity;

import java.util.HashMap;

/**
 * Created by Satish Gadde on 13-03-2016.
 */
public class SessionManager {


    // Check For Activation
    public static final String KEY_CODE = "code", KEY_SMSURL = "smsurl",
            KEY_RECEIVECODE = "reccode",
            KEY_VERSTATUS = "verification_status";

    public static final String KEY_CLIENTID = "clientId";

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;


    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SaleManAppPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_ENODEDED_STRING = "Encoded_string", KEY_USER_VERIFIED_MOBILE = "IsVerifiedMobile";


    public static final String KEY_IS_ENABLE_GPS = "IsEnableGPS", KEY_GPS_INTERVAL = "GPSInterval";
    public static final String KEY_LATTITUDE = "Lattitude", KEY_LONGTITUDE = "Longtitude", KEY_ADDRESS = "Address", KEY_COMPANYNAME = "CompanyName";


    public static final String KEY_EMP_MOBILE = "EmpMobile", KEY_EMP_ID = "EmployeeId", KEY_EMP_NAME = "EmployeeName", KEY_EMP_EMAIL = "EmployeeEmail", KEY_EMP_TYPE = "employeeType";
    public static final String KEY_EMP_UNIQUE_CODE="uniquescode";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public int checkLogin() {
        // Check login status
        int ans = 0;
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, DashBoardActivity.class);
            ans = 1;
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

        return ans;

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    public void setGPSLocations(String lattitude, String longtitude, String companyname) {


        editor.putString(KEY_LATTITUDE, lattitude);
        editor.putString(KEY_LONGTITUDE, longtitude);
        editor.putString(KEY_COMPANYNAME, companyname);


        editor.commit();
    }


    public void setGPSSettings(String isEnableGPS, String gpsInterval) {


        editor.putString(KEY_IS_ENABLE_GPS, isEnableGPS);
        editor.putString(KEY_GPS_INTERVAL, gpsInterval);

        editor.commit();
    }


    public void createUserSendSmsUrl(String code, String websiteurl) {

        editor.putString(KEY_CODE, code);
        editor.putString(KEY_SMSURL, websiteurl);// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile=9825681802&code=7692
        editor.commit();

    }

    public void CheckSMSVerificationActivity(String reccode, String actstatus) {

        editor.putString(KEY_RECEIVECODE, reccode);
        editor.putString(KEY_VERSTATUS, actstatus);
        editor.commit();

    }


    public void setClientid(String clientid)
    {
        editor.putString(KEY_CLIENTID , clientid);
        editor.commit();



    }

    public void setUserDetails(String emp_id, String emp_name, String emp_email, String emp_mobile, String emp_type,String emp_code) {


        editor.putString(KEY_EMP_ID, emp_id);
        editor.putString(KEY_EMP_NAME, emp_name);
        editor.putString(KEY_EMP_EMAIL, emp_email);
        editor.putString(KEY_EMP_MOBILE, emp_mobile);
        editor.putString(KEY_EMP_TYPE, emp_type);
        editor.putString(KEY_EMP_UNIQUE_CODE, emp_code);


        editor.commit();

    }


    public void setUserMobile(String mobile) {

        editor.putString(KEY_EMP_MOBILE, mobile);
        editor.commit();

    }

    public void setEncodedImage(String encodeo_image) {


        editor.putString(KEY_ENODEDED_STRING, encodeo_image);

        editor.commit();
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getSessionDetails() {

        //KEY_FULLNAME="uerfullname",KEY_USERNAME="username",KEY_EMAIL="email",KEY_MOBILE="mobileno",KEY_PHONENO="phoneno",KEY_PASSWORD="password";
        HashMap<String, String> user = new HashMap<String, String>();



        user.put(KEY_EMP_UNIQUE_CODE  , pref.getString(KEY_EMP_UNIQUE_CODE , ""));
        user.put(KEY_CLIENTID , pref.getString(KEY_CLIENTID ,"0"));


        user.put(KEY_COMPANYNAME, pref.getString(KEY_COMPANYNAME, ""));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, ""));
        user.put(KEY_LATTITUDE, pref.getString(KEY_LATTITUDE, "0"));

        user.put(KEY_LONGTITUDE, pref.getString(KEY_LONGTITUDE, "0"));

        user.put(KEY_IS_ENABLE_GPS, pref.getString(KEY_IS_ENABLE_GPS, "1"));
        user.put(KEY_GPS_INTERVAL, pref.getString(KEY_GPS_INTERVAL, "30"));


        user.put(KEY_EMP_ID, pref.getString(KEY_EMP_ID, "0"));
        user.put(KEY_EMP_NAME, pref.getString(KEY_EMP_NAME, ""));

        user.put(KEY_USER_VERIFIED_MOBILE, pref.getString(KEY_USER_VERIFIED_MOBILE, "0"));


        user.put(KEY_EMP_NAME, pref.getString(KEY_EMP_NAME, ""));
        user.put(KEY_EMP_EMAIL, pref.getString(KEY_EMP_EMAIL, ""));
        user.put(KEY_EMP_MOBILE, pref.getString(KEY_EMP_MOBILE, ""));


        user.put(KEY_RECEIVECODE, pref.getString(KEY_RECEIVECODE, "0"));
        user.put(KEY_CODE, pref.getString(KEY_CODE, "0"));

        user.put(KEY_SMSURL, pref.getString(KEY_SMSURL, ""));
        user.put(KEY_VERSTATUS, pref.getString(KEY_VERSTATUS, "0"));


        user.put(KEY_ENODEDED_STRING, pref.getString(KEY_ENODEDED_STRING, ""));


        return user;
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    //sessionmanager.setSellerInformation(c.getString(AllKeys.TAG_SELLER_IS_ACTIVE),c.getString(AllKeys.TAG_SELLER_COMPANY_NAME),c.getString(AllKeys.TAG_SELLER_NAME),c.getString(AllKeys.TAG_SELLER_EMAIL),c.getString(AllKeys.TAG_SELLER_ADDRESS),c.getString(AllKeys.TAG_SELLER_CITY),c.getString(AllKeys.TAG_SELLER_STATE),SELLER_ID,c.getString(AllKeys.TAG_SELLER_CODE),c.getString(AllKeys.TAG_SELLER_RATING),c.getString(AllKeys.TAG_SELLER_AVATAR),c.getString(AllKeys.TAG_SELLER_MOBILE));


}
