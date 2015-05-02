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
import android.widget.EditText;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.IntentHelper;

public class EditUserDetailsActivity extends Activity implements View.OnClickListener {

    private User mUser;
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtDescription;
    private EditText mEtPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        CustomUi.simplifyActionBay(getActionBar(), "", R.drawable.action_bar_app);

        mUser = null;
        Bundle data = getIntent().getExtras();
        if (data != null && data.getParcelable("user") != null) {
            mUser = data.getParcelable("user");
        }
        init();
    }

    private void init() {
        mEtName = ((EditText) findViewById(R.id.et_edit_user_details_name));
        mEtEmail = ((EditText) findViewById(R.id.et_edit_user_details_email));
        mEtDescription = ((EditText) findViewById(R.id.et_edit_user_details_description));
        mEtPhotoUrl = ((EditText) findViewById(R.id.et_edit_user_details_photo_url));

        if (mUser != null) {
            mEtName.setText(mUser.getName());
            mEtEmail.setText(String.valueOf(mUser.getEmail()));
            mEtDescription.setText(mUser.getDescription());
            mEtPhotoUrl.setText(mUser.getPhotoUrl());
        }

        (findViewById(R.id.btn_save)).setOnClickListener(this);
        (findViewById(R.id.btn_cancel)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_user_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                if (mEtName.getText().length() < 0 || mEtEmail.getText().length() < 0 || mEtDescription.getText().length() < 0) {
                    Toast.makeText(this, "Please insert all mandatory details", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mUser == null) {

                    String photoUrl = String.valueOf(mEtPhotoUrl.getText());
                    if (photoUrl.length() > 4) {
                        mUser = new User(String.valueOf(mEtName.getText()), String.valueOf(mEtEmail.getText()),
                                String.valueOf(mEtDescription.getText()), String.valueOf(mEtPhotoUrl.getText()), false);

                    } else {
                        mUser = new User(String.valueOf(mEtName.getText()), String.valueOf(mEtEmail.getText()),
                                String.valueOf(mEtDescription.getText()), "", false);
                    }

                } else {
                    mUser.setName(String.valueOf(mEtName.getText()));
                    mUser.setEmail(String.valueOf(mEtEmail.getText()));
                    mUser.setDescription(String.valueOf(mEtDescription.getText()));
                    mUser.setPhotoUrl(String.valueOf(mEtPhotoUrl.getText()));
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                FileHelper.updateUserFromSharedPreferences(this, sharedPreferences, mUser);
//                startActivity(IntentHelper.createMainActivityIntent(this, mUser));
                startActivity(IntentHelper.createMainActivityIntent(this));
                break;

            case R.id.btn_cancel:
                finish();
        }
    }
}
