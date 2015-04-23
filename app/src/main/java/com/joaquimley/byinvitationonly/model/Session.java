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

/**
 * Model for Session
 */

public class Session {

    private String mTitle;
    private String mPresenter;
    private String mAbstract;
    private String mRoom;
    private String mImageUrl;
    private String mDate;
    private String mStartHour;
    private String mEndHour;
    private int mFeedback;
    private boolean mIsBookmark;

    public Session(){
        // No args constructor
    }

    public Session(String title, String presenter, String sessionAbstract, String room,
                   String date, String startHour, String endHour, String imageUrl) {

        mTitle = title;
        mPresenter = presenter;
        mAbstract = sessionAbstract;
        mRoom = room;
        mDate = date;
        mStartHour = startHour;
        mEndHour = endHour;

        mImageUrl = imageUrl;
        mIsBookmark = false;
        mFeedback = 0;
    }

   @Override
    public String toString() {
        return "Session{" +
                "Title" + mTitle + '\'' +
                "Presenter='" + mPresenter + '\'' +
                "Abstract='" + mAbstract + '\'' +
                "Room='" + mRoom + '\'' +
                "Date='" + mDate + '\'' +
                "StartHour='" + mStartHour + '\'' +
                "EndHour='" + mEndHour + '\'' +
                "Bookmarked='" + mIsBookmark + '\'' +
                "Feedback='" + mFeedback + '\'' +
                '}';
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPresenter() {
        return mPresenter;
    }

    public void setPresenter(String presenter) {
        mPresenter = presenter;
    }

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String sessionAbstract) {
        mAbstract = sessionAbstract;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getStartHour() {
        return mStartHour;
    }

    public void setStartHour(String startHour) {
        mStartHour = startHour;
    }

    public String getEndHour() {
        return mEndHour;
    }

    public void setEndHour(String endHour) {
        mEndHour = endHour;
    }

    public int getFeedback() {
        return mFeedback;
    }

    public void setFeedback(int feedback) {
        mFeedback = feedback;
    }

    public boolean isBookmarked() {
        return mIsBookmark;
    }

    public void setBookmarked(boolean isBookmark) {
        mIsBookmark = isBookmark;
    }
}