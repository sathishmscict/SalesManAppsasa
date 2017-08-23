package com.salesmanapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.salesmanapp.R;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.helper.AllKeys;
import com.salesmanapp.helper.NetConnectivity;
import com.salesmanapp.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

public class VerificationActivity extends AppCompatActivity {

    private ProgressDialog pDialog;


    private TextView txtcode;
    private TextView txtverify;
    private TextView txt;
    private TextView txtresend;
    private TextView txterror;
    private Context context = this;
    private SessionManager sessionmanager;
    private HashMap<String, String> userDetails;
    private int counter;
    private Timer timer;


    private String TAG = VerificationActivity.class.getSimpleName();
    private SpotsDialog spotDialog;
    private String response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        //setContentView(R.layout.demo_layout);
        //setContentView(R.layout.demo_layout);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        spotDialog = new SpotsDialog(context);
        spotDialog.setCancelable(false);


        txtcode = (TextView) findViewById(R.id.txtcode);
        txtverify = (TextView) findViewById(R.id.txtverify);
        txt = (TextView) findViewById(R.id.textView2);
        txtresend = (TextView) findViewById(R.id.txtresend);
        txterror = (TextView) findViewById(R.id.txterror);

        txtcode.setClickable(false);
        txtcode.setDuplicateParentStateEnabled(false);
        // txtcode.setEnabled(false);

        sessionmanager = new SessionManager(context);

        userDetails = new HashMap<String, String>();

        userDetails = sessionmanager.getSessionDetails();

        /*counter = Integer.parseInt(userDetails
                .get(SessionManager.KEY_VERIFICATION_COUNTER));*/


        setTitle("Verify Your Mobile Number");


        if (NetConnectivity.isOnline(context)) {
            timer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {
                    // your code here...

                    userDetails = sessionmanager.getSessionDetails();

                    try {
                        userDetails.get(SessionManager.KEY_CODE);

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                            .length() == 4) {
                        if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                                .equals(userDetails
                                        .get(SessionManager.KEY_CODE))) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        txtcode.setText(""
                                                + userDetails
                                                .get(SessionManager.KEY_RECEIVECODE));
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();

                                    }
                                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                                    try {
                                        timer.cancel();
                                        timer.purge();

                                        timer = null;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }




                                }
                            });

                          //  VerificationStatusUpdate();


							/*
                             * if(timer != null) { timer.cancel(); timer = null;
							 * }
							 */

                        }
                    }

                }
            };

            // schedule the task to run starting now and then every hour...
            timer.schedule(hourlyTask, 0l, 1000 * 5); // 1000*10*60 every 10
            // minut
        }

        txtresend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // String msgurl =
                // AllKeys.TAG_WEBSITE+"?action=mobverify&userid="+
                // userDetails.get(SessionManager.KEY_UID) +"&code="+ Vcode +"";

                //new sendSmsToUser().execute();


                //new sendSmsToUserUser().execute();

                Dexter.withActivity(VerificationActivity.this)
                        .withPermission(Manifest.permission.READ_SMS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                sendSMSToUser();


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();


                txterror.setVisibility(View.GONE);


            }
        });

        if (NetConnectivity.isOnline(context)) {

            // new sendSmsToUserUser().execute();


            Dexter.withActivity(VerificationActivity.this)
                    .withPermission(Manifest.permission.READ_SMS)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            sendSMSToUser();


                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();


        } else {
            Toast.makeText(context, "Please enable internet",
                    Toast.LENGTH_SHORT).show();
        }

		/*txt.setText("A verification code is being sent to your mobile number "
                + userDetails.get(SessionManager.KEY_MOBILE)
				+ ". To verify your mobile number, please enter the code once it  arrives.");
		*/


        if (userDetails.get(SessionManager.KEY_EMP_MOBILE).equals("")) {
            txt.setText("+91 97236 13143");
        } else {
            txt.setText("+91 " + userDetails.get(SessionManager.KEY_EMP_MOBILE));

        }


        txtverify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String currentcode = txtcode.getText().toString();

                // CheckVerification(currentcode);

                if (userDetails.get(SessionManager.KEY_CODE)
                        .equals(currentcode)) {

                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                    try {
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                // txtcode.setText(userDetails.get(SessionManager.KEY_RECEIVECODE));
                            }
                        });
                        timer.cancel();
                        timer.purge();

                        timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    VerificationStatusUpdate();


                } else {
                    Toast.makeText(context, "Invalid code", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }

    private void VerificationStatusUpdate() {
        String url_update_status = AllKeys.WEBSITE + "VerificationStatus?type=status&Mobile=" + userDetails.get(SessionManager.KEY_EMP_MOBILE);
        Log.d(TAG, "URL VerificationStatusUpdate : " + url_update_status);
        StringRequest str_sendsms = new StringRequest(Request.Method.GET, url_update_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "Response VerificationStatusUpdate : " + response);


                sessionmanager.CheckSMSVerificationActivity("",
                        "1");

                Intent intent = new Intent(context , DashBoardActivity.class);
                intent.putExtra("ISFIRSTTIME","1");
                startActivity(intent);
                finish();



                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    VerificationStatusUpdate();
                }


                Log.d(TAG,"Error Response :"+error.getMessage());
        }
    });
        MyApplication.getInstance().addToRequestQueue(str_sendsms);

}





	/*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.verification, menu); return true; }
	 */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Toast.makeText(getApplicationContext(), "Please Complete Verification",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
        // intent = new Intent(getApplicationContext(),RegisterActivity.class);


        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }




    /*public class sendSmsToUserUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String sendsms = "";

            if (userDetails.get(SessionManager.KEY_CODE).equals("0")) {
                Random r = new Random();
                int code = r.nextInt(9999 - 1000) + 1000;
                Log.d(TAG, "Verification Code : " + code);


                sendsms = AllKeys.WEBSITE+"VerificationService?type=Verification&code="+ code +"&mobile="+ userDetails.get(SessionManager.KEY_USER_MOBILE) +"";


                Log.d(TAG, "URL sendSMS : " + sendsms);
                sessionmanager.createUserSendSmsUrl("" + code, sendsms);
            } else {

                userDetails = sessionmanager.getSessionDetails();


                sendsms = AllKeys.WEBSITE+"VerificationService?type=Verification&code="+ userDetails.get(SessionManager.KEY_CODE) +"&mobile="+ userDetails.get(SessionManager.KEY_USER_MOBILE) +"";

                //sendsms = userDetails.get(SessionManager.KEY_SMSURL);
                Log.d(TAG, "URL sendSMS : " + sendsms);


            }

            Log.d(TAG, "sendsms res : " + sendsms);


*//*            ServiceHandler sh = new ServiceHandler();
            response = sh.makeServiceCall(sendsms, ServiceHandler.GET);*//*


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            Log.d(TAG, "Send SMS Response :" + response);
            if (!response.contains("sent successfully")) {
                Toast.makeText(context, "Error in message sending,try again...", Toast.LENGTH_SHORT).show();

            }


            hideDialog();
        }
    }*/

    /**
     * Send SMS To User
     */

    public void sendSMSToUser() {


        String sendsms = "";

        if (userDetails.get(SessionManager.KEY_CODE).equals("0")) {
            Random r = new Random();
            int code = r.nextInt(9999 - 1000) + 1000;
            Log.d(TAG, "Verification Code : " + code);
            // sendsms = AllKeys.WEBSITE + "sendSMS/" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "/" + code + "/otp";

            sendsms = AllKeys.WEBSITE +"VerificationService?type=verification&code="+ code+"&mobile="+ userDetails.get(SessionManager.KEY_EMP_MOBILE) +"";


            Log.d(TAG, "URL sendSMS : " + sendsms);
            sessionmanager.createUserSendSmsUrl("" + code, sendsms);
        } else {

            userDetails = sessionmanager.getSessionDetails();
            //sendsms = AllKeys.WEBSITE + "sendSMS/" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "/" + userDetails.get(SessionManager.KEY_CODE) + "/otp";

            sendsms = AllKeys.WEBSITE + "VerificationService?type=verification&code=" + userDetails.get(SessionManager.KEY_CODE) + "&mobile=" + userDetails.get(SessionManager.KEY_EMP_MOBILE) + "";


            //sendsms = userDetails.get(SessionManager.KEY_SMSURL);
            Log.d(TAG, "URL sendSMS : " + sendsms);


        }

        Log.d(TAG, "sendsms res : " + sendsms);

        showDialog();


        StringRequest str_sendsms = new StringRequest(Request.Method.GET, sendsms, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Send SMS Response :" + response);


                try {
                    JSONObject obj = new JSONObject(response);


                    if (obj.getBoolean(AllKeys.TAG_ERROR_STATUS) == false) {

                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Error in message sending,try again...", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();


                hideDialog();


                Log.d(TAG, "Error Response :" + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_sendsms);


    }

    private void showDialog() {
        if (!spotDialog.isShowing()) {
            spotDialog.show();

        }
    }

    private void hideDialog() {
        if (spotDialog.isShowing()) {
            spotDialog.dismiss();


        }
    }
    //Complete Send sms to user


}
