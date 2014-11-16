package com.example.wing.ka.sendmelater;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MessageListFragment extends ListFragment {

    private final static String TAG="fong";
    public ArrayList<OutgoingMessage>mMessages;
    public MessageLab mMessageLab;
    public static Callbacks mCallbacks;

    public static interface Callbacks{
        void deleteMessage();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks=(Callbacks)activity;

    }

    public void onDetach(){
        super.onDetach();
        mCallbacks=null;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mMessages=MessageLab.get(getActivity()).getMessageList();
        MessageAdapter adapter = new MessageAdapter(mMessages);
        setListAdapter(adapter);
        for(OutgoingMessage c:mMessages){
            if(c.getSendingDate().getTime()<System.currentTimeMillis()){
                c.setAlarmPass(true);
            }
        }
    }


    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.message_list_item_context,menu);
    }
    
    public View onCreateView(final LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState){
        View v=super.onCreateView(inflater,parent,savedInstanceState);
        return v;
    }

    public void onListItemClick(ListView l, View v, int position, long id){
        OutgoingMessage c=((OutgoingMessage)getListAdapter().getItem(position));
        Intent i =new Intent(getActivity(),EditMessagePagerActivity.class);
        i.putExtra(EditMessage.EXTRA_ID,c.getMessageId());
        startActivity(i);
    }


    public class MessageAdapter extends ArrayAdapter<OutgoingMessage>{

        TextView time,personSendingTo;
        TextView MessageContent;
        Button remove;
        public MessageAdapter(ArrayList<OutgoingMessage>messages){
            super(getActivity(),0,messages);
        }

        public View getView(final int position,View convertView,ViewGroup parent){

            mMessageLab=MessageLab.get(getActivity());
            if (convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_view,null);
            }
            OutgoingMessage m=getItem(position);

            if(m.isAlarmPass()==false){
                remove=(Button)convertView.findViewById(R.id.delete_button);
                remove.setTextColor(Color.WHITE);
                if(android.os.Build.VERSION.SDK_INT>=16){
                    remove.setBackground(getResources().getDrawable(R.drawable.button_custom));
                }
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMessageLab.get(getActivity()).deleteMessage(getItem(position));
                        MessageLab.get(getActivity()).saveMessages();
                        MessageStacks.player.stop();
                        mCallbacks.deleteMessage();
                    }
                });
                time=(TextView)convertView.findViewById(R.id.time);
                time.setTextColor(Color.WHITE);
                String dateFormat="MMM dd yyyy HH:mm";
                String dateString= DateFormat.format(dateFormat, m.getSendingDate()).toString();
                time.setText(dateString);
                personSendingTo=(TextView)convertView.findViewById(R.id.person_sending_to);
                personSendingTo.setTextColor(Color.WHITE);
                if(m.getPerson()==null){
                    personSendingTo.setText("Not sending to anyone!");
                }else{
                    personSendingTo.setText(m.getPerson());
                }
                MessageContent=(TextView)convertView.findViewById(R.id.message_content);
                if(m.getMessage()==null){
                    MessageContent.setText(getResources().getText(R.string.empty));
                }else{
                    MessageContent.setText(m.getMessage());
                }
                MessageContent.setTextColor(Color.WHITE);
            }else if(m.isAlarmPass()==true){
                remove=(Button)convertView.findViewById(R.id.delete_button);
                remove.setTextColor(Color.GRAY);
                if(android.os.Build.VERSION.SDK_INT>=16){
                    remove.setBackground(getResources().getDrawable(R.drawable.button_custom_unable));
                }
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMessageLab.get(getActivity()).deleteMessage(getItem(position));
                        MessageLab.get(getActivity()).saveMessages();
                        mCallbacks.deleteMessage();
                    }
                });
                time=(TextView)convertView.findViewById(R.id.time);
                time.setTextColor(Color.GRAY);
                String dateFormat="MMM dd yyyy HH:mm";
                String dateString= DateFormat.format(dateFormat, m.getSendingDate()).toString();
                time.setText(dateString);
                personSendingTo=(TextView)convertView.findViewById(R.id.person_sending_to);
                personSendingTo.setTextColor(Color.GRAY);
                if(m.getPerson()==null){
                    personSendingTo.setText("Not sent to anyone!");
                }else{
                    personSendingTo.setText(m.getPerson());
                }
                MessageContent=(TextView)convertView.findViewById(R.id.message_content);
                MessageContent.setTextColor(Color.GRAY);
                if(m.getMessage()==null){
                    MessageContent.setText(R.string.empty);
                }else{
                    MessageContent.setText(m.getMessage());
                }
            }
            return convertView;
        }
    }

    public void onResume(){
        super.onResume();
        ((MessageAdapter)getListAdapter()).notifyDataSetChanged();
    }
}



