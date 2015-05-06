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