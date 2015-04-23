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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joaquimley.byinvitationonly.BioApp;
import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.adapter.CustomUserListAdapter;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParticipantsList extends Activity {

    private User mUser;
    private ArrayList<User> mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);

        CustomUi.simplifyActionBay(getActionBar(), " Participants List", R.drawable.action_bar_app);
        Bundle data = getIntent().getExtras();
        mUser = data.getParcelable("user");
        mUsersList = BioApp.createDummyUsers();
        if(mUser == null || mUsersList == null){
            CustomUi.createAlertDialog(this, "Error", "There was a error getting user(s) details");
        }
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_participant_name)).setText(mUser.getName());
        ((TextView) findViewById(R.id.tv_participant_email)).setText(mUser.getEmail());
        ((TextView) findViewById(R.id.tv_participant_description)).setText(mUser.getDescription());
        Picasso.with(this).load(mUser.getPhotoUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder_error)
                .transform(new ImageCircleTransform())
                .into((ImageView) findViewById(R.id.iv_participant_pic));

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
}
