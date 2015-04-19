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
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.Firebase;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomListAdapter;
import com.joaquimley.byinvitationonly.db.DatabaseHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.interfaces.FavoriteChangeListener;
import com.joaquimley.byinvitationonly.model.Contact;
import com.joaquimley.byinvitationonly.model.Talk;
import com.joaquimley.byinvitationonly.util.CustomUi;


public class MainActivity extends Activity implements PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, FavoriteChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Firebase mContactsChildRef;
    private Firebase mTalksChildRef;
    private Firebase mSpeakersRef;
    private Contact mMyContact;
    private ListView mList;
    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;
    private CustomListAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Firebase
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mSpeakersRef = FirebaseHelper.getChildRef(firebaseRef, "speakers");
        mContactsChildRef = FirebaseHelper.getChildRef(firebaseRef, "contacts");
        mTalksChildRef = FirebaseHelper.getChildRef(firebaseRef, "talks");

        init();

        mCustomAdapter = new CustomListAdapter(MainActivity.this, DatabaseHelper.createDummyEntries(MainActivity.this), this);
        mList.setAdapter(mCustomAdapter);

    }

    /**
     * Initialize Firebase references, UI elements, listeners
     */
    private void init() {
        CustomUi.setSimpleActionBar(getActionBar(), R.drawable.action_bar_app);
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
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
    public void onCheckBoxClick(int position, Talk talk) {
        talk.setBookmarked(!talk.isBookmarked());
        mCustomAdapter.getItems().set(position, talk);
        mCustomAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "CheckBox Click + Talk: " + position + ", " + talk.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create Talk activity with item details (getItemAtPosition(position))
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
        return mTalksChildRef;
    }

    public void setTalksChildRef(Firebase talksChildRef) {
        mTalksChildRef = talksChildRef;
    }
}