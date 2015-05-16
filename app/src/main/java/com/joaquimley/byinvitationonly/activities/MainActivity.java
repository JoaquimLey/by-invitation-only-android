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

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.joaquimley.byinvitationonly.ui.NavigationDrawerCallbacks;
import com.joaquimley.byinvitationonly.util.CommonUtils;

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks,
        PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, FavoriteChangeListener, ChildEventListener, Firebase.CompletionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int EDIT_USER_DETAILS = 0;
    private static final int PARTICIPANTS_ACTIVITY = 1;
    private static final int BOOKMARKS_ACTIVITY = 2;

    private Firebase mSessionsRef;
    private Firebase mUsersRef;
    private PullRefreshLayout mPullRefreshLayout;
    private SharedPreferences mSharedPreferences;
    private CustomSessionListAdapter mCustomAdapter;
    private ListView mList;
    private Menu mMenu;
    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BioApp.getInstance().setConference(FileHelper.importConferenceDataFromFile(this));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTitle(BioApp.getInstance().getConference().getAcronym());
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
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_users));
        mSessionsRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_sessions));
        if (mSessionsRef != null) {
            mSessionsRef.addChildEventListener(this);
        }
        // List
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
        if(mCustomAdapter == null){
            mCustomAdapter = new CustomSessionListAdapter(MainActivity.this, BioApp.getInstance().getSessionsList(), this);
            mList.setAdapter(mCustomAdapter);
        }
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
                if (!CommonUtils.isOnline(this)) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mUser == null) {
                    AlertDialog.Builder checkoutAlertDialog = new AlertDialog.Builder(this);
                    checkoutAlertDialog
                            .setTitle("New Profile")
                            .setMessage("You must create your profile first, do you wish to create now?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    callCreateProfile();
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                    return;
                }

                if (!mUser.isVisible()) {
                    Toast.makeText(this, getString(R.string.error_user_must_check_in), Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivityForResult(IntentHelper.createParticipantsListIntent(this), PARTICIPANTS_ACTIVITY);
                break;

            case 2:
                startActivity(IntentHelper.createBookmarksActivity(this));
                break;

            default:
        }
    }

    /**
     * Creates a user details activity, here to use the caller activity as context.
     */
    private void callCreateProfile() {
        startActivityForResult(IntentHelper.createUserDetailsActivityIntent(this, null), EDIT_USER_DETAILS);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: Create session details activity
    }


    @Override
    public void onRefresh() {
        mCustomAdapter.setItems(BioApp.getInstance().getSessionsList());
        mCustomAdapter.notifyDataSetChanged();
        mPullRefreshLayout.setRefreshing(false);
    }

    /**
     * Sessions list listener
     */
    @Override
    public void onCheckBoxClick(int position, Session session) {
        session.setBookmarked(!session.isBookmarked());
        mCustomAdapter.getItems().set(position, session);
        mCustomAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "CheckBox Click + Session: " + position + ", " +
                session.getTitle(), Toast.LENGTH_SHORT).show();
    }

    /**
     * PullRefreshLayout.OnRefreshListener
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.w(TAG, "onChildAdded session: " + dataSnapshot.getValue());
//        Session session = dataSnapshot.getValue(Session.class);
//        if (!BioApp.getInstance().getSessionsList().contains(session)) {
//            BioApp.getInstance().getSessionsList().add(session);
//            mCustomAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (CommonUtils.removeSessionFromList(dataSnapshot.getValue(Session.class),
                BioApp.getInstance().getSessionsList())) {
            mCustomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        mCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mUser = BioApp.getInstance().getCurrentUser();
        switch (requestCode) {
            case MainActivity.PARTICIPANTS_ACTIVITY:
                if (mUser.isVisible()) {
                    mMenu.findItem(R.id.ib_user_status).setIcon(R.drawable.ic_status_green);
                } else {
                    mMenu.findItem(R.id.ib_user_status).setIcon(R.drawable.ic_status_red);
                }
                // TODO: Handle
                break;

            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.ib_user_status:
                if (mUser == null) {
                    AlertDialog.Builder checkoutAlertDialog = new AlertDialog.Builder(this);
                    checkoutAlertDialog
                            .setTitle("New Profile")
                            .setMessage(getString(R.string.error_must_create_profile_first))
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    callCreateProfile();
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                    return true;
                }
                mProgressDialog = new MaterialDialog.Builder(this)
                        .title("Connecting")
                        .content(getString(R.string.please_wait))
                        .progress(true, 0)
                        .show();
                FirebaseHelper.changeAvailabilityState(this, mUsersRef, mUser, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Firebase listener
     */
    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if (firebaseError != null) {
            Toast.makeText(this, getString(R.string.error_contacting_server) + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        FileHelper.updateUserDataToSharedPreferences(this, mSharedPreferences, mUser);
        CommonUtils.changeMenuItemIcon(mUser, mMenu.findItem(R.id.ib_user_status));
        mProgressDialog.dismiss();
        Toast.makeText(this, getString(R.string.text_status_updated), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        CommonUtils.changeMenuItemIcon(mUser, mMenu.findItem(R.id.ib_user_status));
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Generic getters and setters
     */
    public Firebase getSessionsChildRef() {
        return mSessionsRef;
    }

    public void setSessionsChildRef(Firebase talksChildRef) {
        mSessionsRef = talksChildRef;
    }

    public Menu getMenu() {
        return mMenu;
    }

    public void setMenu(Menu menu) {
        mMenu = menu;
    }
}