package com.example.homework8;

import com.firebase.client.Firebase;

/**
 * Created by revati on 14-04-2016.
 */
public class FirebaseSingleton {
    private static Firebase myFirebaseRef = new Firebase("https://contactsapphwk8.firebaseio.com/users");

    public static Firebase getFireBaseObject(){
        return myFirebaseRef;
    }
}

