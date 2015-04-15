/*
 * Copyright (c) 2015 Joaquim Ley
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

import java.util.Date;

/**
 * Model for Talk
 */

public class Talk  {

    private String mTitle;
    private String mSpeaker;
    private String mDescription;
    private String mImageUrl;
    private Date mDate;

    public Talk(String title, String speaker, String description, String imageUrl, Date date) {
        mTitle = title;
        mSpeaker = speaker;
        mDescription = description;
        mImageUrl = imageUrl;
        mDate = date;
    }

   @Override
    public String toString() {
        return "Talk{" +
                "Name" + mTitle + '\'' +
                "Speaker='" + mSpeaker + '\'' +
                "Description='" + mDescription + '\'' +
                "StartTime='" + mDate + '\'' +
                '}';
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSpeaker() {
        return mSpeaker;
    }

    public void setSpeaker(String speaker) {
        mSpeaker = speaker;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.mTitle);
//        dest.writeString(this.mSpeaker);
//        dest.writeString(this.mDescription);
//        dest.writeLong(mDate != null ? mDate.getTime() : -1);
//    }
//
//    private Talk(Parcel in) {
//        mTitle = in.readString();
//        mSpeaker = in.readString();
//        mDescription = in.readString();
//        long tmpMStartTime = in.readLong();
//        mDate = tmpMStartTime == -1 ? null : new Date(tmpMStartTime);
//    }
//
//    public static final Parcelable.Creator<Talk> CREATOR = new Parcelable.Creator<Talk>() {
//        public Talk createFromParcel(Parcel source) {
//            return new Talk(source);
//        }
//
//        public Talk[] newArray(int size) {
//            return new Talk[size];
//        }
//    };