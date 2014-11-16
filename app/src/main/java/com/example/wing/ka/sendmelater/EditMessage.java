package com.example.wing.ka.sendmelater;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.util.UUID;

public class EditMessage extends android.support.v4.app.Fragment {

    public static final String TAG="fong";
    public static final String DIALOG_DATE="date";
    public static final int REQUEST_DATE=0;
    public static boolean isDatePicker;

    public static final String DIALOG_TIME="time";
    public static final int REQUEST_TIME=0;
    public static final String EXTRA_ID="id";
    private static final int REQUEST_CONTACT=2;
    private static final int REQUEST_RINGTONE=1111;

    private Button chooseContact,sendIt,DateButton,TimeButton,ChooseRingtone;
    private CheckBox alarm;
    private TextView enterMessage,enterMessageAfter;
    private OutgoingMessage mMessage;
    public BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    public static UUID mId;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mId=(UUID)getArguments().getSerializable(EXTRA_ID);
        mMessage=MessageLab.get(getActivity()).getOutgoingMessage(mId);
    }


    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.message_activity,parent,false);
        chooseContact=(Button)v.findViewById(R.id.chooseContact);
        alarm=(CheckBox)v.findViewById(R.id.alarm_checkbox);
        ChooseRingtone=(Button)v.findViewById(R.id.choose_ringtone_button);
        if(mMessage.getRingtoneName().equals("")){
            ChooseRingtone.setText(getResources().getString(R.string.choose_ringtone_button));
        }else{
            ChooseRingtone.setText(mMessage.getRingtoneName());
        }

        //choose ringtone
        if(ChooseRingtone.isEnabled()==true){
            ChooseRingtone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,REQUEST_RINGTONE);
                    if(mMessage.getRingtone()!=null){
                        ChooseRingtone.setText(mMessage.getRingtoneName());
                    }
                }
            });
        }
        enterMessageAfter=(TextView)v.findViewById(R.id.sendmessageafter);

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mMessage.setAlarmOrNot(true);
                    enterMessageAfter.setEnabled(true);
                    ChooseRingtone.setEnabled(true);
                    enterMessageAfter.setTextColor(Color.WHITE);
                    ChooseRingtone.setTextColor(Color.WHITE);
                    if(android.os.Build.VERSION.SDK_INT>=16){
                        ChooseRingtone.setBackground(getResources().getDrawable(R.drawable.button_custom));
                    }
                    MessageLab.get(getActivity()).saveMessages();

                }else {
                    mMessage.setAlarmOrNot(false);
                    enterMessageAfter.setEnabled(false);
                    enterMessageAfter.setTextColor(Color.GRAY);
                    ChooseRingtone.setEnabled(false);
                    ChooseRingtone.setTextColor(Color.GRAY);
                    if(android.os.Build.VERSION.SDK_INT>=16){
                        ChooseRingtone.setBackground(getResources().getDrawable(R.drawable.button_custom_unable));
                    }
                    MessageLab.get(getActivity()).saveMessages();
                }
            }
        });

        if(mMessage.isAlarmOrNot()==true){
            alarm.setChecked(true);
            enterMessageAfter.setEnabled(true);
            enterMessageAfter.setTextColor(Color.WHITE);
            ChooseRingtone.setTextColor(Color.WHITE);
            if(android.os.Build.VERSION.SDK_INT>=16){
                ChooseRingtone.setBackground(getResources().getDrawable(R.drawable.button_custom));
            }
            ChooseRingtone.setEnabled(true);
        }else if(mMessage.isAlarmOrNot()==false){
            alarm.setChecked(false);enterMessageAfter.setEnabled(false);
            ChooseRingtone.setTextColor(Color.GRAY);
            enterMessageAfter.setTextColor(Color.GRAY);
            if(android.os.Build.VERSION.SDK_INT>=16){
                ChooseRingtone.setBackground(getResources().getDrawable(R.drawable.button_custom_unable));
            }
            ChooseRingtone.setEnabled(false);
        }

        if(mMessage.getPerson()==null){
            chooseContact.setText("add");
        }else{
            chooseContact.setText(mMessage.getPerson());
        }
        chooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i,REQUEST_CONTACT);
                if(mMessage.getPerson()!=null){
                    chooseContact.setText(mMessage.getPerson());
                }
            }
        });
        sendIt=(Button)v.findViewById(R.id.sendIt);
        sendIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMessage.getNumberSendingTo()!=null){
                    //goes to alarm function
                    Intent AlarmIntent=new Intent(getActivity(),MessageStacks.class);
                    AlarmIntent.putExtra(MessageStacks.EXTRA_ID,mMessage.getMessageId().toString());
                    AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent Sender=PendingIntent.getBroadcast(getActivity(),0,AlarmIntent,0);
                    Long milliseconds=mMessage.getSendingDate().getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds,Sender);

                    if(mMessage.isAlarmOrNot()){
                        //send msg after 1 min of the alarm
                        Intent sendMessage=new Intent(getActivity(),SendMessageReceiver.class);
                        sendMessage.putExtra(SendMessageReceiver.EXTRA_ID,mMessage.getMessageId().toString());
                        PendingIntent PendingMessage=PendingIntent.getBroadcast(getActivity(),0,sendMessage,0);
                        Long timeToSendMessage=milliseconds+60000;
                        alarmManager.set(AlarmManager.RTC_WAKEUP,timeToSendMessage,PendingMessage);
                    }else {
                        //send msg right at the time
                        Intent sendMessage=new Intent(getActivity(),SendMessageReceiver.class);
                        sendMessage.putExtra(SendMessageReceiver.EXTRA_ID,mMessage.getMessageId().toString());
                        PendingIntent PendingMessage=PendingIntent.getBroadcast(getActivity(),0,sendMessage,0);
                        Long timeToSendMessage=milliseconds;
                        alarmManager.set(AlarmManager.RTC_WAKEUP,timeToSendMessage,PendingMessage);
                    }
                }else if(mMessage.isAlarmOrNot()){
                    //only the alarm function
                    Intent AlarmIntent=new Intent(getActivity(),MessageStacks.class);
                    AlarmIntent.putExtra(MessageStacks.EXTRA_ID,mMessage.getMessageId().toString());
                    AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent Sender=PendingIntent.getBroadcast(getActivity(),0,AlarmIntent,0);
                    Long milliseconds=mMessage.getSendingDate().getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds,Sender);
                }
                getActivity().finish();
            }
        });
        enterMessage=(TextView)v.findViewById(R.id.message_field);
        enterMessage.setText(mMessage.getMessage());
        enterMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mMessage.setMessage(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        DateButton=(Button)v.findViewById(R.id.Date);
        updateDateButton();
        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDatePicker=true;
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMessage.getSendingDate());
                dialog.setTargetFragment(EditMessage.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        TimeButton=(Button)v.findViewById(R.id.Time);
        TimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDatePicker=false;
                android.support.v4.app.FragmentManager fm=getActivity().getSupportFragmentManager();
                DatePickerFragment timePicker=DatePickerFragment.newInstance(mMessage.getSendingDate());
                timePicker.setTargetFragment(EditMessage.this,REQUEST_TIME);
                timePicker.show(fm,DIALOG_TIME);
            }
        });
        updateTimeButton();
        return v;
    }

    public void updateTimeButton(){
        if(mMessage.getSendingDate().getTime()<System.currentTimeMillis()){
            mMessage.setAlarmPass(true);
            MessageLab.get(getActivity()).saveMessages();
        }else if(mMessage.getSendingDate().getTime()>=System.currentTimeMillis()){
            mMessage.setAlarmPass(false);
            MessageLab.get(getActivity()).saveMessages();
        }
        String timeFormat="HH:mm ";
        String timeString=DateFormat.format(timeFormat,mMessage.getSendingDate()).toString();
        TimeButton.setText(timeString);
    }

    public void onPause(){
        super.onPause();
        MessageLab.get(getActivity()).saveMessages();
        getActivity().unregisterReceiver(smsSentReceiver);
        getActivity().unregisterReceiver(smsDeliveredReceiver);
    }


    public static EditMessage newInstance(UUID messageID){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_ID,messageID);

        EditMessage fragment=new EditMessage();
        fragment.setArguments(args);
        return fragment;

    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!=Activity.RESULT_OK){
            return;
        }else if (requestCode==REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMessage.setSendingDate(date);
            updateDateButton();
            updateTimeButton();
        }else if(requestCode==REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[]queryFields=new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c =getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
            if(c.getCount()==0){
                c.close();
                return;
            }
            c.moveToFirst();
            String name=c.getString(0);
            String number=c.getString(1);
            mMessage.setPerson(name);
            mMessage.setNumberSendingTo(number);
            chooseContact.setText(mMessage.getPerson());
            c.close();
        }else if(requestCode==REQUEST_RINGTONE) {
            Uri ringtoneUri=data.getData();
            String[]query=new String[]{MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,};
            Cursor cursor1=getActivity().getContentResolver().query(ringtoneUri,query,null,null,null);
            if(cursor1.getCount()==0){
                cursor1.close();
                return;
            }
            cursor1.moveToFirst();
            mMessage.setRingtone(ringtoneUri+"");
            mMessage.setRingtoneName(cursor1.getString(1));
            ChooseRingtone.setText(mMessage.getRingtoneName());
            cursor1.close();
        }
    }

    public void onResume(){
        super.onResume();
        smsSentReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity().getBaseContext(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity().getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity().getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity().getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity().getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        smsDeliveredReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity().getBaseContext(),"SMS Delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity().getBaseContext(),"SMS not delivered",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        getActivity().registerReceiver(smsSentReceiver,new IntentFilter("SMS_SENT"));
        getActivity().registerReceiver(smsDeliveredReceiver,new IntentFilter("SMS_DELIVERED"));
    }

    public void updateDateButton(){
        String dateFormat="MMM dd yyyy";
        String dateString= DateFormat.format(dateFormat,mMessage.getSendingDate()).toString();
        DateButton.setText(dateString);
    }

}
