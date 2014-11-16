package com.example.wing.ka.sendmelater;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by wing on 8/14/14.
 */
public class ClockFragment extends Fragment {

    private Button addButton;
    private MessageLab mMessageLab;
    public final static int REQUEST_MESSAGE=1;
    private ArrayList<OutgoingMessage> mMessage;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);;
        mMessage=MessageLab.get(getActivity()).getMessageList();
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.digital_clock,parent,false);
        mMessageLab=MessageLab.get(getActivity());
        addButton=(Button)v.findViewById(R.id.add_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutgoingMessage c=new OutgoingMessage();
                mMessageLab.get(getActivity()).addMessage(c);
                Intent i = new Intent(getActivity(),EditMessagePagerActivity.class);
                i.putExtra(EditMessage.EXTRA_ID,c.getMessageId());
                startActivityForResult(i, REQUEST_MESSAGE);
            }
        });
        return v;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent date){
        if(requestCode==REQUEST_MESSAGE){

        }
    }
}
