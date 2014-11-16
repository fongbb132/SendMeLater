package com.example.wing.ka.sendmelater;

import android.support.v4.app.Fragment;

/**
 * Created by wing on 8/17/14.
 */
public class ListFragmentActivity extends SingleFragmentClass {
    protected Fragment createFragment(){
        return new MessageListFragment();
    }
}
