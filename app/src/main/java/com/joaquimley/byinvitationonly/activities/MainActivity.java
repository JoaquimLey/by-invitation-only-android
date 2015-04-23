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

package com.joaquimley.byinvitationonly.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomSessionListAdapter;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.joaquimley.byinvitationonly.util.IntentHelper;
import com.squareup.picasso.Picasso;


public class MainActivity extends Activity implements PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, Firebase.CompletionListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Firebase mUsersRef;
    private Firebase mSessionsRef;
    private ListView mList;
    private ImageButton mBtnStatus;
    private ImageButton mBtnEdit;
    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;

    private CustomSessionListAdapter mCustomAdapter;
    //FIXME:
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle data = getIntent().getExtras();
        if(data != null){
            mUser = data.getParcelable("user");
        } else {
            mUser = new User("Joaquim Ley", "me@joaquimley.com", "Android Developer",
                    "https://graph.facebook.com/1254180865/picture?type=normal");
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        BioApp.getInstance().setConference(FileHelper.importConferenceDataFromFile(this));
        init();

        // FIXME: Get sessions from CsvFile
//        Log.i(TAG, "Before size: " + BioApp.getInstance().getSessionList().size());
//        FileHelper.importSessionDataFromFile(MainActivity.this, BioApp.getInstance().getSessionList(), 1);
//        FirebaseHelper.exportSessions(MainActivity.this, mSessionsRef);
//        BioApp.pushDummySessionsToFirebase(this, mSessionsRef);
//        Log.i(TAG, "After size: " + BioApp.getInstance().getSessionList().size());

        BioApp.pushDummyUsersToFirebase(this, mUsersRef, mUser);
//        mCustomAdapter = new CustomSessionListAdapter(MainActivity.this, BioApp.getInstance().getSessionList(), this);
    }

    /**
     * Initialize Firebase references, UI elements, listeners
     */
    private void init() {
        CustomUi.simplifyActionBay(getActionBar(), "",  R.drawable.action_bar_app);
        mBtnEdit = (ImageButton) findViewById(R.id.ib_user_edit);
        mBtnEdit.setOnClickListener(this);
        mBtnStatus = (ImageButton) findViewById(R.id.ib_user_status);
        mBtnStatus.setOnClickListener(this);
        changeStatusIcon();
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_users));
        mSessionsRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_sessions));
        // Profile
        ((TextView) findViewById(R.id.tv_user_name)).setText(mUser.getName());
        ((TextView) findViewById(R.id.tv_user_email)).setText(mUser.getEmail());
        ((TextView) findViewById(R.id.tv_user_description)).setText(mUser.getDescription());
        Picasso.with(this).load(mUser.getPhotoUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder_error)
                .transform(new ImageCircleTransform())
                .into((ImageView) findViewById(R.id.iv_user_pic));

        ((TextView) findViewById(R.id.tv_up_coming_sessions)).setText(BioApp.getInstance().getConference().getAcronym()
                + " - " + getString(R.string.text_up_coming_sessions));
        // List
//        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mPullRefreshLayout.setOnRefreshListener(this);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
        mList.setAdapter(mCustomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ic_menu_group:
                if(mUser == null){
                    Toast.makeText(this, "Current user is null", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(!mUser.isVisible()){
                    Toast.makeText(this, getString(R.string.error_user_must_be_visible), Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!BioApp.isOnline(this)){
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                    return false;
                }

                startActivity(IntentHelper.createParticipantsListIntent(this, mUser, BioApp.createDummyUsers()));

            case R.id.ic_menu_favorite:
                //TODO: Show favorites activity
                return true;

            case R.id.ic_menu_session:
                //TODO: Show sessions activity
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onCheckBoxClick(int position, Session session) {
//        session.setBookmarked(!session.isBookmarked());
//        mCustomAdapter.getItems().set(position, session);
//        mCustomAdapter.notifyDataSetChanged();
//        Toast.makeText(MainActivity.this, "CheckBox Click + Session: " + position + ", " + session.getTitle(), Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create Session activity with item details (getItemAtPosition(position))
        Toast.makeText(MainActivity.this, "List Click", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if(firebaseError != null){
            Toast.makeText(this, "Error contacting server", Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setVisible(!mUser.isVisible());
        changeStatusIcon();
    }

    private void changeStatusIcon() {
        if(mUser.isVisible()){
            Toast.makeText(this, getString(R.string.user_visible), Toast.LENGTH_LONG).show();
            mBtnStatus.setBackgroundResource(R.drawable.ic_status_green);
            return;
        }
        mBtnStatus.setBackgroundResource(R.drawable.ic_status_red);
        Toast.makeText(this, getString(R.string.user_invisible), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_user_edit:
                if(!BioApp.isOnline(this)){
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(IntentHelper.userDetailsActivityIntent(this, mUser));
                break;
            case R.id.ib_user_status:
                FirebaseHelper.changeAvailabilityState(this, mUser, mUsersRef, this);
                break;
            default:
        }
    }

    @Override
    public void onRefresh() {
        // TODO: See what type of list is and update according
    }

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
}