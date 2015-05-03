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
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class, handles all Firebase communications
 */

public class FirebaseHelper {

    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static boolean mValueExists;
    private static ArrayList<User> visibleUsers;
    private static Object sessions;

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

    /**
     * Change current user's availability/visibility
     *
     * @param context  self explanatory
     * @param user     self explanatory
     * @param usersRef self explanatory
     * @param listener handle callback response
     */
    public static void changeAvailabilityState(final Context context, final Firebase usersRef, final User user,
                                               final Firebase.CompletionListener listener) {

        if (user == null) {
            Log.e(TAG, context.getString(R.string.error_user_null));
            return;
        }

        if (!BioApp.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            return;
        }

        usersRef.child(BioApp.getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null && user.getId() != null && user.isVisible()) {
                    usersRef.child(user.getId()).removeValue(listener);
                    BioApp.setCurrentUserId("");
                    user.setId(BioApp.getCurrentUserId());
                    user.setVisible(!user.isVisible());

                } else if (!user.isVisible()) {
                    Firebase newUserRef = usersRef.push();
                    BioApp.setCurrentUserId(newUserRef.getKey());
                    user.setId(BioApp.getCurrentUserId());
                    user.setVisible(true);
                    newUserRef.setValue(user, listener);

                } else {
                    user.setVisible(!user.isVisible());
                    Toast.makeText(context, "Server synchronization problem, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
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

    /**
     * Queries the server for all the visible participants
     *
     * @return arrayList with user objects
     */
    public static ArrayList<User> getVisibleUsers() {
        return visibleUsers;
    }

    public static List<Session> getSessions() {
        ArrayList<Session> sessions = new ArrayList<>();
        return sessions;
    }
}
