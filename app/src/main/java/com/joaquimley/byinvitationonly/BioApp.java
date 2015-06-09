/*
 * GNU GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *     Copyright (c) 2015 Joaquim Ley <me@joaquimley.com>
 *
 *     This program is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation; either version 2
 *     of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.joaquimley.byinvitationonly;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CommonUtils;

import java.util.ArrayList;

/**
 * Singleton class with application shared data
 */

public class BioApp extends Activity {

    protected static int sRefreshInterval = 1000 * 60; // Default 1 minutes
    protected static final String TAG = BioApp.class.getSimpleName();
    protected static BioApp sInstance = null;
    protected static ArrayList<Session> sSessionsList = null;
    protected static ArrayList<User> sUsersList = null;
    protected static Conference sConference = null;
    protected static User sCurrentUser = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sInstance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sUsersList = null;
        sSessionsList = null;
        sCurrentUser = null;
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

    public User getCurrentUser() {
        if(sCurrentUser == null){
            User user = FileHelper.getUserFromSharedPreferences(this,
                    PreferenceManager.getDefaultSharedPreferences(this));
            if(user != null){
                sCurrentUser = user;
            }
        }
        return sCurrentUser;
    }

    public User getCurrentUser(Context context) {
        if(sCurrentUser == null){
            sCurrentUser = FileHelper.getUserFromSharedPreferences(context,
                    PreferenceManager.getDefaultSharedPreferences(context));
        }
        return sCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        sCurrentUser = currentUser;
    }

    public ArrayList<Session> getSessionsList() {
        if (sSessionsList == null) {
            sSessionsList = new ArrayList<>();
        }
        return sSessionsList;
    }

    public void setSessionList(ArrayList<Session> sessions) {
        sSessionsList = sessions;
    }

    public ArrayList<User> getUsersList() {
        if (sUsersList == null) {
            sUsersList = new ArrayList<>();
        }
        return sUsersList;
    }

    public void setUsersList(ArrayList<User> users) {
        sUsersList = users;
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

    public int getRefreshInterval() {
        return sRefreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        sRefreshInterval = refreshInterval;
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
     * Pushes dummy users to firebase ref
     *
     * @param context  self-explanatory
     * @param usersRef self-explanatory
     */
    public static void pushDummyUsersToFirebase(Context context, Firebase usersRef) {
        if (!CommonUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<User> users = createDummyUsers();
        Firebase newUserRef;
        for (User user : users) {
            newUserRef = usersRef.push();
            user.setId(newUserRef.getKey());
            newUserRef.setValue(user);
        }
    }

    /**
     * Creates dummy User objects
     *
     * @return list with dummy users
     */
    public static ArrayList<User> createDummyUsers() {
        // TODO: remove usage before production
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Evan Spiegel", "e.spiegel@snapchat.com", "CEO", "http://static5.businessinsider.com/image/539b5f6eeab8ea0266b8bbce-480/snapchat-evan-spiegel.jpg", true));
        users.add(new User("Travis Cordell", "travis@uber.com", "Entrepreneur", "https://s3.amazonaws.com/chicago_ideas_production/portraits/medium/61.jpg?1326134159", true));
        users.add(new User("Emma Watson", "emma@hogwarts.com", "Actor/Director", "http://static.dnaindia.com/sites/default/files/2015/02/23/313017-emma-watson-2.jpg", true));
        users.add(new User("Mark Zuckerberg", "mark@facebook.com", "CEO", "https://pbs.twimg.com/profile_images/1146014416/mark-zuckerberg.jpg", true));
        users.add(new User("Tom Anderson", "friend@myspace.com", "Rich has-been", "http://www.techyville.com/wp-content/uploads/2012/12/tom-myspace.jpg", true));
        users.add(new User("Sean Parker", "sean@napster.com", "Not so rich because of lawsuits has-been", "http://static.trustedreviews.com/94/000025f9d/143d_orh350w620/Napster-Logo.jpg", true));
        return users;
    }
//
//    /**
//     * Push sessions into Firebase cloud
//     *
//     * @param context     self explanatory
//     * @param sessionsRef self explanatory
//     */
//    public static void pushDummySessionsToFirebase(Context context, Firebase sessionsRef) {
//
//        if (!CommonUtils.isOnline(context)) {
//            Toast.makeText(context, context.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        ArrayList<Session> sessions = createDummySessions();
//        Firebase newSessionRef;
//        for (Session session : sessions) {
//            newSessionRef = sessionsRef.push();
//            session.setId(newSessionRef.getKey());
//            newSessionRef.setValue(session);
//        }
//    }
//
//    public static ArrayList<Session> createDummySessions() {
//        ArrayList<Session> sessions = new ArrayList<>();
////        sessions.add(new Session())
//        return sessions;
//    }

}
