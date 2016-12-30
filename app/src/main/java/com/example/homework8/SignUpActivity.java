package com.example.homework8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button SignUp,cancel;
    EditText fullname,email,password,confirmPassword,phoneNumber;
    String fname,emailId,pwd,confirmpwd,phone;
    String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Stay In Touch(Sign Up)");

        fullname=(EditText)findViewById(R.id.editText_name);
        email=(EditText)findViewById(R.id.editText_email);
        password=(EditText)findViewById(R.id.editText_pwd);
        confirmPassword=(EditText)findViewById(R.id.editText_confirmPwd);
        phoneNumber=(EditText)findViewById(R.id.editText_phone);
        SignUp=(Button)findViewById(R.id.button_signUp);
        cancel=(Button)findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = fullname.getText().toString().trim();
                emailId = email.getText().toString().trim();
                pwd = password.getText().toString().trim();
                confirmpwd = confirmPassword.getText().toString().trim();
                phone = phoneNumber.getText().toString().trim();
                final Firebase myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/");
                if ((fname.length() != 0 || emailId.length() != 0 || phone.length() != 0) && pwd.length() != 0) {
                    if (pwd.equals(confirmpwd)) {
                        myFirebaseRef.createUser(emailId, pwd, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> stringObjectMap) {
                                Firebase ref = myFirebaseRef.child("users");

                                Toast.makeText(SignUpActivity.this, "Successfully created new account", Toast.LENGTH_SHORT).show();
                                //System.out.println("Successfully created user account with uid: " + stringObjectMap.get("uid"));
                                User user = new User(fname, emailId, pwd, phone);

                                Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.contactimg);
                                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                                bitmapOrg.compress(Bitmap.CompressFormat.PNG, 100, bao);
                                byte[] ba = bao.toByteArray();
                                picture = Base64.encodeToString(ba, 0);
                                Log.d("demoPP", picture);
                                user.picture = picture;
                                ref.push().setValue(user);

                                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {

                                Toast.makeText(SignUpActivity.this, "Select Different Email ID", Toast.LENGTH_SHORT).show();
                               // finish();
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
