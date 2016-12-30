package com.example.homework8;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    EditText name,mail,phone,pwd;
    TextView name1;
    String user;
    User u;
    ImageView img;

    String key,pic;
    String encodedImage;
    Boolean email1 = true, p1 = true;
    final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        Intent i = getIntent();
        user = i.getStringExtra("user");

        name1=(TextView)findViewById(R.id.textView_name);
        name=(EditText)findViewById(R.id.editText_name2);
        mail=(EditText)findViewById(R.id.editText_email);
        phone=(EditText)findViewById(R.id.editText_phone);
        pwd=(EditText)findViewById(R.id.editText_password);
        img=(ImageView)findViewById(R.id.imageView);
        img.setId(100);

      final Firebase myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/users");
       // FirebaseSingleton.getFireBaseObject().orderByChild("email").equals(user);
        myFirebaseRef.orderByChild("email").equals(user);
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    u= d.getValue(User.class);
                    if(u.getEmail().equals(user)) {
                        key = d.getKey();
                       // Log.d("Demokey", d.getKey());
                        name1.setText(u.getFullName());
                        name.setText(u.getFullName());
                        mail.setText(u.getEmail());
                        phone.setText(u.getPhoneNumber());
                        pwd.setText(u.getPassword());
                        //updating image
                        if(u.getPicture() != null && !u.getPicture().equals("")) {
                            byte[] decodedString = Base64.decode(u.getPicture(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            img.setImageBitmap(decodedByte);
                        } else {
                            img.setImageResource(R.drawable.contactimg);
                        }
                       //Log.d("demoUser", u.toString());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void updateClicked(View v) {

        Firebase myref = new Firebase("https://contactsapphwk8.firebaseio.com/");
       // Toast.makeText(this, "Welcome " + user, Toast.LENGTH_SHORT).show();
        Map<String, Object> updateUser = new HashMap<String, Object>();
        updateUser.put("email", mail.getText().toString().trim());
        updateUser.put("fullName", name.getText().toString().trim());
        updateUser.put("password", pwd.getText().toString().trim());
        updateUser.put("phoneNumber", phone.getText().toString().trim());
        updateUser.put("picture",pic);

        Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/users");
        ref.child(key).updateChildren(updateUser);

        if (user != u.getEmail()) {
            Log.d("demoUser", "Different email");
            myref.changeEmail(u.getEmail(), u.getPassword(), mail.getText().toString(), new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    // email changed
                    user = mail.getText().toString().trim();
                    //Log.d("demoUser", "email changed");
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    email1 = false;

                }
            });

            if (u.getPassword() != pwd.getText().toString().trim()) {
                //Log.d("demoUser", "diff pwd");
                myref.changePassword(user, u.getPassword(), pwd.getText().toString().trim(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        // password changed
                        //Log.d("demoUser", "pwd changed");

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        p1 = false;
                    }
                });
            }

        }
        if (email1 == false || p1 == false) {
            Toast.makeText(this, "Changes not saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getBaseContext(), ConversationActivity.class);
            i.putExtra("user", user);
            startActivity(i);
            finish();
        }




    }

    public void cancelClicked(View v) {
//        Intent i = new Intent(getBaseContext(), ConversationActivity.class);
//        i.putExtra("user", user);
        finish();
        //startActivity(i);
    }

    public void imageClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null!=data)
         if(data!=null)
         {
            Uri photoUri = data.getData();
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 90, stream);
                final byte[] image=stream.toByteArray();
              //  Log.d("bitmap byte array:", image.toString());
                pic = Base64.encodeToString(image, 0);
              //  Log.d("bitmap string:",pic);

            } catch (IOException e) {
                e.printStackTrace();
            }
             img.setImageBitmap(selectedImage);

            /*String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            //Log.d("demoPic",picturePath);
            cursor.close();
            img.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("demoP", encodedImage);
            u.setPicture(encodedImage);
*/

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
