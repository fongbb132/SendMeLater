package com.example.wing.ka.sendmelater;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wing on 8/14/14.
 */
public class OutgoingMessage {
    private UUID messageId;
    private String person;
    private String message;
    private Date sendingDate;
    private String numberSendingTo;
    private String Ringtone;
    private String RingtoneName="";
    private boolean alarmOrNot;
    private boolean alarmPass;
    private static final String JSON_ID="id";
    private static final String JSON_CONTACT="person";
    private static final String JSON_MESSAGE="message";
    private static final String JSON_DATE="date";
    private static final String JSON_NUMBER="number";
    private static final String JSON_ALARMORNOT="false";
    private static final String JSON_ISALARMPASS="false";
    private static final String JSON_RINGTONE="ringtone";
    private static final String JSON_RINGTONENAME="ringtonename";

    public Date getSendingDate() {
        return sendingDate;
    }

    public String getRingtoneName() {
        return RingtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        RingtoneName = ringtoneName;
    }

    public boolean isAlarmPass() {
        return alarmPass;
    }

    public void setAlarmPass(boolean alarmPass) {
        this.alarmPass = alarmPass;
    }

    public boolean  isAlarmOrNot() {
        return alarmOrNot;
    }

    public void setAlarmOrNot(boolean alarmOrNot) {
        this.alarmOrNot = alarmOrNot;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }

    public OutgoingMessage(){
        messageId=UUID.randomUUID();
        sendingDate=Calendar.getInstance().getTime();
        alarmOrNot=false;
        alarmPass=false;
        RingtoneName="";
    }

    public String getMessage() {
        return message;
    }

    public String getNumberSendingTo() {
        return numberSendingTo;
    }

    public void setNumberSendingTo(String numberSendingTo) {
        this.numberSendingTo = numberSendingTo;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPerson(String  person) {
        this.person = person;
    }

    public String getPerson() {
        return person;
    }

    public String getRingtone() {
        return Ringtone;
    }

    public void setRingtone(String ringtone) {
        Ringtone = ringtone;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json=new JSONObject();
        json.put(JSON_ID, messageId.toString());
        json.put(JSON_CONTACT,person);
        json.put(JSON_MESSAGE,message);
        json.put(JSON_DATE,sendingDate.getTime());
        json.put(JSON_NUMBER,numberSendingTo);
        json.put(JSON_ALARMORNOT,alarmOrNot);
        json.put(JSON_ISALARMPASS,alarmPass);
        json.put(JSON_RINGTONE,Ringtone);
        json.put(JSON_RINGTONENAME,RingtoneName);
        return json;
    }

    public OutgoingMessage(JSONObject json)throws JSONException{
        messageId=UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_CONTACT)){
            person=json.getString(JSON_CONTACT);
        }if(json.has(JSON_MESSAGE)){
            message=json.getString(JSON_MESSAGE);
        }if (json.has(JSON_DATE)){
            sendingDate=new Date(json.getLong(JSON_DATE));
        }if(json.has(JSON_NUMBER)){
            numberSendingTo=json.getString(JSON_NUMBER);
        }if (json.has(JSON_ALARMORNOT)){
            alarmOrNot=json.getBoolean(JSON_ALARMORNOT);
        }if(json.has(JSON_ALARMORNOT)){
            alarmPass=json.getBoolean(JSON_ISALARMPASS);
        }if(json.has(JSON_RINGTONE)){
            Ringtone=json.getString(JSON_RINGTONE);
        }if(json.has(JSON_RINGTONENAME)){
            RingtoneName=json.getString(JSON_RINGTONENAME);
        }
    }
}
