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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.joaquimley.byinvitationonly.R;
import com.joaquimley.byinvitationonly.helper.FileHelper;
import com.joaquimley.byinvitationonly.model.User;
import com.joaquimley.byinvitationonly.util.ImageCircleTransform;
import com.joaquimley.byinvitationonly.util.IntentHelper;
import com.squareup.picasso.Picasso;

public class EditUserDetailsActivity extends BaseActivity implements View.OnClickListener {

    public static final int RESULT_GALLERY = 0;

    private User mUser;
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtDescription;
    private ImageView mIvUserPhoto;
    private Uri mImageUri;
    private boolean mImageChangedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_ab_drawer);

        Bundle data = getIntent().getExtras();
        if (data != null && data.getParcelable("user") != null) {
            mUser = data.getParcelable("user");
        }
        init();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_user_details;
    }

    private void init() {
        mEtName = ((EditText) findViewById(R.id.et_edit_user_details_name));
        mEtEmail = ((EditText) findViewById(R.id.et_edit_user_details_email));
        mEtDescription = ((EditText) findViewById(R.id.et_edit_user_details_description));

        mIvUserPhoto = (ImageView) findViewById(R.id.iv_profile_picture);
        mIvUserPhoto.setOnClickListener(this);
        Picasso.with(this).load(R.drawable.image_placeholder).transform(new ImageCircleTransform()).into(mIvUserPhoto);

        if (mUser != null) {
            mEtName.setText(mUser.getName());
            mEtEmail.setText(String.valueOf(mUser.getEmail()));
            mEtDescription.setText(mUser.getDescription());
            if(!mUser.getPhotoBase64().isEmpty()){
                Picasso.with(this)
                        .load(FileHelper.decodeBase64ToFile(this, mUser.getPhotoBase64()))
                        .transform(new ImageCircleTransform())
                        .into(mIvUserPhoto);
            }
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

            case R.id.iv_profile_picture:
                startActivityForResult(IntentHelper.createPickImageIntent(this), RESULT_GALLERY);
                break;

            case R.id.btn_save:

                if (mEtName.getText().length() < 0 || mEtEmail.getText().length() < 0 || mEtDescription.getText().length() < 0) {
                    Toast.makeText(this, getString(R.string.error_fill_required_fields), Toast.LENGTH_LONG).show();
                    return;
                }

                if (mUser == null) {
                    mUser = new User(String.valueOf(mEtName.getText()), String.valueOf(mEtEmail.getText()),
                            String.valueOf(mEtDescription.getText()), "", false);

                } else {
                    mUser.setName(String.valueOf(mEtName.getText()));
                    mUser.setEmail(String.valueOf(mEtEmail.getText()));
                    mUser.setDescription(String.valueOf(mEtDescription.getText()));
                }

                if(mImageChangedFlag && mImageUri != null){
                    mUser.setPhotoBase64(FileHelper.encodeUriToBase64(this, mImageUri));
                }
                mUser.setVisible(false);

                FileHelper.updateUserDataToSharedPreferences(this, PreferenceManager.getDefaultSharedPreferences(this), mUser);
                startActivity(IntentHelper.createMainActivityIntent(this));
                break;

            case R.id.btn_cancel:
                finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case EditUserDetailsActivity.RESULT_GALLERY:
                if (data != null) {
                    mImageChangedFlag = true;
                    mImageUri = data.getData();
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit()
                            .putString(getString(R.string.shared_pref_user_details_photo_uri), String.valueOf(mImageUri))
                            .apply();

                    Picasso.with(this)
                            .load(mImageUri)
                            .transform(new ImageCircleTransform())
                            .into(mIvUserPhoto);
                }
                break;

            default:
        }
    }


}
