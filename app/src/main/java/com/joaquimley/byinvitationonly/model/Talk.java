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

import java.util.Date;

/**
 * Model for Talk
 */

public class Talk  {

    private String mTitle;
    private Speaker mSpeaker;
    private String mDescription;
    private String mImageUrl;
    private Date mDate;
    private boolean mIsBookmark;

    public Talk(String title, Speaker speaker, String description, String imageUrl, Date date, boolean isBookmark) {
        mTitle = title;
        mSpeaker = speaker;
        mDescription = description;
        mImageUrl = imageUrl;
        mDate = date;
        mIsBookmark = isBookmark;
    }

   @Override
    public String toString() {
        return "Talk{" +
                "Name" + mTitle + '\'' +
                "Speaker='" + mSpeaker + '\'' +
                "Description='" + mDescription + '\'' +
                "Date='" + mDate + '\'' +
                "Bookmarked='" + mIsBookmark + '\'' +
                '}';
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Speaker getSpeaker() {
        return mSpeaker;
    }

    public void setSpeaker(Speaker speaker) {
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

    public boolean isBookmarked() {
        return mIsBookmark;
    }

    public void setBookmarked(boolean isBookmarked) {
        mIsBookmark = isBookmarked;
    }
}