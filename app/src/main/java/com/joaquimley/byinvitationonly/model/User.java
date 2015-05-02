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

package com.joaquimley.byinvitationonly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for user/participant
 */

public class User implements Parcelable {

    private String mId;
    private String mName;
    private String mEmail;
    private String mDescription;
    private String mPhotoUrl;
    private boolean mIsVisible;

    public User() {
        // No args constructor
    }


    public User(String name, String email, String description, String photoUrl, boolean isVisible) {
        mName = name;
        mEmail = email;
        mDescription = description;
        mPhotoUrl = photoUrl;

        mIsVisible = isVisible;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id" + mId + '\'' +
                "Name" + mName + '\'' +
                "Email='" + mEmail + '\'' +
                "Description='" + mDescription + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mEmail);
        dest.writeString(this.mDescription);
        dest.writeString(this.mPhotoUrl);
        dest.writeByte(mIsVisible ? (byte) 1 : (byte) 0);
    }

    private User(Parcel in) {
        this.mName = in.readString();
        this.mEmail = in.readString();
        this.mDescription = in.readString();
        this.mPhotoUrl = in.readString();
        this.mIsVisible = in.readByte() != 0;
        this.mId = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
