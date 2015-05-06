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
