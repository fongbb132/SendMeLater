package com.example.wing.ka.sendmelater;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.UUID;

/**
 * Created by wing on 10/20/14.
 */
public class MessageStacks extends BroadcastReceiver {

    public static Vibrator v;
    public static AudioPlayer player=new AudioPlayer();
    public static String EXTRA_ID;
    public void onReceive(Context context, Intent intent){
        Bundle b=intent.getExtras();
        String j =(String)b.get(EXTRA_ID);
        OutgoingMessage mMessage=MessageLab.get(context).getOutgoingMessage(UUID.fromString(j));

        if(mMessage.isAlarmOrNot()&&mMessage.getRingtone()!=null){
            Intent intent3=new Intent(context,CancelMessage.class);

            NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notiicon).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.sendmelater))
                    .setContentTitle(R.string.app_name + "").setContentTitle("ALARM!" ).addAction(0, "Cancel Alarm", PendingIntent.getActivity(context,0,intent3,0))
                    ;

            if(mMessage.isAlarmOrNot()){
                v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000},10);
                if(mMessage.getRingtone()!=null){
                    player.play(context, Uri.parse(mMessage.getRingtone()));
                    AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
                }
              }
            PendingIntent intent2= PendingIntent.getActivity(context, 0, new Intent(context, TotalFragmentActivity.class), 0);
            mBuilder.setContentIntent(intent2);
            mBuilder.setAutoCancel(true);
            manager.notify(R.string.app_name, mBuilder.build());
        }

        if(mMessage!=null&&mMessage.getPerson()!=null){
            String person=mMessage.getPerson();
            Intent intent1=new Intent(context,SendMessage.class);
            intent1.putExtra(SendMessage.EXTRA_ID,mMessage.getMessageId().toString());
            Intent intent3=new Intent(context,CancelMessage.class);

            NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notiicon).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.sendmelater))
                    .setContentTitle(R.string.app_name + "").setContentTitle("Msg is sending to " + person).addAction(0, "Cancel Message", PendingIntent.getActivity(context,0,intent3,0))
                    .addAction(0, "Send it now",PendingIntent.getActivity(context,0,intent1,0) );

            if(mMessage.isAlarmOrNot()){
                v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000},10);
                if(mMessage.getRingtone()!=null){
                    player.play(context, Uri.parse(mMessage.getRingtone()));
                    AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
                }
            }

            PendingIntent intent2= PendingIntent.getActivity(context, 0, new Intent(context, TotalFragmentActivity.class), 0);
            mBuilder.setContentIntent(intent2);
            mBuilder.setAutoCancel(true);
            manager.notify(R.string.app_name, mBuilder.build());
        }
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getActivity(context, 0, new Intent(context, TotalFragmentActivity.class), 0));
    }
}
