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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomUserListAdapter;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParticipantsList extends Activity implements ChildEventListener {

    private static final String TAG = ParticipantsList.class.getSimpleName();
    private ArrayList<User> mUsersList;
    private SharedPreferences mSharedPreferences;
    private Firebase mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        init();
    }

    private void init() {

        User user = FileHelper.getUserFromSharedPreferences(this, PreferenceManager.getDefaultSharedPreferences(this));
        if(user == null){
            CustomUi.createAlertDialog(this, "Error", "There was a error retering your user data");
            finish();
        }
        mUsersList = new ArrayList<>();
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_users));
        if(mUsersRef != null){
            mUsersRef.addChildEventListener(this);
        }

        CustomUi.simplifyActionBay(getActionBar(), "", R.drawable.action_bar_app);
        ((TextView) findViewById(R.id.tv_participant_name)).setText(user.getName());
        ((TextView) findViewById(R.id.tv_participant_email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.tv_participant_description)).setText(user.getDescription());
        if(!user.getPhotoBase64().isEmpty() && mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), "") != null){
            Picasso.with(this).load(mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), ""))
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .transform(new ImageCircleTransform())
                    .into((ImageView) findViewById(R.id.iv_participant_pic));
        }

        ((ListView) findViewById(R.id.list)).setAdapter(new CustomUserListAdapter(this, mUsersList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_participants_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        mUsersList.add((User) dataSnapshot.getValue());
        Log.w(TAG, "Users list: " + mUsersList);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
