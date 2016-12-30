package com.example.homework8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    TextView full_name,name,phone,email;
    ImageView contactImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        getSupportActionBar().setTitle("View Contact");
        full_name = (TextView) findViewById(R.id.textView_fullcontactName);
        name = (TextView) findViewById(R.id.textView_Contactname);
        phone = (TextView) findViewById(R.id.textView_contactPhone);
        email = (TextView) findViewById(R.id.textView_contactemail);
        contactImg=(ImageView)findViewById(R.id.imageView_conatctImg);

        Intent i =getIntent();
        User e = i.getParcelableExtra("user");
        full_name.setText(e.getFullName());
        name.setText(e.getFullName());
        phone.setText(e.getPhoneNumber());
        email.setText(e.getEmail());
        byte[] decodedString = Base64.decode(e.picture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        contactImg.setImageBitmap(decodedByte);
    }
}
