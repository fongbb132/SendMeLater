package com.example.wing.ka.sendmelater;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;

import java.util.UUID;

/**
 * Created by wing on 10/31/14.
 */
public class SendMessage extends Activity{

    public static String EXTRA_ID;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        OutgoingMessage mMessage=MessageLab.get(this).getOutgoingMessage(EditMessage.mId);
        if(mMessage!=null) {
            SmsManager smsManager = SmsManager.getDefault();
            String phone = mMessage.getNumberSendingTo();
            String message = mMessage.getMessage();
            PendingIntent pendSent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_sent"), 0);
            PendingIntent pendDelivered = PendingIntent.getBroadcast(this, 0, new Intent("SMS_delivered"), 0);
            smsManager.sendTextMessage(PhoneNumberUtils.formatNumber(phone),null,message,pendSent,pendDelivered);
            //smsManager.sendTextMessage("6463091331", null, message, pendSent, pendDelivered);

            if(MessageStacks.v!=null){
                MessageStacks.v.cancel();
            }if(MessageStacks.player!=null){
                MessageStacks.player.stop();
            }

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notiicon).setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.sendmelater))
                    .setContentTitle(R.string.app_name + "").setContentTitle("Msg is sent to " + mMessage.getPerson()).setAutoCancel(true);
            PendingIntent intent2= PendingIntent.getActivity(this, 0, new Intent(this, TotalFragmentActivity.class), 0);
            mBuilder.setContentIntent(intent2);
            mBuilder.setAutoCancel(true);
            manager.notify(R.string.app_name,mBuilder.build());
            MessageLab.get(this).deleteMessage(mMessage);
        }
        AlarmManager alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getActivity(this, 0, new Intent(this, TotalFragmentActivity.class), 0));

        finish();
    }
}
