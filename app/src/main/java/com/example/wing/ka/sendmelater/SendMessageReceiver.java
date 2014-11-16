package com.example.wing.ka.sendmelater;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;

import java.util.UUID;

/**
 * Created by wing on 10/31/14.
 */
public class SendMessageReceiver extends BroadcastReceiver {

    public static String EXTRA_ID;
    public void onReceive(Context context, Intent intent){
        Bundle b=intent.getExtras();
        String j =(String)b.get(EXTRA_ID);
        OutgoingMessage mMessage=MessageLab.get(context).getOutgoingMessage(UUID.fromString(j));

        if(mMessage!=null){
            SmsManager smsManager = SmsManager.getDefault();
            String phone = mMessage.getNumberSendingTo();
            String message = mMessage.getMessage();
            PendingIntent pendSent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_sent"), 0);
            PendingIntent pendDelivered = PendingIntent.getBroadcast(context, 0, new Intent("SMS_delivered"), 0);
            smsManager.sendTextMessage(PhoneNumberUtils.formatNumber(phone),null,message,pendSent,pendDelivered);
            //smsManager.sendTextMessage("6463091331", null, message, pendSent, pendDelivered);

            NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notiicon).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.sendmelater))
                    .setContentTitle(R.string.app_name + "").setContentTitle("Msg is sent to " + mMessage.getPerson());
            manager.notify(R.string.app_name,mBuilder.build());
            PendingIntent intent2= PendingIntent.getActivity(context, 0, new Intent(context, TotalFragmentActivity.class), 0);
            mBuilder.setContentIntent(intent2);
            mBuilder.setAutoCancel(true);
            if(MessageStacks.v!=null){
                MessageStacks.v.cancel();
            }if(MessageStacks.player!=null){
                MessageStacks.player.stop();
            }
        }
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getActivity(context, 0, new Intent(context, TotalFragmentActivity.class), 0));
    }
}
