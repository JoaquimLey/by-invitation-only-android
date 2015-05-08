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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerCallbacks;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerFragment;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

/**
 * BaseActivity needed for correct ActionBar material Toolbar/ActionBar and shared components
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected NavigationDrawerFragment mNavigationDrawerFragment;
    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setupNavigationDrawer();
        mUser = FileHelper.getUserFromSharedPreferences(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (mUser != null) {
            BioApp.getInstance().setCurrentUser(mUser);
        }
        writeProfileInfo();

        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.closeDrawer();
        }
    }

    private void setupNavigationDrawer() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolBar);
    }

    protected void writeProfileInfo() {
        String userName = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.shared_pref_user_details_name), "");
        String userEmail = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.shared_pref_user_details_email), "");
        String userAvatarUri = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.shared_pref_user_details_photo_uri), "");

        if (userName != null && !userName.isEmpty()) {
            mNavigationDrawerFragment.getUserName().setText(userName);
        }

        if (userEmail != null && !userEmail.isEmpty()) {
            mNavigationDrawerFragment.getUserEmail().setText(userEmail);
        }

        if (userAvatarUri != null && !userAvatarUri.isEmpty()) {
            Picasso.with(this).load(userAvatarUri).transform(new ImageCircleTransform())
                    .into((ImageView) findViewById(R.id.imgAvatar));
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    protected abstract int getLayoutResource();
}
