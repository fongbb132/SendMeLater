package com.example.wing.ka.sendmelater;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by wing on 10/31/14.
 */
public class CancelMessage extends Activity {
    public static String TAG="cancelmessage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        if(MessageStacks.v!=null){
            MessageStacks.v.cancel();
        }if(MessageStacks.player!=null){
            MessageStacks.player.stop();
            Log.d(TAG,"can't stop");
        }
        MessageLab.get(this).deleteMessage(MessageLab.get(this).getOutgoingMessage(EditMessage.mId));
        AlarmManager alarmManager=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getActivity(this, 0, new Intent(this, TotalFragmentActivity.class), 0));
        finish();
    }
}
