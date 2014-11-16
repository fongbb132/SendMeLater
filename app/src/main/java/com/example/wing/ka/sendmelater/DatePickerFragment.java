package com.example.wing.ka.sendmelater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wing on 8/23/14.
 */
public class DatePickerFragment extends DialogFragment {
    public final static String EXTRA_DATE="com.ka.wing.fong.date";
    public static int year,month,day,hour,min;
    private Date mDate;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        mDate=(Date)getArguments().getSerializable(EXTRA_DATE);
        View v=new View(getActivity());
        int title=0;

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);

        if(EditMessage.isDatePicker==true) {
            v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
            title=R.string.date_picker_title;

            DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);

            datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                    year = i;
                    month = i2;
                    day = i3;
                    mDate = new GregorianCalendar(year, month, day, hour, min).getTime();
                    getArguments().putSerializable(EXTRA_DATE, mDate);
                }
            });

        }else {
            v = getActivity().getLayoutInflater().inflate(R.layout.time_picker, null);
            title=R.string.time_picker_title;

            TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_timePicker);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);

            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                    hour=i;
                    min=i2;
                    mDate=new GregorianCalendar(year,month,day,hour,min).getTime();
                    mDate.setHours(hour);
                    mDate.setMinutes(min);
                    getArguments().putSerializable(EXTRA_DATE, mDate);
                }
            });
        }
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(title).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();

    }

    public static DatePickerFragment newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_DATE,date);

        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent i =new Intent();
        i.putExtra(EXTRA_DATE,mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
