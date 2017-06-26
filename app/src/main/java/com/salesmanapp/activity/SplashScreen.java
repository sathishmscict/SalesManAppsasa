package com.salesmanapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.salesmanapp.R;
import com.salesmanapp.animation.FlipAnimation;
import com.salesmanapp.animation.PulseAnimation;
import com.salesmanapp.animation.RotateAnimation;
import com.salesmanapp.animation.ShakeAnimation;
import com.salesmanapp.app.MyApplication;
import com.salesmanapp.helper.AllKeys;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;


    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1500;

    private String TAG = SplashScreen.class.getSimpleName();

    private SQLiteDatabase sd;
    private Context context = this;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);


            }
            return false;
        }
    };
    private ImageView flipImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


     /*   try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }*/




        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        flipImage = (ImageView) findViewById(R.id.imageView);

        // final ImageView flipImage = (ImageView)findViewById(R.id.flip);


        //Animation Types
        // initRotation();
       // initFlip();
        //initPulse();
        //initShake();







        new Handler().postDelayed(new Runnable() {


        /*      Showing splash screen with a timer. This will be useful when you
             want to show case your app logo / company*/


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, DashBoardActivity.class);
                startActivity(i);
                finish();

                // close this activity
                finish();
            }
        }, AUTO_HIDE_DELAY_MILLIS);


        //getAllCategoryDetailsFromServer();

        //sendCheckoutDetailsToServerUsing_POST();

    }


    private void initShake() {

        //final ImageView shakeImage = (ImageView)findViewById(R.id.shake);

        ShakeAnimation.create().with(flipImage)
                .setDuration(2000)
                .setRepeatMode(ShakeAnimation.RESTART)
                .setRepeatCount(ShakeAnimation.INFINITE)
                .start();
    }

    private void initRotation() {


        RotateAnimation.create().with(flipImage)
                .setRepeatCount(RotateAnimation.INFINITE)
                .setRepeatMode(RotateAnimation.RESTART)
                .setDuration(2000)
                .start();
    }

    private void initPulse() {

        PulseAnimation.create().with(flipImage)
                .setDuration(600)
                .setRepeatCount(PulseAnimation.INFINITE)
                .setRepeatMode(PulseAnimation.REVERSE)
                .start();


    }

    private void initFlip() {


        FlipAnimation.create().with(flipImage)
                .setDuration(3600)
                .setRepeatCount(FlipAnimation.INFINITE)
                .start();

    }





    private void getAllCategoryDetailsFromServer()
    {

        String url_getallcategories = AllKeys.WEBSITE + "getAllCategory";
        Log.d(TAG, "url GetAllCategories :" + url_getallcategories);
        StringRequest str_getallcategories = new StringRequest(Request.Method.GET, url_getallcategories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               Log.d(TAG, "GetAllCategories Response :" + response);
               // sd.delete(dbhandler.TABLE_CATEGORY, null, null);
                if (response.contains("name")) {

                   /*  try
                    {
                        response = dbhandler.convertToJsonFormat(response);
                        JSONObject obj = new JSONObject(response);
                        JSONArray arr = obj.getJSONArray("data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject c = arr.getJSONObject(i);
                            try {
                                ContentValues cv = new ContentValues();
                                cv.put(dbhandler.CATEGORY_ID, c.getString(AllKeys.TAG_CATEGORY_ID));
                                cv.put(dbhandler.CATEGORY_NAME, c.getString(AllKeys.TAG_CATEGORY_NAME));
                                cv.put(dbhandler.CATEGORY_PARENTID, c.getString(AllKeys.TAG_CATEGORY_PARENTID));
                                cv.put(dbhandler.CATEGORY_TYPE, c.getString(AllKeys.TAG_CATEGORY_TYPE));
                                cv.put(dbhandler.CATEGORY_DELETED_AT, c.getString(AllKeys.TAG_CATEGORY_DELETED_AT));
                                cv.put(dbhandler.CATEGORY_CREATED_AT, c.getString(AllKeys.TAG_CATEGORY_CREATED_AT));
                                cv.put(dbhandler.CATEGORY_UPDATED_AT, c.getString(AllKeys.TAG_CATEGORY_UPDATED_AT));
                                cv.put(dbhandler.CATEGORY_MLM_DISCOUNT, c.getString(AllKeys.TAG_CATEGORY_MLM_DISCOUNT));
                                cv.put(dbhandler.CATEGORY_SEQUENCE_NO, c.getString(AllKeys.TAG_CATEGORY_SEQUENCE_NO));
                                cv.put(dbhandler.CATEGORY_SHIPPING_COST, c.getString(AllKeys.TAG_CATEGORY_SHIPING_COST));
                                cv.put(dbhandler.CATEGORY_SHIPING_COST_SELLER, c.getString(AllKeys.TAG_CATEGORY_SHIPPING_COST_SELLER));
                                cv.put(dbhandler.CATEGORY_SHIPPING_COST_BUYER, c.getString(AllKeys.TAG_CATEGORY_SHIPPING_COST_BUYER));

                                sd.insert(dbhandler.TABLE_CATEGORY, null, cv);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "Error in Inserting Data : " + e.getMessage());
                                FirebaseCrash.report(new Exception("Error Inserting Category Data : " + e.getMessage()));

                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
*/

                }

                Intent i = new Intent(SplashScreen.this, DashBoardActivity.class);
                startActivity(i);
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError) {

                    Log.d(TAG, "Server Error");
                    Intent i = new Intent(SplashScreen.this, DashBoardActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    getAllCategoryDetailsFromServer();
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_getallcategories);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
