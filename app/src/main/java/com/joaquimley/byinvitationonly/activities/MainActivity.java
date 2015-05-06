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

package com.joaquimley.byinvitationonly.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomSessionListAdapter;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.helper.IntentHelper;
import com.joaquimley.byinvitationonly.interfaces.FavoriteChangeListener;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerCallbacks;
import com.joaquimley.byinvitationonly.util.CommonUtils;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MainActivity extends BaseActivity implements PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener,
        Firebase.CompletionListener, View.OnClickListener, ChildEventListener, FavoriteChangeListener, NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Firebase mUsersRef;
    private Firebase mSessionsRef;
    private ImageButton mBtnStatus;
    private PullRefreshLayout mPullRefreshLayout;
    private SharedPreferences mSharedPreferences;
    private CustomSessionListAdapter mCustomAdapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        BioApp.getInstance().setConference(FileHelper.importConferenceDataFromFile(this));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUser = FileHelper.getUserFromSharedPreferences(this, mSharedPreferences);
        setTitle(getString(R.string.text_welcome_to) + " " + BioApp.getInstance().getConference().getAcronym());
        init();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Initialize Firebase references, UI elements, listeners
     */
    private void init() {
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mSessionsRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_sessions));
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_users));
        if (mUsersRef != null) {
            mUsersRef.addChildEventListener(this);
        }
        // UI
        loadProfileInfo();
        findViewById(R.id.ib_user_edit).setOnClickListener(this);
        mBtnStatus = (ImageButton) findViewById(R.id.ib_user_status);
        mBtnStatus.setOnClickListener(this);
        CommonUtils.changeStatusIcon(mUser, mBtnStatus);
        ((TextView) findViewById(R.id.tv_up_coming_sessions)).setText(getString(R.string.text_up_coming_sessions));
        // List
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        ListView mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
//        mCustomAdapter = new CustomSessionListAdapter(MainActivity.this, FirebaseHelper.getSessions(), this);
        mList.setAdapter(mCustomAdapter);
    }

    /**
     * Updates header info with SharedPreferences data
     */
    private void loadProfileInfo() {
        if (mUser != null) {
            BioApp.setCurrentUserId(mUser.getId());
            ((TextView) findViewById(R.id.tv_user_name)).setText(mUser.getName());
            ((TextView) findViewById(R.id.tv_user_email)).setText(mUser.getEmail());
            ((TextView) findViewById(R.id.tv_user_description)).setText(mUser.getDescription());

            if (!mUser.getPhotoBase64().isEmpty() && mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), "") != null) {
                Picasso.with(this).load(mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), ""))
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder_error)
                        .transform(new ImageCircleTransform())
                        .into((ImageView) findViewById(R.id.iv_user_pic));
            } else {
                Picasso.with(this).load(R.drawable.image_placeholder).into((ImageView) findViewById(R.id.iv_user_pic));
            }
            return;
        }

        ((TextView) findViewById(R.id.tv_user_name)).setText("Create Your Profile");
        ((TextView) findViewById(R.id.tv_user_email)).setText("Use the edit button");
        ((TextView) findViewById(R.id.tv_user_description)).setText("To share your details and see other participants. Start networking! :)");
        Picasso.with(this).load(R.drawable.image_placeholder).into((ImageView) findViewById(R.id.iv_user_pic));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Toast.makeText(this, "Item clicked: " + position, Toast.LENGTH_SHORT).show();
        switch (position) {

            case 0:
                if(mNavigationDrawerFragment != null && mNavigationDrawerFragment.isDrawerOpen()){
                    mNavigationDrawerFragment.closeDrawer();
                }
                break;

            case 1:
                startActivity(IntentHelper.createSessionListActivityIntent(this));
                break;

            case 2:
                if (mUser == null) {
                    Log.w(TAG, "User is null");
                    return;
                }

                if (!CommonUtils.isOnline(this)) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mUser.isVisible()) {
                    Toast.makeText(this, getString(R.string.error_user_must_check_in), Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(IntentHelper.createParticipantsListIntent(this));
                break;

            case 3:
                startActivity(IntentHelper.createBookmarksActivity(this));
                break;

            default:
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

//            case R.id.ic_menu_group:
//                if (mUser == null) {
//                    Toast.makeText(this, getString(R.string.error_no_user_details), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                if (!mUser.isVisible()) {
//                    Toast.makeText(this, getString(R.string.error_user_must_check_in), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                if (!CommonUtils.isOnline(this)) {
//                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//                startActivity(IntentHelper.createParticipantsListIntent(this));
//                return true;
//
//            case R.id.ic_menu_favorite:
//                BioApp.pushDummyUsersToFirebase(this, mUsersRef);
//                //TODO: Show favorites activity
//                return true;
//
//            case R.id.ic_menu_session:
//                //TODO: Show sessions activity
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * FavoriteChangeListener
     */
    @Override
    public void onCheckBoxClick(int position, Session session) {
        session.setBookmarked(!session.isBookmarked());
        mCustomAdapter.getItems().set(position, session);
        mCustomAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "CheckBox Click + Session: " + position + ", " + session.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create Session details activity (getItemAtPosition(position))
        Toast.makeText(MainActivity.this, "List Click", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_user_edit:
                if (mUser == null) {
                    startActivity(IntentHelper.createUserDetailsActivityIntent(this, null));
                    return;
                }

                if (mUser.isVisible()) {
                    Toast.makeText(this, getString(R.string.error_user_edit_available), Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(IntentHelper.createUserDetailsActivityIntent(this, mUser));
                break;

            case R.id.ib_user_status:
                if (mUser != null) {
                    FirebaseHelper.changeAvailabilityState(this, mUsersRef, mUser, this);
                } else {
                    Toast.makeText(this, getString(R.string.error_no_user_details), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    /**
     * Firebase.CompletionListener
     */
    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if (firebaseError != null) {
            Toast.makeText(this, getString(R.string.error_contacting_server) + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.changeStatusIcon(mUser, mBtnStatus);
        FileHelper.updateUserDataToSharedPreferences(this, mSharedPreferences, mUser);
    }

    /**
     * PullRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        // TODO: Query server with sessions and populate list
    }

    /**
     * ChildEventListener
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (mUser != null && mUser.isVisible()) {
            Map<String, User> newUser = (Map<String, User>) dataSnapshot.getValue();
            Toast.makeText(this, newUser.get("name") + " has checked in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (mUser != null && mUser.isVisible()) {
            Map<String, User> newUser = (Map<String, User>) dataSnapshot.getValue();
            Toast.makeText(this, newUser.get("name") + " has checked out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    /**
     * Generic getters and setters
     */
    public Firebase getContactsChildRef() {
        return mUsersRef;
    }

    public void setContactsChildRef(Firebase contactsChildRef) {
        mUsersRef = contactsChildRef;
    }

    public Firebase getTalksChildRef() {
        return mSessionsRef;
    }

    public void setTalksChildRef(Firebase talksChildRef) {
        mSessionsRef = talksChildRef;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

}