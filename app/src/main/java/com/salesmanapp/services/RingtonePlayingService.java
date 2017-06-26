package com.salesmanapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

/**
 * Created by SATHISH on 01-Jun-17.
 */

public class RingtonePlayingService extends Service
{
    private MediaPlayer mp;
    private Ringtone ringtoneSound;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

       // Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
       // Ringtone defaultRingtone = RingtoneManager.getRingtone(context, defaultRintoneUri);




/*
        Uri ringtoneUri = Uri.parse(intent.getExtras().getString("ringtone-uri"));

        this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtone.play();
*/


        /*String defaultPath = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();

        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this, defaultRintoneUri);

        mp= MediaPlayer.create(this,defaultRintoneUri);


        mp.start();*/



        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneSound = RingtoneManager.getRingtone(this, ringtoneUri);

        if (ringtoneSound != null) {
            ringtoneSound.play();
        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {

        ringtoneSound.stop();

      /*  try {
            mp.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }*/
    }
}