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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CommonUtils;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

public class BookmarksListActivity extends BaseActivity implements PullRefreshLayout.OnRefreshListener, ChildEventListener {

    private static final String TAG = BookmarksListActivity.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private PullRefreshLayout mPullRefreshLayout;
    private CustomUserListAdapter mCustomUserListAdapter;
    private Firebase mUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        init();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_participants_list;
    }

    private void init() {

        User user = FileHelper.getUserFromSharedPreferences(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (user == null) {
            CommonUtils.createAlertDialog(this, "Error", "There was a error with your user data");
            finish();
        }

        Firebase firebaseRef = FirebaseHelper.initiateFirebase(this);
        mUsersRef = FirebaseHelper.getChildRef(firebaseRef, getString(R.string.firebase_child_users));
        if (mUsersRef != null) {
            mUsersRef.addChildEventListener(this);
        }

        ((TextView) findViewById(R.id.tv_participant_name)).setText(user.getName());
        ((TextView) findViewById(R.id.tv_participant_email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.tv_participant_description)).setText(user.getDescription());

        if (!user.getPhotoBase64().isEmpty() && mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), "") != null) {
            Picasso.with(this).load(mSharedPreferences.getString(getString(R.string.shared_pref_user_details_photo_uri), ""))
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .transform(new ImageCircleTransform())
                    .into((ImageView) findViewById(R.id.iv_participant_pic));
        }

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(this);
        mCustomUserListAdapter = new CustomUserListAdapter(this, BioApp.getInstance().getUsersList());
        ((ListView) findViewById(R.id.list)).setAdapter(mCustomUserListAdapter);
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
    public void onRefresh() {
        mCustomUserListAdapter.setItems(BioApp.getInstance().getUsersList());
        mCustomUserListAdapter.notifyDataSetChanged();
        mPullRefreshLayout.setRefreshing(false);
    }

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
            mCustomUserListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
}
