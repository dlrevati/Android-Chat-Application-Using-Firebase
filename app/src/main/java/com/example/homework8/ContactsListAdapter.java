package com.example.homework8;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by revati on 15-04-2016.
 */
public class ContactsListAdapter extends ArrayAdapter<User> {
    List<User> mData;
    Context mContext;
    int mResource;
    User self;

    ContactsListAdapter(Context context, int resource, List<User> objects, User mself) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mData = objects;
        self = mself;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        final User u = mData.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.textView_fullname);
        name.setText(u.getFullName());

        final ImageView img = (ImageView) convertView.findViewById(R.id.imageView_unread);
        img.setImageResource(R.drawable.unread);


        ImageView call = (ImageView) convertView.findViewById(R.id.imageView_call);
        call.setTag(position);

        ImageView dp = (ImageView) convertView.findViewById(R.id.imageView_dp);
        if(u.getPicture()!= null && !u.getPicture().isEmpty()) {
        byte[] decodedString = Base64.decode(u.getPicture(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        dp.setImageBitmap(decodedByte);}
        else{
            dp.setImageResource(R.drawable.contactimg);
        }

        Firebase ref = new Firebase("https://contactsapphwk8.firebaseio.com/Messages/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message m = postSnapshot.getValue(Message.class);
                    if (m.getSender().equals(u.getFullName())) {
                        if (m.getMessage_read() == false) {
                            img.setVisibility(View.VISIBLE);
                        } else {
                            img.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return convertView;

    }
}
