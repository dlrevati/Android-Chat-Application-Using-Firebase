package com.example.homework8;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewMessages extends AppCompatActivity {
    Firebase myFirebaseRef;
    User user;
    String sender,key;
    Button send;
    EditText msgTxt;
    ListView lv;
    final ArrayList<Message> msgList = new ArrayList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        Intent i = getIntent();
        user = i.getParcelableExtra("user");
        sender = i.getStringExtra("fromUser");
        getSupportActionBar().setTitle(user.getFullName());

        send=(Button)findViewById(R.id.button_sendMsg);
        msgTxt=(EditText)findViewById(R.id.editText_messageText);
        lv=(ListView)findViewById(R.id.listView_message);

        myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/Messages");
        myFirebaseRef.orderByChild("receiver").equals(user.getFullName());
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.d("key", d.getKey());
                    key = d.getKey();
                    Message m = d.getValue(Message.class);
                    if (m.getReceiver().equals(sender) && m.getSender().equals(user.fullName) || m.getReceiver().equals(user.fullName) && m.getSender().equals(sender)) {
                        Log.d("demoM", m.toString());
                        msgList.add(m);
                        m.id = key;
                    }

                }
                MessageListAdapter msgDataAdapter = new MessageListAdapter(getBaseContext(), R.layout.adapter_messagelist, msgList, sender);
                msgDataAdapter.setNotifyOnChange(true);
                lv.setAdapter(msgDataAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgTxt.getText().toString();
                if (msg.length() == 0) {
                    Toast.makeText(ViewMessages.this, "Enter A Message ", Toast.LENGTH_SHORT).show();
                } else if (msg.length() > 140) {
                    Toast.makeText(ViewMessages.this, "Message text cannot exceed 140 characters", Toast.LENGTH_SHORT).show();
                } else {
                    String finalDate;
                    //Calendar cal = Calendar.getInstance();
                    Long date = System.currentTimeMillis();
                    SimpleDateFormat month_date = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                    finalDate = month_date.format(date);
                    Log.d("date",finalDate);
                    Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/Messages");
                    Message m = new Message();
                    m.setReceiver(user.getFullName());
                    m.setSender(sender);
                    m.setMessage_text(msg);
                    m.setTime_Stamp(finalDate);
                    //ref.push().setValue(m);
                    msgTxt.setText("");

                    myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/Messages");
                    myFirebaseRef.orderByChild("receiver").equals(user.getFullName());
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            msgList.clear();
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                key = d.getKey();
                                Message e = d.getValue(Message.class);
                                if (e.getReceiver().equals(sender) && e.getSender().equals(user.fullName) || e.getReceiver().equals(user.fullName) && e.getSender().equals(sender)) {
                                    // Log.d("demoMsg", e.toString());
                                    msgList.add(e);
                                    e.id = key;
                                }
                            }
                            MessageListAdapter msgDataAdapter = new MessageListAdapter(getBaseContext(), R.layout.adapter_messagelist, msgList, sender);
                            msgDataAdapter.setNotifyOnChange(true);
                            lv.setAdapter(msgDataAdapter);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    Toast.makeText(ViewMessages.this, "Message sent", Toast.LENGTH_SHORT).show();
                    ref.push().setValue(m);

                }

            }

        });

    }
    public void deleteClicked(View v)
    {
        Message m = (Message)v.getTag();
        Log.d("pushId",m.toString());
        Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/Messages/"+m.id);
        ref.setValue(null);

        myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/Messages");
        myFirebaseRef.orderByChild("receiver").equals(user.getFullName());
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    key = d.getKey();

                    Message e = d.getValue(Message.class);
                    if (e.getReceiver().equals(sender) && e.getSender().equals(user.fullName) || e.getReceiver().equals(user.fullName) && e.getSender().equals(sender)) {
                       // Log.d("demo", e.toString());
                        e.id = key;
                        msgList.add(e);
                    }
                }
                MessageListAdapter msgDataAdapter = new MessageListAdapter(getBaseContext(), R.layout.adapter_messagelist, msgList, sender);
                msgDataAdapter.setNotifyOnChange(true);
                lv.setAdapter(msgDataAdapter);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.view_contact) {
            Intent i = new Intent(ViewMessages.this,ViewContact.class);
            i.putExtra("user", user);
            startActivity(i);
        } else if (id == R.id.call_contact) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + user.getPhoneNumber()));

            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getBaseContext(), "Call not connected", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

