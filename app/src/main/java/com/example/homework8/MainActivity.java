package com.example.homework8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends AppCompatActivity {

    EditText uname,pwd;
    String username,password,val;
    Button login,newUser;
    AuthData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Stay In Touch(Login)");
        uname=(EditText)findViewById(R.id.editText_username);
        pwd=(EditText)findViewById(R.id.editText_pwd);
        login=(Button)findViewById(R.id.button_submit);
        newUser=(Button)findViewById(R.id.button_createUser);

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/");

        myFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {

                if (authData != null) {
                    data = authData;
                    val=String.valueOf(data.getProviderData());
                    int i=val.indexOf("=");
                    int j=val.indexOf(",");
                    val=val.substring(i+1,j);
                 //   Log.d("demoD",data.toString());
                 //   Log.d("demo", "In session");

                } else {
                    Log.d("demo", "logged out of session");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = uname.getText().toString().trim();
                password = pwd.getText().toString().trim();
                myFirebaseRef.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        uname.setText("");
                        pwd.setText("");
                        Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                        intent.putExtra("user",val);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                        Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                        uname.setText("");
                        pwd.setText("");
                    }
                });

            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                //finish();
            }
        });
    }
}
