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
 * Model for Conference
 */
public class Conference {

    private String mAcronym;
    private String mFullName;
    private String mLocation;
    private String mDates;
    private String mLogoUrl;
    private String mWebsite;
    private String mCallForPapers;

    public Conference() {
        // No args constructor
    }

    public Conference(String acronym, String fullName,  String location, String dates, String logoUrl,
                      String website, String callForPapers) {

        mAcronym = acronym;
        mFullName = fullName;
        mLocation = location;
        mDates = dates;
        mLogoUrl = logoUrl;
        mWebsite = website;
        mCallForPapers = callForPapers;

    }

    public String getAcronym() {
        return mAcronym;
    }

    public void setAcronym(String acronym) {
        mAcronym = acronym;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDates() {
        return mDates;
    }

    public void setDates(String dates) {
        mDates = dates;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        mLogoUrl = logoUrl;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getCallForPapers() {
        return mCallForPapers;
    }

    public void setCallForPapers(String callForPapers) {
        mCallForPapers = callForPapers;
    }
}
