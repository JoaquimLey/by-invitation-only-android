/*
 * Copyright (c) 2015 Joaquim Ley - www.joaquimley.com
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
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.User;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Helper class, handles all Firebase communications
 */

public class FirebaseHelper {

    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static boolean mValueExists;

    private FirebaseHelper() {
        // No args constructor, prevent instantiating
    }

    /**
     * Initiates firebase for the app
     *
     * @param context self explanatory
     * @return firebase reference
     */
    public static Firebase initiateFirebase(Context context) {
        Firebase.setAndroidContext(context);
        return new Firebase(context.getString(R.string.firebase_url));
    }


    /**
     * Change user availability on database Im Here/Invisible
     *
     * @param context  self explanatory
     * @param user     self explanatory
     * @param usersRef self explanatory
     * @param listener handle callback response
     */
    public static void changeAvailabilityState(Context context, User user, Firebase usersRef, Firebase.CompletionListener listener) {

        if (user == null) {
            Log.e(TAG, context.getString(R.string.error_user_null));
            return;
        }

        if (!BioApp.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            return;
        }

        Firebase newUserRef = usersRef.push();
        if (isValueOnRef(usersRef, BioApp.getCurrentUserId())) {
            usersRef.child(BioApp.getCurrentUserId()).removeValue(listener);
            BioApp.setCurrentUserId("");
        } else {
            BioApp.setCurrentUserId(newUserRef.getKey());
            newUserRef.setValue(user, listener);
        }
    }

    /**
     * Query firebaseRef for a child value passed by @param value, sets mValueExists GLOBAL_VAR
     *
     * @param firebaseRef self explanatory
     * @param value       to be compared on ref
     */
    public static boolean isValueOnRef(final Firebase firebaseRef, String value) {

        firebaseRef.child(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mValueExists = snapshot.getValue() != null;
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
        return mValueExists;
    }

    /**
     * Retrieve or Create a child reference from root reference
     *
     * @param firebaseRef self explanatory
     * @param childName   self explanatory
     * @return Firebase child reference
     */
    public static Firebase getChildRef(Firebase firebaseRef, String childName) {
        if (firebaseRef == null) {
            Log.e(TAG, "Firebase Reference is null");
            return null;
        }
        return firebaseRef.child(childName);
    }

    public static void exportSessions(Context context, Firebase firebaseChildRef) {

        BufferedReader bufferedReader = FileHelper.getBufferedReaderFromAssets(context, context.getString(R.string.file_sessions_data));
        if (bufferedReader == null) {
            Log.e(TAG, "exportSessions(): Csv file not found");
            return;
        }

        String[] values;
        try {
            values = bufferedReader.readLine().split(context.getString(R.string.csv_split));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Firebase item = firebaseChildRef.push();

                String[] parts = line.split("\\|", -1);
                for (int i = 0; i < values.length; i++) {
                    item.child(values[i]).setValue(parts[i]);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "exportSessions(): Couldn't get bufferedReader");
            e.printStackTrace();
        }
    }

    public static User hasPersonalData(SharedPreferences sharedPreferences) {
        String name = sharedPreferences.getString("chave_nome", "");
        String email = sharedPreferences.getString("chave_email", "");

        if (!name.isEmpty() && !email.isEmpty()) {
//            return new User(sharedPreferences.getString("chave_nome", ""), sharedPreferences.getString("chave_email", ""), null);
        }
        return null;
    }
}
