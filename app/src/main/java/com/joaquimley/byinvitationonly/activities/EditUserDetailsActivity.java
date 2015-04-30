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
import android.view.View;
import android.widget.EditText;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.CustomUi;
import com.joaquimley.byinvitationonly.util.IntentHelper;

public class EditUserDetailsActivity extends Activity implements View.OnClickListener {

    private User mUser;
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        CustomUi.simplifyActionBay(getActionBar(), "", R.drawable.action_bar_app);

        mUser = null;
        Bundle data = getIntent().getExtras();
        if(data != null && data.getParcelable("user") != null){
            mUser = data.getParcelable("user");
        }
        if (mUser == null) {
            CustomUi.createAlertDialog(this, "No user!", "There was a error getting user details");
        }
        init();
    }

    private void init() {
        mEtName = ((EditText) findViewById(R.id.et_edit_user_details_name));
        mEtEmail = ((EditText) findViewById(R.id.et_edit_user_details_email));
        mEtDescription = ((EditText) findViewById(R.id.et_edit_user_details_description));

        mEtName.setText(mUser.getName());
        mEtEmail.setText(String.valueOf(mUser.getEmail()));
        mEtDescription.setText(mUser.getDescription());

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
                mUser.setName(String.valueOf(mEtName.getText()));
                mUser.setEmail(String.valueOf(mEtEmail.getText()));
                mUser.setDescription(String.valueOf(mEtDescription.getText()));

                startActivity(IntentHelper.mainActivityIntent(this, mUser));
                break;
            case R.id.btn_cancel:
                finish();
        }
    }
}
