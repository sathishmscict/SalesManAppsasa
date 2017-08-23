package com.salesmanapp.database;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.salesmanapp.activity.DashBoardActivity;
import com.salesmanapp.R;
import com.salesmanapp.activity.FollowupResponseActivity;
import com.salesmanapp.adapter.FollowupDataAdapterRecyclerView;
import com.salesmanapp.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class dbhandler extends SQLiteOpenHelper {


    public static final String databasename = "SalesManApp.db";
    public static final int dbversion = 29;
    private final Context context;

    String query = "";


    public static final String TABLE_CLIENTMASTER = "ClientMst";
    public static final String CLIENT_ID = "ClientId";

    public static final String UNIQUE_ID = "Id";
    public static final String CLIENT_NAME = "ContactPersonName";
    public static final String CLIENT_COMPANYNAME = "CompanyName";
    public static final String CLIENT_LATTITUDE = "Lattiude";
    public static final String CLIENT_LONGTITUDE = "Longtitude";
    public static final String CLIENT_EMAIL = "Email";
    public static final String CLIENT_MOBILE1 = "Mobile1";
    public static final String CLIENT_MOBILE2 = "Mobile2";
    public static final String CLIENT_LANDLINE = "Landline3";
    public static final String CLIENT_BUSSINESS = "Bussiness";
    public static final String VISIT_DATE = "VisitDate";
    public static final String CLIENT_ADDRESS = "Address";
    public static final String CLIENT_NOTE = "Note";
    //public static final String CLIENT_SYNC_STATUS = "SyncStatus";
    public static final String CLIENT_DEVICE_TYPE = "DeviceType";
    public static final String CLIENT_TYPE = "ClientType";
    public static final String CLIENT_VISITING_CARD_FRONT = "ClientVisitingCardFront";
    public static final String CLIENT_VISITING_CARD_BACK = "ClientVisitingCardBack";
    public static final String CLIENT_WEBSITE = "Website";
    //public static final String CLIENT_EMPLOYEEID = "EmployeeId";


    public static final String TABLE_LOCATION_MASTER = "LocationMst";
    public static final String LOCATION_ID = "LocationId";
    public static final String LOCATION_TIME = "LocationTime";
    public static final String LOCATION_LATTITUDE = "Lattitude";
    public static final String LOCATION_LONGTITUDE = "Longtitude";
   // public static final String SYNC_STATUS = "syncStatus";SyncStatus


    public static final String TABLE_FOLLOWUP_MASTER = "FollowupMaster";
    public static final String FOLLOWUP_ID = "FollowupId";
    public static final String FOLLOWUP_DESCR = "Description";
    public static final String FOLLOWUP_DATE = "FollowupDate";
    public static final String FOLLOWUP_TIME = "FollowupTime";
    //public static final String FOLLOWUP_CLIENT_ID = "ClientId";
    public static final String EMPLOYEE_ID = "EmployeeId";
    //public static final String FOLLOWUP_DEVICE_TYPE = "DeviceType";
    public static final String FOLLOWUP_STATUS = "FollowupStatus";
    public static final String FOLLOWUP_REASON = "FollowupReason";

    /*FOLLOWUP_STATUS
    0= Default
    1 =Yes
    2=No
    3=Reschedule*/


    public static final String TABLE_SERVICES = "ServiceMst";
    public static final String SERVICE_ID = "ServiceId";
    public static final String SERVICE_NAME = "ServiceName";


    public static final String TABLE_ORDER_MASTER = "OrderMaster";
    public static final String ORDER_ID = "OrderId";
    public static final String ORDER_SERVICEID = "ServiceId";
    public static final String ORDER_QUANTITY = "Quantity";
    public static final String ORDER_RATE = "Rate";
    public static final String ORDER_DISCOUNT_AMOUNT = "DiscountAmount";
    public static final String ORDER_NET_AMOUNT = "NetAmount";
    public static final String ORDER_CLIENT_ID = "ClientId";
    public static final String ORDER_EMPLOYEE_ID = "EmployeeId";
    public static final String ORDER_DATE = "Date";
    public static final String ORDER_DESCR = "Descr";

    public static final String SYNC_STATUS = "SyncStatus";


    private static String TAG = dbhandler.class.getSimpleName();


    public dbhandler(Context context) {
        super(context, databasename, null, dbversion);
        this.context = context;

        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {


     //       query = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTMASTER + "(" + UNIQUE_ID + " INTEGER PRIMARY KEY ," + CLIENT_ID + " INTEGER ," + CLIENT_DEVICE_TYPE + " TEXT," + CLIENT_NAME + " TEXT," + CLIENT_EMAIL + " TEXT," + CLIENT_MOBILE1 + " TEXT," + CLIENT_MOBILE2 + " TEXT," + CLIENT_LANDLINE + " TEXT," + CLIENT_BUSSINESS + " TEXT," + VISIT_DATE + " TEXT," + CLIENT_ADDRESS + " TEXT, " + CLIENT_NOTE + " TEXT," + CLIENT_SYNC_STATUS + " TEXT," + CLIENT_COMPANYNAME + " TEXT," + CLIENT_LATTITUDE + " TEXT," + CLIENT_LONGTITUDE + " TEXT," + CLIENT_TYPE + " TEXT," + CLIENT_VISITING_CARD_FRONT + " TEXT," + CLIENT_VISITING_CARD_BACK + " TEXT," + CLIENT_WEBSITE + " TEXT," + EMPLOYEE_ID + " TEXT,"+ SYNC_STATUS +" TEXT)";
            query = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTMASTER + "(" + UNIQUE_ID + " INTEGER PRIMARY KEY ," + CLIENT_ID + " INTEGER ," + CLIENT_DEVICE_TYPE + " TEXT," + CLIENT_NAME + " TEXT," + CLIENT_EMAIL + " TEXT," + CLIENT_MOBILE1 + " TEXT," + CLIENT_MOBILE2 + " TEXT," + CLIENT_LANDLINE + " TEXT," + CLIENT_BUSSINESS + " TEXT," + VISIT_DATE + " TEXT," + CLIENT_ADDRESS + " TEXT, " + CLIENT_NOTE + " TEXT," + CLIENT_COMPANYNAME + " TEXT," + CLIENT_LATTITUDE + " TEXT," + CLIENT_LONGTITUDE + " TEXT," + CLIENT_TYPE + " TEXT," + CLIENT_VISITING_CARD_FRONT + " TEXT," + CLIENT_VISITING_CARD_BACK + " TEXT," + CLIENT_WEBSITE + " TEXT," + EMPLOYEE_ID + " TEXT,"+ SYNC_STATUS +" TEXT)";
            Log.d("Table  : " + TABLE_CLIENTMASTER, query);
            db.execSQL(query);


            query = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION_MASTER + "(" + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LOCATION_TIME + " TEXT," + LOCATION_LATTITUDE + " TEXT," + LOCATION_LONGTITUDE + " TEXT," + SYNC_STATUS + " TEXT)";
            Log.d("Table  : " + TABLE_LOCATION_MASTER, query);
            db.execSQL(query);

            query = "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICES + "(" + SERVICE_ID + " INTEGER PRIMARY KEY ," + SERVICE_NAME + " TEXT)";
            Log.d("Table  : " + TABLE_SERVICES, query);
            db.execSQL(query);


            query = "CREATE TABLE IF NOT EXISTS " + TABLE_FOLLOWUP_MASTER + "(" + FOLLOWUP_ID + " TEXT," + FOLLOWUP_DESCR + " TEXT," + FOLLOWUP_DATE + " TEXT," + FOLLOWUP_TIME + " TEXT," + CLIENT_ID + " TEXT, " + EMPLOYEE_ID + " TEXT," + CLIENT_DEVICE_TYPE + " TEXT," + FOLLOWUP_STATUS + " TEXT," + FOLLOWUP_REASON + " TEXT,"+ SYNC_STATUS +" TEXT)";
            Log.d("Table  : " + TABLE_FOLLOWUP_MASTER, query);
            db.execSQL(query);


            query = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_MASTER + "(" + UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ORDER_ID + " TEXT," + ORDER_SERVICEID + " INTEGER," + ORDER_QUANTITY + " TEXT," + ORDER_RATE + " TEXT, " + ORDER_DISCOUNT_AMOUNT + " TEXT," + ORDER_NET_AMOUNT + " TEXT," + ORDER_CLIENT_ID + " TEXT," + ORDER_EMPLOYEE_ID + " TEXT," + ORDER_DATE + " TEXT," + CLIENT_DEVICE_TYPE + " TEXT,"+ ORDER_DESCR +" TEXT,"+ SYNC_STATUS +" TEXT)";
            Log.d("Table  : " + TABLE_ORDER_MASTER, query);
            db.execSQL(query);


            db.execSQL("create table IF NOT EXISTS Notification_Mst(id INTEGER PRIMARY KEY AUTOINCREMENT,header TEXT,notification TEXT,ndate text)");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, "dbhandler onCreate : " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        try {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTMASTER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWUP_MASTER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_MASTER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);






/*
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASK);*/
           /* db.execSQL("DROP TABLE IF EXISTS Notification_Mst");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTMASTER);
*/
            db.setVersion(newVersion);
            onCreate(db);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


    public static final String convertedDoubleAmount(String amt) {
        Log.d(TAG, "Value : " + amt);
        NumberFormat formatter = null;
        try {
            formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            amt = formatter.format(amt);


        } catch (Exception e) {
            e.printStackTrace();

        }
        return amt;
    }

    public static final String getCompanyNameByCompnayId(int id) {

        String companyname = "";
        if (id == 3)
            companyname = "Shah Enterprise";
        else if (id == 1)
            companyname = "NH Corporation";
        else if (id == 2)
            companyname = "Shah Agency";
        return companyname;
    }


    public static final String convertToJsonDateFormat(String cur_date) {

        Log.d("Passed Date : ", cur_date);
        SimpleDateFormat dateFormat = null;
        Date date = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());

//String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = format.parse(cur_date);
            System.out.println(date);
        } catch (Exception e) {
            Log.d("Convert DataFormat :: ", e.getMessage());
        }


        //Date date = new Date();

        return dateFormat.format(date);


    }


    public static final String convertToAppDateFormat(String cur_date) {

        Log.d("Passed Date : ", cur_date);
        SimpleDateFormat dateFormat = null;
        Date date = null;
        try {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault());

//String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = format.parse(cur_date);
            System.out.println(date);
        } catch (Exception e) {
            Log.d("Convert DataFormat :", e.getMessage());
        }


        //Date date = new Date();

        return dateFormat.format(date);


    }

    //Methods
    public static String convertToJsonFormat(String json_data) {

        String response = "{\"data\":" + json_data + "}";
        return response;

    }

    public static final String convertEncodedString(String str) {
        String enoded_string = null;
        try {
            enoded_string = URLEncoder.encode(str, "utf-8").replace(".", "%2E");
            enoded_string = URLEncoder.encode(str, "utf-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enoded_string;
    }

    public static String getMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }


    public static final String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());


        Date date = new Date();

        Log.d(TAG, "Current Date and Time :" + dateFormat.format(date).toString());

        return dateFormat.format(date);
    }


    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Encoded String : ", encodedImage);
        return encodedImage;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        Log.d("Decoded String   : ", "" + BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    public static final String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    /**
     * @param title
     * @param textMessage
     * @param context
     * @param notid
     */

    @SuppressLint("NewApi")
    public static void Notify(String title, String textMessage, Context context, String notid) {
        try {
            /**
             * Simple Notification
             */


            SessionManager sessionManager = new SessionManager(context);
            HashMap<String, String> userDetails = new HashMap<String, String>();
            userDetails = sessionManager.getSessionDetails();
        /*    if(userDetails.get(SessionManager.KEY_SETTINGS_NOTIFY).equals("1"))
            {
                sessionManager.setAlaramStatus("true");
                Intent startIntent = new Intent(context, RingtonePlayingService.class);
                //startIntent.putExtra("ringtone-uri", ringtoneUri);
                context.startService(startIntent);
            }
            else
            {
                sessionManager.setAlaramStatus("false");
            }*/


            //Set Vibration
            //Vibration


           /* if(userDetails.get(SessionManager.KEY_VERRIFIVATION_STATUS).equals("1"))
            {
                builder.setVibrate(new long[]{500, 500, 500, 500});
            }*/

            //BigTextStyle, InboxStyle,BigPictureStyle,MediaStyle
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


            sessionManager.setFollowupId(notid);

            Log.d(TAG, "Notofication Or FollowupID  : " + notid);
            //Set Color
            int color = context.getResources().getColor(R.color.colorPrimaryDark);
            builder.setColor(color);

            //Set Profile picture
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            builder.setLargeIcon(bitmap);


            //Set Intent to notification action
            Random r = new Random();

            int mNotificationId = r.nextInt(1000);
            mNotificationId = Integer.parseInt(notid);
            Log.d("dbhandler", "Notification Id From Method :" + mNotificationId);
            Intent notificationIntent = null;
            try {

                notificationIntent = new Intent(context,
                        FollowupResponseActivity.class);


            } catch (Exception e) {
                e.printStackTrace();
            }

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                    mNotificationId, notificationIntent, 0);


            builder.setContentIntent(resultPendingIntent);
            builder.addAction(R.drawable.icon_notification, "Show", resultPendingIntent);


            builder.setAutoCancel(true);
            /*builder.setWhen(0);*/

            builder.setContentText(textMessage);

            //Set BigText Style  Display as multiline notification
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(textMessage));

/*
            Bitmap image = null;
            try {

                URL url = new URL("https://www.delta.edu/images/gps/World.JPG");
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            //Set BigPictureStyle ,Display Image as notification
//            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));


            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title)
                    .setContentText(textMessage);
            //builder.setContentIntent(resultPendingIntent);


            Notification notification1 = builder.build();


/*
            NotificationManagerCompat.from(this).notify(mNotificationId, notification1);
            notification1.flags |= Notification.FLAG_AUTO_CANCEL;
*/


            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(mNotificationId);
            notificationManager.notify(mNotificationId, notification1);
            // notification1.flags |= Notification.FLAG_AUTO_CANCEL;


            /**
             * Complete Simple Notification
             *
             */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/
}
