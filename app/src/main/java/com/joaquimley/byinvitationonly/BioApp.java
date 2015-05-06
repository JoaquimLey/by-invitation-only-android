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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.joaquimley.byinvitationonly.db.DatabaseHelper;
import com.joaquimley.byinvitationonly.model.Conference;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class with application shared data
 */

public class BioApp extends Activity implements View.OnClickListener {

    protected static final String TAG = BioApp.class.getSimpleName();

    protected static BioApp sInstance = null;
    protected static DatabaseHelper sDatabase = null;
    protected static ArrayList<Session> sSessionsList = null;
    protected static ArrayList<User> sUsersList = null;
    protected static Conference sConference = null;
    protected static String sCurrentUserId = null;
    // FloatActionMenu
    private ImageView mMenuIcon;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sInstance = this;
        sDatabase = null;
        sCurrentUserId = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sDatabase != null) {
            OpenHelperManager.releaseHelper();
            sDatabase = null;
            sUsersList = null;
            sSessionsList = null;
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

    public static String getCurrentUserId() {
        if (sCurrentUserId == null) {
            return "";
        }
        return sCurrentUserId;
    }

    public static void setCurrentUserId(String currentUserId) {
        sCurrentUserId = currentUserId;
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

    /**
     * Removes the user from the instance usersList, should only be called by a "onChildRemoved()
     * listener
     *
     * @param user returned by dataSnapshot, compared with the current user list
     */
    public boolean removeUserFromList(User user) {
        int removeUserIndex = 0;
        boolean removeUserFlag = false;

        for(int i = 0; i < sUsersList.size(); i++){
            if (user.getId().equals(sUsersList.get(i).getId())) {
                removeUserIndex = i;
                removeUserFlag = true;
            }
        }

        if(removeUserFlag){
            sUsersList.remove(removeUserIndex);
        }
        return removeUserFlag;
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

    //********************************************************************************************//
    //*********************************** Floating Action Menu ***********************************//
    //********************************************************************************************//

    @Override
    public void onClick(View v) {

    }

//    /**
//     * Creates menu and sub-menu items
//     */
//    public void buildMenu() {
//        // Create Button to attach to Menu
//        mMenuIcon = new ImageView(this); // Create an icon
////        mMenuIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_cross_2));
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(mMenuIcon)
//                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
//                .build();
//
//        // Custom sub-menu
//        int actionMenuContentSize = getResources().getDimensionPixelSize(R.dimen.sub_action_button_size);
//        int actionMenuContentMargin = getResources().getDimensionPixelSize(R.dimen.sub_action_button_content_margin);
//        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
//
//        FrameLayout.LayoutParams actionMenuContentParams = new FrameLayout
//                .LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//
//        actionMenuContentParams.setMargins(actionMenuContentMargin, actionMenuContentMargin, actionMenuContentMargin,
//                actionMenuContentMargin);
//        lCSubBuilder.setLayoutParams(actionMenuContentParams);
//
//        // Set custom layout params
//        FrameLayout.LayoutParams subButtonParams = new FrameLayout.LayoutParams(actionMenuContentSize, actionMenuContentSize);
//        lCSubBuilder.setLayoutParams(subButtonParams);
//
//        // Create menu Buttons
//        ImageView mainActivityIcon = new ImageView(this);
//        ImageView participantsListIcon = new ImageView(this);
//        ImageView sessionsListIcon = new ImageView(this);
//        ImageView favouritesListIcon = new ImageView(this);
//
////        mainActivityIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_list));
////        participantsListIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_info));
////        sessionsListIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star));
////        favouritesListIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star));
//
//        SubActionButton menuSubBtnHome = lCSubBuilder.setContentView(mainActivityIcon).build();
//        SubActionButton menuSubBtnInfo = lCSubBuilder.setContentView(participantsListIcon).build();
//        SubActionButton menuSubBtnMisc = lCSubBuilder.setContentView(sessionsListIcon).build();
//        SubActionButton menuSubBtnFavourites = lCSubBuilder.setContentView(sessionsListIcon).build();
//
//        // Button Action
//        menuSubBtnHome.setOnClickListener(this);
//        menuSubBtnInfo.setOnClickListener(this);
//        menuSubBtnMisc.setOnClickListener(this);
//        menuSubBtnFavourites.setOnClickListener(this);
//
//        // Create menu with MenuButton + SubItemsButtons
//        mActionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(menuSubBtnHome)
//                .addSubActionView(menuSubBtnInfo)
//                .addSubActionView(menuSubBtnMisc)
//                .attachTo(actionButton)
//                .build();
//
//        // Listen menu open and close events to animate the button content view
//        mActionMenu.setStateChangeListener(this);
//    }
//
//    private void closeMenu() {
//        if(mActionMenu != null && mActionMenu.isOpen()){
//            mActionMenu.close(true);
//        }
//    }
//
//    @Override
//    public void onMenuOpened(FloatingActionMenu menu) {
//        // Rotate the icon of rightLowerButton 45 degrees clockwise
//        mMenuIcon.setRotation(0);
//        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
//        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(mMenuIcon, pvhR);
//        animation.start();
//    }
//
//    @Override
//    public void onMenuClosed(FloatingActionMenu menu) {
//        // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
//        mMenuIcon.setRotation(45);
//        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
//        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(mMenuIcon, pvhR);
//        animation.start();
//    }

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
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Evan Spiegel", "e.spiegel@snapchat.com", "CEO", "http://static5.businessinsider.com/image/539b5f6eeab8ea0266b8bbce-480/snapchat-evan-spiegel.jpg", true));
        users.add(new User("Travis Cordell", "travis@uber.com", "Entrepreneur", "https://s3.amazonaws.com/chicago_ideas_production/portraits/medium/61.jpg?1326134159", true));
        users.add(new User("Emma Watson", "emma@hogwarts.com", "Actor/Director", "http://static.dnaindia.com/sites/default/files/2015/02/23/313017-emma-watson-2.jpg", true));
        users.add(new User("Mark Zuckerberg", "mark@facebook.com", "CEO", "https://pbs.twimg.com/profile_images/1146014416/mark-zuckerberg.jpg", true));
        users.add(new User("Tom Anderson", "friend@myspace.com", "Rich has-been", "http://www.techyville.com/wp-content/uploads/2012/12/tom-myspace.jpg", true));
        users.add(new User("Sean Parker", "sean@napster.com", "Not so rich because of lawsuits has-been", "http://static.trustedreviews.com/94/000025f9d/143d_orh350w620/Napster-Logo.jpg", true));
        return users;
    }

    /**
     * Push sessions into Firebase cloud
     *
     * @param context     self explanatory
     * @param sessionsRef self explanatory
     */
    public static void pushDummySessionsToFirebase(Context context, Firebase sessionsRef) {
        if (!CommonUtils.isOnline(context)) {
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

    public static ArrayList<Session> createDummySessions() {
        ArrayList<Session> sessions = new ArrayList<>();
//        sessions.add(new Session("title 1", "Presenter 1", "Abstrac Abstrac Abstrac Abstrac", "Room 1", 1))
        return sessions;
    }

}
