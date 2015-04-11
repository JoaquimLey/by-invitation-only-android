package com.joaquimley.byinvitationonly.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Model for Talk
 */
public class Talk implements Parcelable {

    private String mTitle;
    private String mSpeaker;
    private String mDescription;
    private Date mStartTime;

    public Talk(String name, String speaker, String description, Date startTime) {
        mTitle = name;
        mSpeaker = speaker;
        mDescription = description;
        mStartTime = startTime;
    }



   @Override
    public String toString() {
        return "Talk{" +
                "Name" + mTitle + '\'' +
                "Speaker='" + mSpeaker + '\'' +
                "Description='" + mDescription + '\'' +
                "StartTime='" + mStartTime + '\'' +
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

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        mStartTime = startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mSpeaker);
        dest.writeString(this.mDescription);
        dest.writeLong(mStartTime != null ? mStartTime.getTime() : -1);
    }

    private Talk(Parcel in) {
        mTitle = in.readString();
        mSpeaker = in.readString();
        mDescription = in.readString();
        long tmpMStartTime = in.readLong();
        mStartTime = tmpMStartTime == -1 ? null : new Date(tmpMStartTime);
    }

    public static final Parcelable.Creator<Talk> CREATOR = new Parcelable.Creator<Talk>() {
        public Talk createFromParcel(Parcel source) {
            return new Talk(source);
        }

        public Talk[] newArray(int size) {
            return new Talk[size];
        }
    };

}
