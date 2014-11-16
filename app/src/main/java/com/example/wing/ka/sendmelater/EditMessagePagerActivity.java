package com.example.wing.ka.sendmelater;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by wing on 8/19/14.
 */
public class EditMessagePagerActivity extends android.support.v4.app.FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<OutgoingMessage> mMessages;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mViewPager=new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mMessages=MessageLab.get(this).getMessageList();

        FragmentManager fm=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                OutgoingMessage m = mMessages.get(position);
                return EditMessage.newInstance(m.getMessageId());
            }

            @Override
            public int getCount() {
                return mMessages.size();
            }
        });

        UUID messageId=(UUID)getIntent().getSerializableExtra(EditMessage.EXTRA_ID);
        for(int i=0;i<mMessages.size();i++){
            if(mMessages.get(i).getMessageId().equals(messageId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                OutgoingMessage m=mMessages.get(position);
                if(m.getMessage()!=null){

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
