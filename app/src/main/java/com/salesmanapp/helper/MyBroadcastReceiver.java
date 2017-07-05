package com.salesmanapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;


import com.salesmanapp.database.dbhandler;
import com.salesmanapp.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Satish Gadde on 19-09-2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    private String TAG = MyBroadcastReceiver.class.getSimpleName();
    private Ringtone ringtoneSound;

    @Override
    public void onReceive(Context context, Intent intent) {

        String defaultPath = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();

        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(context, defaultRintoneUri);


        /*mp=MediaPlayer.create(context,defaultRintoneUri);


        mp.start();*/

      /*  Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
         ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri);

        if (ringtoneSound != null) {
            ringtoneSound.play();
        }*/

      /*  Intent startIntent = new Intent(context, RingtonePlayingService.class);
        //startIntent.putExtra("ringtone-uri", ringtoneUri);
        context.startService(startIntent);*/



        Log.d("Intent Data ", "Data : " + intent.getStringExtra("SATISH") + "Alaram ID : " + intent.getStringExtra("ALARAMID"));
        /*Intent ii = new Intent(context , DisplayReminder.class);
        ii.putExtra("SATISH",intent.getStringExtra("SATISH"));
        ii.putExtra("ALARAMID" , intent.getStringExtra("ALARAMID"));
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(ii);*/
        Log.d(TAG, "Notification Data Receive: " + intent.getStringExtra("SATISH"));

        List<String> myList = new ArrayList<String>(Arrays.asList(intent.getStringExtra("SATISH").split(",")));
        Log.d(TAG, "MyList Data Notification : " + myList.toString());

        String title = myList.get(0);
        String message = myList.get(1);
        String scheduletime = myList.get(2);

        title = intent.getStringExtra("TITLE");
        message =intent.getStringExtra("MESSAGE");

        Log.d(TAG, "Current Time  : " + System.currentTimeMillis() + " Notification Time : " + scheduletime);

        long sad = System.currentTimeMillis();
      /*  if (System.currentTimeMillis() == Long.parseLong(scheduletime)) {

            String notid = intent.getStringExtra("ALARAMID");

            dbhandler.Notify(title, message, context, notid);
            Log.d(TAG, "Schedule On Time");
        } else {
            Log.d(TAG, "Not Schedule On Time");
        }*/
        String notid = intent.getStringExtra("NOTIFICATIONID");




        dbhandler.Notify(title, message, context, notid);
        //Log.d(TAG, "Schedule On Time");

        //mp.stop();

        // Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        //Log.d("MYBroadcostReceiver", "Alaram Fired");
       // stopAlaram();
    }



}