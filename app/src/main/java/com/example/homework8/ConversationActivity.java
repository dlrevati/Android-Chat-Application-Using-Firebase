package com.example.homework8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {
    Firebase myFirebaseRef;
    String username, fromName;
    ListView lv;
    User self;
    ArrayList<Message> msgList;
    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        msgList = new ArrayList<Message>();
        msgList.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getSupportActionBar().setTitle("Stay In Touch");
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/");
        Intent i = getIntent();
        username = i.getStringExtra("user");


        final Firebase myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/users");
        myFirebaseRef.orderByChild("email").equals(username);
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    // m=d.getValue(Message.class);
                    if (username.equals(u.getEmail())) {
                        fromName = u.getFullName();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        userList = new ArrayList<User>();
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/Messages");
                ref.orderByChild("sender").equals(user.getFullName());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Message msg = postSnapshot.getValue(Message.class);
                            if (msg.getMessage_read() == false) {
                                msg.setMessage_read(true);
                            }
                        }
                        ContactsListAdapter adapter = new ContactsListAdapter(getBaseContext(), R.layout.adapter_contactslist, userList, self);
                        adapter.setNotifyOnChange(true);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                //  Toast.makeText(getBaseContext(), "Position " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ConversationActivity.this, ViewMessages.class);
                i.putExtra("user", userList.get(position));
                i.putExtra("fromUser", fromName);
                startActivity(i);

            }
        });

        Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //   Log.d("demo_snap", postSnapshot.getValue().toString());
                    User u = postSnapshot.getValue(User.class);

                    if (!u.getEmail().equals(username)) {
                        userList.add(u);
                    } else {
                        self = u;
                    }
                }
                ContactsListAdapter adapter = new ContactsListAdapter(getBaseContext(), R.layout.adapter_contactslist, userList, self);
                adapter.setNotifyOnChange(true);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(ConversationActivity.this, EditProfileActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);

                break;
            case R.id.log_out:
                myFirebaseRef.unauth();
                Intent intent1 = new Intent(ConversationActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();

        }
        return true;
    }

    public void callNumber(View v) {
        int u = (Integer) v.getTag();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + userList.get(u).getPhoneNumber()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}
