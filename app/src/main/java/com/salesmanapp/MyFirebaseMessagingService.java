package com.salesmanapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.salesmanapp.activity.DashBoardActivity;
import com.salesmanapp.database.dbhandler;
import com.salesmanapp.session.SessionManager;


import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by Satish Gadde on 02-09-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SessionManager sessionamanger;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        try {
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("notification"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG , "Error FCM : "+e.getMessage());
        }
        Log.i(TAG, "Received message");

        sessionamanger = new SessionManager(this);
        userDetails = sessionamanger.getSessionDetails();

        dbhandler db = new dbhandler(this);
        SQLiteDatabase sd = db.getWritableDatabase();




        String message = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String notification_id = remoteMessage.getData().get("notificationid");
        String img_url = remoteMessage.getData().get("image");
        String type=remoteMessage.getData().get("type");


        System.out.println("MESSAGE : " + message + " TITLE :" + title+" NotificationID : "+notification_id+" img_url :"+img_url+"type: "+type);

        Log.d("Message : " + message, "Title : " + title);
        String cdate = getDateTime();

        String query = "delete from Notification_Mst where notification ='" + message + "' and ndate='" + cdate + "'";
        Log.d("Notifi Delete Query : ", query);
        sd.execSQL(query);
        query = "insert into Notification_Mst values(null,'" + title
                + "','" + message + "','" + cdate + "')";
        Log.d(TAG,"Notifi Insert Query : "+ query);

        sd.execSQL(query);

        System.out.print("Current Date : " + cdate);
        System.out.print("Not Query : " + query);

        String dismessage = title + "GSS" + message;
        //displayMessage(context, message );

        // notifies user

        // dbhandler db=new dbhandler(context);
        // SQLiteDatabase sd=db.getWritableDatabase();
        // sd.execSQL("INSERT into notificationmsg values('','"+message+"')");
        //displayMessage(context, message);
        // notifies user
        // generateNotification(context, message);
        sendNotification( message , title , notification_id , img_url ,type);

    }

    private void sendNotification(String message, String title, String notificationid, String imgurl, String type) {


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        String textMessage = ""+message;

        //Set Sound
        builder.setSound(
                RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));



        builder.setVibrate(new long[]{300, 300, 200, 300});




        //Set Color
        int color = getResources().getColor(R.color.colorPrimaryDark);
        builder.setColor(color);

        //Set Profile picture
        Drawable drawable = this.getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        builder.setLargeIcon(bitmap);


        //Set Intent to notification action
        int mNotificationId = 001;
        /*Intent notificationIntent = new Intent(this,
                NotificationActivity.class);*/

        Intent notificationIntent = new Intent(this,
                DashBoardActivity.class);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(R.drawable.ic_eye, "Show", resultPendingIntent);
        builder.setAutoCancel(true);

        //Set BigText Style  Display as multiline notification
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(textMessage));


        //Set BigPictureStyle ,Display Image as notification
        Bitmap image = null;
        if(type.equals("image"))
        {
            try {
                URL url = new URL(imgurl);
                Log.d("Image URL : ", ""+imgurl);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title)
                .setContentText(textMessage);

        Notification notification1 = builder.build();

        NotificationManagerCompat.from(this).notify(Integer.parseInt(notificationid), notification1);




    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }

}
