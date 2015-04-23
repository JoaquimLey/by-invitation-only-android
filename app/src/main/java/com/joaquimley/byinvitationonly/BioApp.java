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

package com.joaquimley.byinvitationonly;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.joaquimley.byinvitationonly.db.DatabaseHelper;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class with application shared data
 */

public class BioApp extends Activity {

    protected static final String TAG = BioApp.class.getSimpleName();

    protected static BioApp sInstance = null;
    protected static DatabaseHelper sDatabase = null;
    protected static ArrayList<Session> sSessionsList = null;
    protected static Conference sConference = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sInstance = this;
        sDatabase = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sDatabase != null) {
            OpenHelperManager.releaseHelper();
            sDatabase = null;
        }
    }

    /**
     * Create if not exist, singleton instance of "BioApp"
     *
     * @return BioApp.class instance
     */
    public static BioApp getInstance() {
        if (sInstance == null) {
            sInstance = new BioApp();
        }
        return sInstance;
    }

    public ArrayList<Session> getSessionList() {
        if (sSessionsList == null) {
            sSessionsList = new ArrayList<>();
        }
        return sSessionsList;
    }

    public void setSessionList(ArrayList<Session> sessions) {
        sSessionsList = sessions;
    }

    public Conference getConference() {
        if (sConference == null) {
            sConference = new Conference();
        }
        return sConference;
    }

    public void setConference(Conference conference) {
        sConference = conference;
    }

    /**
     * Check device connection status
     *
     * @param context self explanatory
     * @return boolean result
     */
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();

        } catch (Exception e) {
            Log.e(TAG, "Unable to fetch device connection status", e);
            return false;
        }
    }

    /**
     * Get application local database, create if not exists
     *
     * @param context self explanatory
     * @return DatabaseHelper current database
     */
    public DatabaseHelper getDb(Context context) {
        if (sDatabase == null) {
            sDatabase = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return sDatabase;
    }

    public void setDb(DatabaseHelper dataBase) {
        sDatabase = dataBase;
    }

    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    //********************************** Debug/Dummy methods **********************************//
    // TODO: Delete or comment before pushing to production, ensure there are 0 (ZERO) usages

    /**
     * Push sessions into Firebase cloud
     *
     * @param context self explanatory
     * @param sessionsRef self explanatory
     */
    public static void pushDummySessionsToFirebase(Context context, Firebase sessionsRef) {
        if(!isOnline(context)){
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Session> sessionList = new ArrayList<>();

//        sessionList.add(new Session("Keynote: " + "On the State of Software Engineering", "Dr. James T. Kirk", ))


        Map<String, Session> sessions = new HashMap<>();
        sessions.put(sessionList.get(0).getTitle(), sessionList.get(0));
        sessions.put(sessionList.get(1).getTitle(), sessionList.get(1));
        sessions.put(sessionList.get(2).getTitle(), sessionList.get(2));
        sessions.put(sessionList.get(3).getTitle(), sessionList.get(3));

        sessionsRef.setValue(sessions);
    }

    public static void pushDummyUsersToFirebase(Context context, Firebase usersRef, User myUser){

        if(!isOnline(context)){
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<User> users = createDummyUsers();
        users.add(myUser);

        Map<String, User> usersMap = new HashMap<>();
        usersMap.put(users.get(0).getName(), users.get(0));
        usersMap.put(users.get(1).getName(), users.get(1));
        usersMap.put(users.get(2).getName(), users.get(2));
        usersMap.put(users.get(3).getName(), users.get(3));
        usersMap.put(users.get(4).getName(), users.get(4));
        usersMap.put(users.get(5).getName(), users.get(5));

        usersRef.setValue(usersMap);
    }

    public static ArrayList<User> createDummyUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Emma Watson", "emma@hogwarts.com", "Actor/Director", "http://static.dnaindia.com/sites/default/files/2015/02/23/313017-emma-watson-2.jpg"));
        users.add(new User("Mark Zuckerberg", "mark@facebook.com", "CEO", "https://pbs.twimg.com/profile_images/1146014416/mark-zuckerberg.jpg"));
        users.add(new User("Tom Anderson", "friend@myspace.com", "Rich has-been", "http://www.techyville.com/wp-content/uploads/2012/12/tom-myspace.jpg"));
        users.add(new User("Sean Parker", "sean@napster.com", "Not so rich because of lawsuits has-been", "http://static.trustedreviews.com/94/000025f9d/143d_orh350w620/Napster-Logo.jpg"));
        return users;
    }
}
