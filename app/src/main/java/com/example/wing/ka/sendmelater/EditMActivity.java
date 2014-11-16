package com.example.wing.ka.sendmelater;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by wing on 8/17/14.
 */
public class EditMActivity extends SingleFragmentClass {
    protected Fragment createFragment(){
        UUID messageId=(UUID)getIntent().getSerializableExtra(EditMessage.EXTRA_ID);
        return EditMessage.newInstance(messageId);
    }

}
