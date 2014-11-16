package com.example.wing.ka.sendmelater;


import android.os.Bundle;
import android.support.v4.app.*;

/**
 * Created by wing on 8/14/14.
 */
public abstract class SingleFragmentClass extends android.support.v4.app.FragmentActivity {

    protected abstract Fragment createFragment();
    protected int getLayoutResId(){return R.layout.activity_fragment;}

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);

        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}
