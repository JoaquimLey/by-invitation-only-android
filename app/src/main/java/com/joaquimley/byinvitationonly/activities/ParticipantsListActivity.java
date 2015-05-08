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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomUserListAdapter;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.helper.FirebaseHelper;
import com.joaquimley.byinvitationonly.helper.IntentHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CommonUtils;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

public class ParticipantsListActivity extends BaseActivity implements PullRefreshLayout.OnRefreshListener,
        ChildEventListener, View.OnClickListener, AdapterView.OnItemClickListener, Firebase.CompletionListener {

    private static final String TAG = ParticipantsListActivity.class.getSimpleName();
    private static final int EDIT_USER_DETAILS = 0;

    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;
    private CustomUserListAdapter mCustomAdapter;
    private Firebase mUsersRef;
    private ImageButton mBtnStatus;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mUser = FileHelper.getUserFromSharedPreferences(this, mSharedPreferences);
        if (mUser == null) {
            CommonUtils.createAlertDialog(this, "Error", getString(R.string.error_user_data));
            finish();
        }

        init();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_participants_list;
    }

    private void init() {
        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
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
        // List
        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        ListView mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        mList.setItemsCanFocus(true);
        mCustomAdapter = new CustomUserListAdapter(this, BioApp.getInstance().getUsersList());
        mList.setAdapter(mCustomAdapter);
    }

    private void loadProfileInfo() {
        mUser = FileHelper.getUserFromSharedPreferences(this, mSharedPreferences);
        if (mUser != null) {
            BioApp.getInstance().setCurrentUser(mUser);
            ((TextView) findViewById(R.id.tv_user_name)).setText(mUser.getName());
            ((TextView) findViewById(R.id.tv_user_email)).setText(mUser.getEmail());
            ((TextView) findViewById(R.id.tv_user_description)).setText(mUser.getDescription());

            if (!mUser.getPhotoBase64().isEmpty() && mSharedPreferences
                    .getString(getString(R.string.shared_pref_user_details_photo_uri), "") != null) {

                Picasso.with(this).load(mSharedPreferences.
                        getString(getString(R.string.shared_pref_user_details_photo_uri), ""))
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder_error)
                        .transform(new ImageCircleTransform())
                        .into((ImageView) findViewById(R.id.iv_user_pic));
            } else {
                Picasso.with(this).load(R.drawable.image_placeholder).into((ImageView) findViewById(R.id.iv_user_pic));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ParticipantsListActivity.EDIT_USER_DETAILS:
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        FirebaseHelper.changeAvailabilityState(this, mUsersRef, mUser, this);
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Check in");
                        alertDialogBuilder
                                .setMessage("You must be checked in to see other participants")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                    }
                }
                break;

            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_user_edit:
                if (mUser == null) {
                    startActivityForResult(IntentHelper.createUserDetailsActivityIntent(this, null), EDIT_USER_DETAILS);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Create Session details activity (getItemAtPosition(position))
        Toast.makeText(this, "List Click", Toast.LENGTH_SHORT).show();

    }

    /**
     * PullRefreshLayout.OnRefreshListener
     */
    @Override
    public void onRefresh() {
        mCustomAdapter.setItems(BioApp.getInstance().getUsersList());
        mCustomAdapter.notifyDataSetChanged();
        mPullRefreshLayout.setRefreshing(false);
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
     * ChildEventListener
     */
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);
        if (!user.getId().equals(mSharedPreferences.getString(getString(R.string.shared_pref_user_details_id), ""))) {
            BioApp.getInstance().getUsersList().add(dataSnapshot.getValue(User.class));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (CommonUtils.removeUserFromList(dataSnapshot.getValue(User.class), BioApp.getInstance().getUsersList())) {
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
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_participants_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
