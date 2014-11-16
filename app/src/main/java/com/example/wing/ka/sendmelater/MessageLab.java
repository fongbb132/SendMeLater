package com.example.wing.ka.sendmelater;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by wing on 8/14/14.
 */
public class MessageLab {
    private final static String TAG="MessageLab";
    private final static String FILENAME="message.json";

    public ArrayList<OutgoingMessage> MessageList;
    private static MessageLab sMessageLab;
    private MessageListJSONSerializer mSerializer;

    private Context mAppContext;

    public MessageLab(Context appContext){
        mAppContext=appContext;
       mSerializer=new MessageListJSONSerializer(mAppContext,FILENAME);
        try{
            MessageList=mSerializer.loadMessages();
        }catch(Exception e){
            MessageList=new ArrayList<OutgoingMessage>();
            Log.e(TAG,"Error loading list",e);
        }
    }

    public OutgoingMessage getDeleteMessage(int a ){
        OutgoingMessage b=new OutgoingMessage();
        for(int i = 0; i < MessageList.size();i++){
            if(i==a){
                b=MessageList.get(i);
            }
        }
        return b;
    }

    public void addMessage(OutgoingMessage message){
        MessageList.add(message);
        saveMessages();
    }

    public OutgoingMessage getOutgoingMessage(UUID id){
        for(OutgoingMessage m:MessageList){
            if(m.getMessageId().equals(id)){
                return m;
            }
        }
        return null;
    }

    public ArrayList<OutgoingMessage> getMessageList(){
        if(sMessageLab==null){
            sMessageLab=new MessageLab(mAppContext.getApplicationContext());
        }
        return MessageList;}

    public void deleteMessage(OutgoingMessage m){
        MessageList.remove(m);
    }

    public static MessageLab get(Context c){
        if(sMessageLab==null){
            sMessageLab=new MessageLab(c.getApplicationContext());
        }
        return sMessageLab;
    }
    public boolean saveMessages(){
        try{
            mSerializer.saveMessages(MessageList);
            Log.d(TAG,"Message saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving Messages");
            return false;
        }
    }

}
