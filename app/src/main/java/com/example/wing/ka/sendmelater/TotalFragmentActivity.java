package com.example.wing.ka.sendmelater;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * Created by wing on 8/14/14.
 */
public class TotalFragmentActivity extends android.support.v4.app.FragmentActivity implements MessageListFragment.Callbacks{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MessageStacks.v!=null){
            MessageStacks.v.cancel();
            MessageStacks.player.stop();
        }
        setContentView(R.layout.activity_fragment);
        getActionBar().hide();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new ClockFragment();
        Fragment fragment2=new MessageListFragment();

        fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        fm.beginTransaction().add(R.id.listPeople,fragment2).commit();
    }

    public void deleteMessage(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        ft.remove(fm.findFragmentById(R.id.listPeople));
        ft.add(R.id.listPeople,new MessageListFragment());
        ft.commit();
    }

}
