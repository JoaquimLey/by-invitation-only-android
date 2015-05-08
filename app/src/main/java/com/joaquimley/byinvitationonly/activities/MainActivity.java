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

import android.content.Intent;
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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks,
        PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, ValueEventListener, FavoriteChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int EDIT_USER_DETAILS = 0;

    private Firebase mSessionsRef;
    private PullRefreshLayout mPullRefreshLayout;
    private SharedPreferences mSharedPreferences;
    private CustomSessionListAdapter mCustomAdapter;
    private User mUser;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BioApp.getInstance().setConference(FileHelper.importConferenceDataFromFile(this));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        if (mSessionsRef != null) {
            mSessionsRef.addValueEventListener(this);
        }
        // List
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
        mCustomAdapter = new CustomSessionListAdapter(MainActivity.this, BioApp.getInstance().getSessionsList(), this);
        mList.setAdapter(mCustomAdapter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                if (mNavigationDrawerFragment != null && mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                break;

            case 1:
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

                if (FileHelper.getUserFromSharedPreferences(this, mSharedPreferences) == null) {
                    Toast.makeText(this, this.getString(R.string.error_create_user_profile_first), Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(IntentHelper.createParticipantsListIntent(this));
                break;

            case 2:
                startActivity(IntentHelper.createBookmarksActivity(this));
                break;

            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: Create session details activity
    }

    /**
     * PullRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        mCustomAdapter.setItems(BioApp.getInstance().getSessionsList());
        mCustomAdapter.notifyDataSetChanged();
        mPullRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCheckBoxClick(int position, Session session) {
        session.setBookmarked(!session.isBookmarked());
        mCustomAdapter.getItems().set(position, session);
        mCustomAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "CheckBox Click + Session: " + position + ", " +
                session.getTitle(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Sessions list listener
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MainActivity.EDIT_USER_DETAILS:
                if (data != null) {
                    Toast.makeText(this, getString(R.string.text_profile_updated), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            /**
             * case R.id.XPTO:
             *  doStuff()
             *  break;
             */

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Generic getters and setters
     */
    public Firebase getSessionsChildRef() {
        return mSessionsRef;
    }

    public void setSEssionsChildRef(Firebase talksChildRef) {
        mSessionsRef = talksChildRef;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}