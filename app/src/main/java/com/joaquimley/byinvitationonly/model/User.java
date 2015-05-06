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
    private String mPhotoBase64;
    private boolean mIsVisible;

    public User() {
        // No args constructor
    }


    public User(String name, String email, String description, String photoBase64, boolean isVisible) {
        mName = name;
        mEmail = email;
        mDescription = description;
        mPhotoBase64 = photoBase64;

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

    public String getPhotoBase64() {
        return mPhotoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        mPhotoBase64 = photoBase64;
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
        dest.writeString(this.mPhotoBase64);
        dest.writeByte(mIsVisible ? (byte) 1 : (byte) 0);
    }

    private User(Parcel in) {
        this.mName = in.readString();
        this.mEmail = in.readString();
        this.mDescription = in.readString();
        this.mPhotoBase64 = in.readString();
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
