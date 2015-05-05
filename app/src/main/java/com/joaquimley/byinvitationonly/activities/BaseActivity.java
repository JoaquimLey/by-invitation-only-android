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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerCallbacks;
import com.joaquimley.byinvitationonly.ui.NavigationDrawerFragment;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

/**
 * BaseActivity needed for correct ActionBar material style
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setupNavigationDrawer();
        writeProfileInfo();
    }


    private void setupNavigationDrawer() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

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
