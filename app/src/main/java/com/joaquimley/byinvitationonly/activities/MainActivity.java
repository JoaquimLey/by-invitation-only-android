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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomListAdapter;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.interfaces.FavoriteChangeListener;
import com.joaquimley.byinvitationonly.model.Session;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;


public class MainActivity extends Activity implements PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, FavoriteChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Firebase mContactsChildRef;
    private Firebase mSessionRef;
    private User mMyUser;
    private ListView mList;
    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;
    private CustomListAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        init();
        // FIXME:
        Log.i(TAG, "Before size: " + BioApp.getInstance().getSessionList().size());
        FileHelper.importSessionDataFromFile(MainActivity.this, BioApp.getInstance().getSessionList(), 1);
        Log.i(TAG, "After size: " + BioApp.getInstance().getSessionList().size());


//        Conference conf = FileHelper.importConferenceDataFromFile(this);
//        Log.i(TAG, "Conference: " + conf.getFullName());


        mCustomAdapter = new CustomListAdapter(MainActivity.this, BioApp.getInstance().getSessionList(), this);
        mList.setAdapter(mCustomAdapter);
    }

    /**
     * Initialize Firebase references, UI elements, listeners
     */
    private void init() {
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mContactsChildRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_contacts));
        mSessionRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_sessions));

        CustomUi.simplifyActionBay(getActionBar(), R.drawable.action_bar_app);
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);

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
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onCheckBoxClick(int position, Session session) {
        session.setBookmarked(!session.isBookmarked());
        mCustomAdapter.getItems().set(position, session);
        mCustomAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "CheckBox Click + Session: " + position + ", " + session.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create Session activity with item details (getItemAtPosition(position))
        Toast.makeText(MainActivity.this, "List Click", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onRefresh() {
        // TODO: See what type of list is and update according
    }

    public Firebase getContactsChildRef() {
        return mContactsChildRef;
    }

    public void setContactsChildRef(Firebase contactsChildRef) {
        mContactsChildRef = contactsChildRef;
    }

    public Firebase getTalksChildRef() {
        return mSessionRef;
    }

    public void setTalksChildRef(Firebase talksChildRef) {
        mSessionRef = talksChildRef;
    }
}