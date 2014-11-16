package com.example.wing.ka.sendmelater;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class MessageListJSONSerializer {

    private Context mContext;
    private String mFilename;

    public MessageListJSONSerializer(Context c,String f){
        mContext=c;
        mFilename=f;
    }

    public ArrayList<OutgoingMessage>loadMessages()throws IOException,JSONException{
        ArrayList<OutgoingMessage>messages=new ArrayList<OutgoingMessage>();
        BufferedReader reader=null;
        try{
            InputStream in = mContext.openFileInput(mFilename);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++) {
                messages.add(new OutgoingMessage(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null)
                reader.close();
        }
        return messages;
    }

    public void saveMessages(ArrayList<OutgoingMessage> messages) throws JSONException, IOException {
        // build an array in JSON
        JSONArray array = new JSONArray();
        for (OutgoingMessage c : messages)
            array.put(c.toJSON());

        // write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
