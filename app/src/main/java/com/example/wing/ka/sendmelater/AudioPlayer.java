package com.example.wing.ka.sendmelater;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

/**
 * Created by wing on 11/2/14.
 */
public class AudioPlayer {
    public static String TAG="audioplayer";
    private MediaPlayer mPlayer;
    public void stop(){
        if(mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
            Log.d(TAG,"stop wrong");
        }
    }
    public void play(Context c, Uri musicId){
        mPlayer=MediaPlayer.create(c,musicId);
        mPlayer.start();
    }

}
