/*
 * Copyright (c) 2015 Joaquim Ley
 * All rights reserved.
 *
 * Redistribution, modification or use of source and binary forms are NOT allowed
 * without permission. The name of Joaquim Ley, or joaquimley.com may not be used
 * to endorse products derived without previous authorization.
 * THIS SOFTWARE IS PROVIDED 'AS IS' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.joaquimley.byinvitationonly.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Contact;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles all firebase communications
 */

public class FirebaseHelper {


    private static final String TAG = FirebaseHelper.class.getSimpleName();

    private FirebaseHelper(){
        // No args constructor, prevent instantiating
    }

    /**
     * Initiates firebase for the app
     *
     * @param context self explanatory
     * @return firebase reference
     */
    public static Firebase initiateFirebase(Context context){
        Firebase.setAndroidContext(context);
        return new Firebase(context.getString(R.string.firebase_url));
    }


    /**
     * Change user availability on database Im Here/Invisible
     *
     * @param context self explanatory
     * @param contact self explanatory
     * @param childRef self explanatory
     * @return true if change was successful
     */
    public static boolean changeAvailabilityState(Context context, Contact contact, Firebase childRef){
        if(!BioApp.isOnline(context)){
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            return false;
        }

        if(childRef.child(contact.getEmail()) != null){
            childRef.child(contact.getEmail()).removeValue();
            Toast.makeText(context, context.getString(R.string.user_invisible), Toast.LENGTH_LONG).show();
        } else {
            Map<String, Contact> contacts = new HashMap<>();
            contacts.put(contact.getEmail(), contact);
            childRef.setValue(contacts);
            Toast.makeText(context, context.getString(R.string.user_visible), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /**
     * Creates a child reference from a root reference
     *
     * @param firebaseRef self explanatory
     * @param childName self explanatory
     * @return Firebase child reference
     */
    public static Firebase getChildRef(Firebase firebaseRef, String childName) {
        if(firebaseRef == null){
            Log.e(TAG, "Firebase Reference is null");
            return null;
        }
        return firebaseRef.child(childName);
    }
}
